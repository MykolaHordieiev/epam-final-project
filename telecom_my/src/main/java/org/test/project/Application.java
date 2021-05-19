package org.test.project;


import org.test.project.User.UserController;
import org.test.project.User.UserRepository;
import org.test.project.User.UserService;
import org.test.project.infra.config.*;
import org.test.project.infra.db.LiquibaseStarter;
import org.test.project.infra.web.*;
import org.test.project.operator.OperatorController;
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
        SubscriberController subscriberController = configureSubscriber(dataSource);
        UserController userController = configureUser(dataSource);
        OperatorController operatorController = configureOperator(dataSource);
        RateController rateController = configureRate(dataSource);
        ProductController productController = configureProduct(dataSource);
        SubscribingController subscribingController = configureSubscribing(dataSource);

        //web
        ExceptionHandler exceptionHandler = new ExceptionHandlerImplMy();
        FrontServlet frontServlet = new FrontServlet(subscriberController,userController,operatorController,
                rateController,productController,subscribingController, exceptionHandler, "front", "/service");
        ServerStarter serverStarter = serverStarterConfig.configureServer(frontServlet);
        serverStarter.startServer();
    }

    private static SubscriberController configureSubscriber(DataSource dataSource) {
        //Subscriber config
        SubscriberRepository subscriberRepository = new SubscriberRepository(dataSource);
        SubscriberService subscriberService = new SubscriberService(subscriberRepository,
                new ProductService(new ProductRepository(dataSource)),
                new RateService(new RateRepository(dataSource)));
        return new SubscriberController(subscriberService, new SubscribingService(new SubscribingRepository(dataSource),
                new SubscriberService(new SubscriberRepository(dataSource),
                        new ProductService(new ProductRepository(dataSource)),
                        new RateService(new RateRepository(dataSource))),
                new ProductService(new ProductRepository(dataSource)),
                new RateService(new RateRepository(dataSource))));
    }

    private static UserController configureUser(DataSource dataSource) {
        //User config
        UserRepository userRepository = new UserRepository(dataSource);
        UserService userService = new UserService(userRepository);
        return new UserController(userService,new SubscriberService(new SubscriberRepository(dataSource),
                new ProductService(new ProductRepository(dataSource)),
                new RateService(new RateRepository(dataSource))));
    }
    private static OperatorController configureOperator(DataSource dataSource){
        //Operator config
        OperatorRepository operatorRepository = new OperatorRepository(dataSource);
        OperatorService operatorService = new OperatorService(operatorRepository);
        return new OperatorController(operatorService);
    }
    private static RateController configureRate(DataSource dataSource){
        //Operator config
        RateRepository rateRepository = new RateRepository(dataSource);
        RateService rateService = new RateService(rateRepository);
        return new RateController(rateService, new SubscriberService(new SubscriberRepository(dataSource),
                new ProductService(new ProductRepository(dataSource)),
                new RateService(new RateRepository(dataSource))));
    }

    private static ProductController configureProduct(DataSource dataSource){
        //Operator config
        ProductRepository productRepository = new ProductRepository(dataSource);
        ProductService productService = new ProductService(productRepository);
        return new ProductController(productService);
    }
    private static SubscribingController configureSubscribing(DataSource dataSource) {
        //Subscriber config
        SubscribingRepository subscribingRepository = new SubscribingRepository(dataSource);
        SubscribingService subscribingService = new SubscribingService(subscribingRepository,
                new SubscriberService(new SubscriberRepository(dataSource),
                        new ProductService(new ProductRepository(dataSource)),
                        new RateService(new RateRepository(dataSource))),
                new ProductService(new ProductRepository(dataSource)),
                new RateService(new RateRepository(dataSource)));

        return new SubscribingController(subscribingService,new SubscriberService(new SubscriberRepository(dataSource),
                new ProductService(new ProductRepository(dataSource)),
                new RateService(new RateRepository(dataSource))));
    }
}