package com.uinty.tim.annotation_compiler;

import com.google.auto.service.AutoService;
import com.uinty.tim.annotation.BindPath;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;

/**
 * author : Administrator
 * time   : 2019/06/28
 * desc   : 注解处理器 用来生成代码
 */

@AutoService(Processor.class)
public class AnnotationCompiler extends AbstractProcessor {

    private Filer filer;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        filer = processingEnv.getFiler();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new HashSet<>();
        types.add(BindPath.class.getCanonicalName());
        return types;
    }

    //声明注解支持的java 原版本
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return processingEnv.getSourceVersion();
    }

    //生成处理注解的文件
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        //获取到注解的所有类节点
        Set<? extends Element> envElementsAnnotatedWith = roundEnv.getElementsAnnotatedWith(BindPath.class);
        //用来保存
        Map<String, String> map = new HashMap<>();

        for (Element element : envElementsAnnotatedWith) {
            //类节点 TypeElement  方法节点 成员变量节点

            TypeElement typeElement= (TypeElement) element;

            //获取到注解的值login/login
            String key = typeElement.getAnnotation(BindPath.class).value();
            //获取到包名+类名
            String value = typeElement.getQualifiedName().toString();
            map.put(key,value);
        }
        if (map.size()>0){

            Writer writer=null;
            String utilName = "ActivityUtils" + System.currentTimeMillis();
            try {
                JavaFileObject sourceFile = filer.createSourceFile("com.uinty.tim.util." + utilName);
                writer=sourceFile.openWriter();

                writer.append("package com.uinty.tim.util;\n" +
                        "\n" +
                        "import com.uinty.tim.arouter.ARouter;\n" +
                        "import com.uinty.tim.arouter.IArouter;\n" +
                        "\n" +
                        "public  class "+utilName+" implements IArouter {\n" +
                        "    @Override\n" +
                        "    public void putActivity() {");
                for (String key : map.keySet()) {
                    writer.append("ARouter.getInstance().putActivity(\""+key+"\","+map.get(key)+".class);");
                }
                writer.append("}\n}");
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if (writer!=null){
                    try {
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return false;
    }
}
