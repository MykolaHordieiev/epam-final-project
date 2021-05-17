package org.test.project.rate;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.test.project.entity.Rate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class RateRepository {

    private final DataSource dataSource;

    @SneakyThrows
    public List<Rate> getRatesByProduct(Long productId) {
        String query = "SELECT * FROM rate WHERE product_id=" + productId;
        List<Rate> rates = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String name = resultSet.getString("name_rate");
                double prise = resultSet.getDouble("price");
                rates.add(new Rate(id, name, prise, productId));
            }
        }
        return rates;
    }

    @SneakyThrows
    public Optional<Rate> getRateById(long id) {
        String query = "SELECT * FROM rate WHERE id=" + id;
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                long productId = resultSet.getLong("product_id");
                String name = resultSet.getString("name_rate");
                double prise = resultSet.getDouble("price");
                return Optional.of(new Rate(id, name, prise, productId));
            }
        }
        return Optional.empty();
    }
}
