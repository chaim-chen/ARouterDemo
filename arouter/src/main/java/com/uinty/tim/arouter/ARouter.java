package com.uinty.tim.arouter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dalvik.system.DexFile;

/**
 * author : Administrator
 * time   : 2019/06/28
 * desc   : 中间类
 */
public class ARouter {

    private Map<String,Class<?extends Activity>> activityLists;

    private static ARouter aRouter =new ARouter();

    private Context context;

    public ARouter(){
        activityLists =new HashMap<>();
    }

    public static ARouter getInstance() {
        return aRouter;
    }

    public void init(Context context){
        this.context=context;
        List<Class<?>> classList = getClasses(context,"com.uinty.tim.util");
        for (Class<?> aClass : classList) {
            try {
                IArouter iArouter= (IArouter) aClass.newInstance();
                iArouter.putActivity();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 将类对象加入到集合
     * @param key
     * @param cls
     */
    public void putActivity(String key,Class<? extends Activity> cls){
        if (key!=null && cls!=null){
            activityLists.put(key,cls);
        }
    }
    private List<Class<?>> getClasses(Context mContext, String packageName) {
        List<Class<?>> classes = new ArrayList<>();
        try {
            String packageCodePath = mContext.getPackageCodePath();
            DexFile df = new DexFile(packageCodePath);
            String regExp = "^" + packageName + ".\\w+$";
            for (Enumeration iter = df.entries(); iter.hasMoreElements(); ) {
                String className = iter.nextElement().toString();
                if (className.startsWith(packageName)) {
                    classes.add(Class.forName(className));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classes;
    }

    /**
     * 页面跳转
     * @param key
     * @param bundle
     */
    public void jumpActivity(String key, Bundle bundle) {
        Class<? extends Activity> aClass = activityLists.get(key);
        Intent intent = new Intent(context, aClass);
        intent.putExtra("bundle",bundle);
        context.startActivity(intent);
    }
}
