package org.test.project;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.test.project.rate.RateValidator;
import org.test.project.user.*;
import org.test.project.infra.config.*;
import org.test.project.infra.db.LiquibaseStarter;
import org.test.project.infra.web.*;
import org.test.project.product.ProductController;
import org.test.project.product.ProductRepository;
import org.test.project.product.ProductService;
import org.test.project.rate.RateController;
import org.test.project.rate.RateRepository;
import org.test.project.rate.RateService;
import org.test.project.subscriber.*;
import org.test.project.subscribing.SubscribingController;
import org.test.project.subscribing.SubscribingRepository;
import org.test.project.subscribing.SubscribingService;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Application {

    public static void main(String[] args) {

        //config
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        ServerStarterConfig serverStarterConfig = new ServerStarterConfig();
        ExceptionHandlerConfig exceptionHandlerConfig = new ExceptionHandlerConfig();

        //database
        DataSource dataSource = dataSourceConfig.configureDataSource();
        LiquibaseStarter liquibaseStarter = new LiquibaseStarter(dataSource);
        liquibaseStarter.updateDatabase();

        //application
        QueryValueResolver queryValueResolver = new QueryValueResolver(new ObjectMapper());
        UserService userService = getUserService(dataSource);
        SubscriberService subscriberService = getSubscriberService(dataSource);
        ProductService productService = getProductService(dataSource);
        RateService rateService = getRateService(dataSource);
        SubscribingService subscribingService = getSubscribingService(dataSource);

        List<Controller> controllers = getControllersList(userService, subscriberService, productService, rateService,
                subscribingService, queryValueResolver);

        //web
        ExceptionHandler exceptionHandler = exceptionHandlerConfig.configureExceptionHandler();
        FrontServlet frontServlet = new FrontServlet(controllers, exceptionHandler, "front", "/service");
        ServerStarter serverStarter = serverStarterConfig.configureServer(frontServlet);
        serverStarter.startServer();
    }

    private static List<Controller> getControllersList(UserService userService,
                                                       SubscriberService subscriberService, ProductService productService,
                                                       RateService rateService, SubscribingService subscribingService,
                                                       QueryValueResolver queryValueResolver) {
        SubscriberController subscriberController = getSubscriberController(subscriberService, subscribingService, queryValueResolver);
        UserController userController = getUserController(userService, queryValueResolver);
        RateController rateController = getRateController(rateService, subscriberService, productService, queryValueResolver);
        ProductController productController = new ProductController(productService);
        SubscribingController subscribingController = new SubscribingController(subscribingService, subscriberService,
                rateService);

        List<Controller> controllers = new ArrayList<>();
        controllers.add(subscriberController);
        controllers.add(userController);
        controllers.add(rateController);
        controllers.add(productController);
        controllers.add(subscribingController);
        return controllers;
    }

    private static RateController getRateController(RateService rateService,
                                                    SubscriberService subscriberService,
                                                    ProductService productService, QueryValueResolver queryValueResolver) {
        RateValidator rateValidator = new RateValidator();
        return new RateController(rateService, subscriberService, productService, rateValidator, queryValueResolver);
    }

    private static UserController getUserController(UserService userService, QueryValueResolver queryValueResolver) {
        Map<UserRole, String> viewMap = new HashMap<>();
        viewMap.put(UserRole.OPERATOR, "/operator/home.jsp");
        viewMap.put(UserRole.SUBSCRIBER, "/subscriber/home.jsp");
        UserValidator userValidator = new UserValidator();
        return new UserController(userService, viewMap, userValidator, queryValueResolver);
    }

    private static SubscriberController getSubscriberController(SubscriberService subscriberService,
                                                                SubscribingService subscribingService,
                                                                QueryValueResolver queryValueResolver) {
        SubscriberValidator subscriberValidator = new SubscriberValidator();
        return new SubscriberController(subscriberService, subscribingService, subscriberValidator, queryValueResolver);
    }

    private static SubscriberService getSubscriberService(DataSource dataSource) {
        SubscriberRepository subscriberRepository = new SubscriberRepository(dataSource);
        SubscriberMapper subscriberMapper = new SubscriberMapper();
        return new SubscriberService(subscriberRepository, subscriberMapper);
    }

    private static UserService getUserService(DataSource dataSource) {
        UserRepository userRepository = new UserRepository(dataSource);
        return new UserService(userRepository);
    }

    private static RateService getRateService(DataSource dataSource) {
        RateRepository rateRepository = new RateRepository(dataSource);
        return new RateService(rateRepository);
    }

    private static ProductService getProductService(DataSource dataSource) {
        ProductRepository productRepository = new ProductRepository(dataSource);
        return new ProductService(productRepository);
    }

    private static SubscribingService getSubscribingService(DataSource dataSource) {
        SubscribingRepository subscribingRepository = new SubscribingRepository(dataSource);
        return new SubscribingService(subscribingRepository);
    }
}