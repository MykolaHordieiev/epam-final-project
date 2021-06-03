package org.test.project.infra.web;

import lombok.SneakyThrows;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;

import javax.servlet.Servlet;
import javax.servlet.http.HttpSessionListener;
import java.io.File;
import java.util.List;

public class ServerStarter {

    private static Logger logger = LogManager.getLogger(ServerStarter.class);

    private final Tomcat tomcat;
    private final Context context;
    private static final String CONTEXT_PATH = "/telecom";


    @SneakyThrows
    public ServerStarter() {
        this.tomcat = new Tomcat();

        tomcat.setPort(8080);
        context = tomcat.addWebapp(CONTEXT_PATH, new File("webapp").getAbsolutePath());
        logger.info("Set context path --> " + CONTEXT_PATH);
    }

    public void addServlet(String servletName, String url, Servlet servlet) {
        tomcat.addServlet(CONTEXT_PATH, servletName, servlet);
        logger.info("Add servlet: servlet name --> " + servletName);

        context.addServletMappingDecoded(url, servletName);
        logger.info("Add servlet mapping: url --> " + url);
    }

    public void addFilter(FilterDef filterDef, FilterMap filterMap) {
        context.addFilterDef(filterDef);
        logger.info("Add filter --> " + filterDef.getFilterName());

        context.addFilterMap(filterMap);
    }

    public void addSessionListeners(List<HttpSessionListener> sessionListeners) {
        context.setApplicationLifecycleListeners(sessionListeners.toArray());
        logger.info("Set session listeners ");
    }

    @SneakyThrows
    public void startServer() {
        tomcat.start();
        tomcat.getServer().await();
    }
}
