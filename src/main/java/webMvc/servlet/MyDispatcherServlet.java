package webMvc.servlet;

import webMvc.annotation.MyAutowired;
import webMvc.annotation.MyController;
import webMvc.annotation.MyEntity;
import webMvc.annotation.MyService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.*;

/**
 * Created by sultan on 2018/7/20.
 */
public class MyDispatcherServlet extends HttpServlet{
    //配置文件路径
    private Properties contextConfig = new Properties();
    //所有待加载类
    private List<String> classNames = new ArrayList<>();
    //ioc容器
    private Map<String,Object> ioc = new HashMap<String , Object>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //等待请求
        doDispatch();
    }

    private void doDispatch() {
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        //开始启动

        //加载配置文件
        doLoadConfig(config.getInitParameter("contextConfigLocation"));
        //扫描相关类
        doScanner(contextConfig.getProperty("scanPackage"));
        //实例化，初始化相关类
        doInstance();
        //自动注入
        doAutowired();
        //spring初始化完成
        
        //初始化HandlerMapping，springmvc部分
        doHandlerMapping();
        //
    }

    private void doHandlerMapping() {
    }

    private void doAutowired() {
        if(ioc.isEmpty()){return;}
        //循环ioc容器，对需要赋值的属性自动赋值
        for (Map.Entry<String,Object> stringObjectEntry : ioc.entrySet()) {
            Field[] fields = stringObjectEntry.getValue().getClass().getDeclaredFields();
            for (Field field : fields) {
                if(!field.isAnnotationPresent(MyAutowired.class)){continue;}

                MyAutowired myAutowired = field.getAnnotation(MyAutowired.class);
                String beanName = myAutowired.value().trim();
                if("".equals(beanName)){
                    beanName = field.getType().getName();
                }

                //检测beanName
                System.out.println("当前注入："+stringObjectEntry.getValue().getClass().getName()+"属性："+field.getName()+"值："+beanName);

                field.setAccessible(true);

                try {
                    field.set(stringObjectEntry.getValue(),ioc.get(beanName));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    continue;
                }


            }
            
        }
    }

    private void doInstance() {
        if(classNames.isEmpty()){return;}

        try {
            for (String className : classNames) {
                Class<?> clazz = Class.forName(className);
                //初始化应该初始化的类
                if(clazz.isAnnotationPresent(MyController.class)){
                    String beanName = this.lowerFirstCase(clazz.getName());
                    //检测beanName
                    System.out.println("当前初始化容器："+className+"值："+beanName);

                    ioc.put(beanName,clazz.newInstance());
                }else if(clazz.isAnnotationPresent(MyService.class)){
                    /*
                    * 1.默认采用类名首字母小写
                    * 2.采用注解自定义名字，优先使用
                    * 3.如果是接口类型？？？
                    * */
                    MyService service = clazz.getAnnotation(MyService.class);
                    String beanName = service.value();
                    if("".equals(beanName.trim())){
                        beanName = this.lowerFirstCase(clazz.getName());
                    }
                    Object instance = clazz.newInstance();
                    //检测beanName
                    System.out.println("当前初始化容器："+className+"值："+beanName);
                    ioc.put(beanName,instance);
                    for (Class<?> aClass : clazz.getInterfaces()) {

                        try {
                            //检测beanName
                            System.out.println("当前初始化容器："+aClass.getName()+"值："+aClass.getName());
                            ioc.put(aClass.getName(),instance);
                        }catch (Exception e){
                            throw new Exception(aClass.getName()+"实现类"+beanName+"重复");
                        }
                    }



                }else if(clazz.isAnnotationPresent(MyEntity.class)){

                }else{
                    continue;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void doScanner(String scanPackage) {
        System.out.println("文件路径："+scanPackage);
        System.out.println("文件替换前路径："+"/"+scanPackage.replaceAll("\\.","/"));
        URL url = this.getClass().getClassLoader().getResource("/"+scanPackage.replaceAll("\\.","/"));
        System.out.println("文件替换后路径："+url.getPath());
        File classDir = new File(url.getFile());

        for (File file : classDir.listFiles()) {
            if(file.isDirectory()){
                doScanner(scanPackage+"."+file.getName());
            }else{
                String className = scanPackage+"."+file.getName().replaceAll(".class","");
                classNames.add(className);
            }
        }
    }

    private void doLoadConfig(String contextConfigLocation) {
        InputStream is =this.getClass().getClassLoader().getResourceAsStream(contextConfigLocation);
        try {
            contextConfig.load(is);
            System.out.println(contextConfig.getProperty("加载路径："+"contextConfigLocation"));
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(null != is){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //类名首字母小写
    private String lowerFirstCase(String name){
        char[] chars = name.toCharArray();
        //chars[0] += 32;
        return String.valueOf(chars);

    }
}
