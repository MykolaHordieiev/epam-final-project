package org.test.project.infra.web;

import lombok.SneakyThrows;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;

import javax.servlet.Servlet;
import javax.servlet.http.HttpSessionListener;
import java.io.File;
import java.util.List;

public class ServerStarter {

    private final Tomcat tomcat;
    private final Context context;


    @SneakyThrows
    public ServerStarter() {
        this.tomcat = new Tomcat();

        tomcat.setPort(8080);
        context = tomcat.addWebapp("/telecom", new File("webapp").getAbsolutePath());
    }

    public void addServlet(String servletName, String url, Servlet servlet) {
        tomcat.addServlet("/telecom", servletName, servlet);
        context.addServletMappingDecoded(url, servletName);
    }

    public void addFilter(FilterDef filterDef, FilterMap filterMap) {
        context.addFilterDef(filterDef);
        context.addFilterMap(filterMap);
    }

    public void addSessionListeners(List<HttpSessionListener> sessionListeners) {
        context.setApplicationLifecycleListeners(sessionListeners.toArray());
    }

    @SneakyThrows
    public void startServer() {
        tomcat.start();
        tomcat.getServer().await();
    }
}
