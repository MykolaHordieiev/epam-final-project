package org.test.project.infra.config;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;
import org.test.project.infra.auth.AuthorizationFilter;
import org.test.project.infra.encoding.EncodingFilter;
import org.test.project.infra.web.FrontServlet;
import org.test.project.infra.web.LocaleSessionListener;
import org.test.project.infra.web.ServerStarter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
public class ServerStarterConfig {

    private static Logger logger = LogManager.getLogger(ServerStarterConfig.class);

    private final static String MATCH_ALL_SUFFIX = "/*";

    @SneakyThrows
    public ServerStarter configureServer(FrontServlet frontServlet) {
        logger.info("Start configure server");
        ServerStarter serverStarter = new ServerStarter();

        serverStarter.addServlet(frontServlet.getName(), frontServlet.getPath() + MATCH_ALL_SUFFIX, frontServlet);

        configureSecurity(serverStarter);
        configureEncodingFilter(serverStarter);
        configureSessionListener(serverStarter);
        return serverStarter;
    }

    private void configureSessionListener(ServerStarter serverStarter) {
        logger.info("Start configure session listener");

        List<Locale> locales = new ArrayList<>();
        Locale selectedLocale = new Locale("en");
        locales.add(new Locale("en"));
        locales.add(new Locale("ru"));

        serverStarter.addSessionListeners(Arrays.asList(new LocaleSessionListener(locales, selectedLocale)));
    }

    private void configureEncodingFilter(ServerStarter serverStarter) {
        logger.info("Start configure encoding filter");

        FilterDef filterDef = new FilterDef();
        filterDef.setFilterName(EncodingFilter.class.getSimpleName());
        filterDef.setFilterClass(EncodingFilter.class.getName());

        FilterMap filterMap = new FilterMap();
        filterMap.setFilterName(EncodingFilter.class.getSimpleName());
        filterMap.addURLPattern("/*");

        serverStarter.addFilter(filterDef, filterMap);
    }

    private void configureSecurity(ServerStarter serverStarter) {
        logger.info("Start configure security");

        FilterDef filterDef = new FilterDef();
        filterDef.setFilterName(AuthorizationFilter.class.getSimpleName());
        filterDef.setFilterClass(AuthorizationFilter.class.getName());

        FilterMap filterMap = new FilterMap();
        filterMap.setFilterName(AuthorizationFilter.class.getSimpleName());
        filterMap.addURLPattern("/*");

        serverStarter.addFilter(filterDef, filterMap);
    }
}