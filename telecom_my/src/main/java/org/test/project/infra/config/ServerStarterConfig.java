package org.test.project.infra.config;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;
import org.test.project.infra.auth.AuthorizationFilter;
import org.test.project.infra.auth.EncodingFilter;
import org.test.project.infra.web.FrontServlet;
import org.test.project.infra.web.ServerStarter;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import java.lang.reflect.InvocationTargetException;

@RequiredArgsConstructor
public class ServerStarterConfig {

    private final static String MATCH_ALL_SUFFIX = "/*";

    @SneakyThrows
    public ServerStarter configureServer(FrontServlet frontServlet) {
        ServerStarter serverStarter = new ServerStarter();

        serverStarter.addServlet(frontServlet.getName(), frontServlet.getPath() + MATCH_ALL_SUFFIX, frontServlet);

        configureSecurity(serverStarter);
        configureEncodingFilter(serverStarter);
        return serverStarter;
    }

    private void configureEncodingFilter(ServerStarter serverStarter) throws IllegalAccessException, ServletException, InstantiationException, NoSuchMethodException, NamingException, InvocationTargetException, ClassNotFoundException {
        FilterDef filterDef = new FilterDef();
        filterDef.setFilterName(EncodingFilter.class.getSimpleName());
        filterDef.setFilterClass(EncodingFilter.class.getName());

        FilterMap filterMap = new FilterMap();
        filterMap.setFilterName(EncodingFilter.class.getSimpleName());
        filterMap.addURLPattern("/*");

        serverStarter.addFilter(filterDef,filterMap);
    }

    private void configureSecurity(ServerStarter serverStarter) {
        FilterDef filterDef = new FilterDef();
        filterDef.setFilterName(AuthorizationFilter.class.getSimpleName());
        filterDef.setFilterClass(AuthorizationFilter.class.getName());

        FilterMap filterMap = new FilterMap();
        filterMap.setFilterName(AuthorizationFilter.class.getSimpleName());
        filterMap.addURLPattern("/*");

        serverStarter.addFilter(filterDef, filterMap);
    }
}