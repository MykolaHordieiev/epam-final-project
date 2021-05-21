package org.test.project.infra.config;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;
import org.test.project.infra.auth.AuthorizationFilter;
import org.test.project.infra.web.FileDownloadServlet;
import org.test.project.infra.web.FrontServlet;
import org.test.project.infra.web.ServerStarter;

@RequiredArgsConstructor
public class ServerStarterConfig {

    private final static String MATCH_ALL_SUFFIX = "/*";

    @SneakyThrows
    public ServerStarter configureServer(FrontServlet frontServlet, FileDownloadServlet fileDownloadServlet) {
        ServerStarter serverStarter = new ServerStarter();

        serverStarter.addServlet(frontServlet.getName(), frontServlet.getPath() + MATCH_ALL_SUFFIX, frontServlet);
        serverStarter.addServlet(fileDownloadServlet.getName(),
                fileDownloadServlet.getPath() + MATCH_ALL_SUFFIX, fileDownloadServlet);

        configureSecurity(serverStarter);
        return serverStarter;
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