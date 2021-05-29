package org.test.project.rate;

import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RateRepositoryTest {

    @Mock
    private DataSource dataSource;
    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement preparedStatement;
    @Mock
    private Statement statement;
    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private RateRepository repository;

    @SneakyThrows
    @Before
    public void setDataSource() {
        when(dataSource.getConnection()).thenReturn(connection);
    }

    @Test
    public void getRatesByProduct() {


    }

    @Test
    public void getRateById() {
    }

    @Test
    public void changeRateById() {
    }

    @Test
    public void addRateByProductId() {
    }

    @Test
    public void deleteRateById() {
    }

    @Test
    public void checkUsingRateBySubscribers() {
    }

    @Test
    public void doUnusableRateByRateId() {
    }
}