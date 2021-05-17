package org.test.project.User;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.test.project.operator.Operator;
import org.test.project.subscriber.Subscriber;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@RequiredArgsConstructor
public class UserRepository {

    private final DataSource dataSource;

    @SneakyThrows
    public Optional<User> getUserByLogin(String login) {
        String query = "SELECT id,password,role FROM user WHERE login=?";
        ResultSet resultSet;
        User user;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, login);
            resultSet = ps.executeQuery();
            if (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String password = resultSet.getString("password");
                String role = resultSet.getString("role");
                if (role.equals("OPERATOR")) {
                    user = new Operator();
                    user.setUserRole(UserRole.OPERATOR);
                } else {
                    user = new Subscriber();
                    user.setUserRole(UserRole.SUBSCRIBER);
                }
                user.setId(id);
                user.setLogin(login);
                user.setPassword(password);
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }
}