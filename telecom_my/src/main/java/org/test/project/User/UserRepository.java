package org.test.project.User;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.test.project.operator.Operator;
import org.test.project.subscriber.Subscriber;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Locale;
import java.util.Optional;

@RequiredArgsConstructor
public class UserRepository {

    private final DataSource dataSource;

    @SneakyThrows
    public Optional<User> getUserByLogin(String login) {
        String query = "SELECT * FROM user WHERE login=?";
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
                user.setLocale(new Locale(resultSet.getString("locale")));
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    @SneakyThrows
    public User changeLocaleForUser(User user, String selectedLocale) {
        String query = "UPDATE user SET locale='" + selectedLocale + "' WHERE id=" + user.getId();
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(query);
            user.setLocale(new Locale(selectedLocale));
        }
        return user;
    }

//    public User checkUserLocale(String selectedLocale, User user) {
//        String query = "SELECT locale FROM user WHERE id=" + user.getId();
//        try (Connection connection = dataSource.getConnection();
//             Statement statement = connection.createStatement();
//             ResultSet resultSet = statement.executeQuery(query)) {
//            if (resultSet.next()) {
//                user.setLocale(new Locale(selectedLocale));
//            }
//
//        }
//
//    }
}
