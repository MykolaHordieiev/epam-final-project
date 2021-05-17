package org.test.project.infra.config;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.test.project.infra.web.FrontServlet;
import org.test.project.infra.web.ServerStarter;

@RequiredArgsConstructor
public class ServerStarterConfig {

    private final static String MATCH_ALL_SUFFIX = "/*";

    @SneakyThrows
    public ServerStarter configureServer(FrontServlet frontServlet) {
        ServerStarter serverStarter = new ServerStarter();
        serverStarter.addServlet(frontServlet.getName(), frontServlet.getPath() + MATCH_ALL_SUFFIX, frontServlet);
        return serverStarter;
    }
}