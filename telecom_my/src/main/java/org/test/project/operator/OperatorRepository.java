package org.test.project.operator;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.test.project.product.Product;
import org.test.project.rate.Rate;
import org.test.project.subscriber.FiledTransactionException;
import org.test.project.subscriber.Subscriber;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

@RequiredArgsConstructor
public class OperatorRepository {

    private final DataSource dataSource;

}

