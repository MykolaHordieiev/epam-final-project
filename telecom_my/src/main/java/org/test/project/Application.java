package org.test.project;


import org.test.project.user.UserController;
import org.test.project.user.UserRepository;
import org.test.project.user.UserService;
import org.test.project.infra.config.*;
import org.test.project.infra.db.LiquibaseStarter;
import org.test.project.infra.web.*;
import org.test.project.operator.OperatorRepository;
import org.test.project.operator.OperatorService;
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
import java.util.List;

public class Application {

    public static void main(String[] args) {

        //config
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        ServerStarterConfig serverStarterConfig = new ServerStarterConfig();

        //database
        DataSource dataSource = dataSourceConfig.configureDataSource();
        LiquibaseStarter liquibaseStarter = new LiquibaseStarter(dataSource);
        liquibaseStarter.updateDatabase();

        //application
        UserService userService = getUserService(dataSource);
        SubscriberService subscriberService = getSubscriberService(dataSource);
        ProductService productService = getProductService(dataSource);
        RateService rateService = getRateService(dataSource);
        SubscribingService subscribingService = getSubscribingService(dataSource);

        SubscriberController subscriberController = new SubscriberController(subscriberService, subscribingService);
        UserController userController = new UserController(userService, subscriberService);
        RateController rateController = new RateController(rateService, subscriberService, productService);
        ProductController productController = new ProductController(productService);
        SubscribingController subscribingController = new SubscribingController(subscribingService, subscriberService,
                productService, rateService);

        List<Controller> controllers = new ArrayList<>();
        controllers.add(subscriberController);
        controllers.add(userController);
        controllers.add(rateController);
        controllers.add(productController);
        controllers.add(subscribingController);
        //web
        ExceptionHandler exceptionHandler = new ExceptionHandlerImplMy();
        FrontServlet frontServlet = new FrontServlet(controllers, exceptionHandler, "front", "/service");
        ServerStarter serverStarter = serverStarterConfig.configureServer(frontServlet);
        serverStarter.startServer();
    }

    private static SubscriberService getSubscriberService(DataSource dataSource) {
        //Subscriber config
        SubscriberRepository subscriberRepository = new SubscriberRepository(dataSource);
        return new SubscriberService(subscriberRepository);
    }

    private static UserService getUserService(DataSource dataSource) {
        //user config
        UserRepository userRepository = new UserRepository(dataSource);
        return new UserService(userRepository);
    }

    private static RateService getRateService(DataSource dataSource) {
        //Operator config
        RateRepository rateRepository = new RateRepository(dataSource);
        return new RateService(rateRepository);
    }

    private static ProductService getProductService(DataSource dataSource) {
        //Operator config
        ProductRepository productRepository = new ProductRepository(dataSource);
        return new ProductService(productRepository);
    }

    private static SubscribingService getSubscribingService(DataSource dataSource) {
        //Subscriber config
        SubscribingRepository subscribingRepository = new SubscribingRepository(dataSource);
        return new SubscribingService(subscribingRepository);
    }

    private static OperatorService getOperatorService(DataSource dataSource) {
        //Operator config
        OperatorRepository operatorRepository = new OperatorRepository(dataSource);
        return new OperatorService(operatorRepository);
    }
}