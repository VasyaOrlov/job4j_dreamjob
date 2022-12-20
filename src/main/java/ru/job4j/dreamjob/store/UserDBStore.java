package ru.job4j.dreamjob.store;

import net.jcip.annotations.ThreadSafe;
import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
@ThreadSafe
public class UserDBStore {

    private static final Logger LOG = LoggerFactory.getLogger(UserDBStore.class);
    private static final String ADD_USER = "INSERT INTO users (email, password, name) VALUES (?, ?, ?)";
    private static final String FIND_BY_ID = "SELECT * FROM users WHERE id = ?";
    private static final String FIND_BY_EMAIL_PASSWORD = "SELECT * FROM users WHERE email = ? and password = ?";

    private final BasicDataSource pool;

    public UserDBStore(BasicDataSource pool) {
        this.pool = pool;
    }

    public Optional<User> add(User user) {
        Optional<User> rsl = Optional.empty();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(ADD_USER, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getName());
            ps.execute();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    user.setId(rs.getInt(1));
                    rsl = Optional.of(user);
                }
            }
        } catch (SQLException e) {
            LOG.error("Failed connection when add:", e);
        }
        return rsl;
    }

    public Optional<User> findById(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(FIND_BY_ID)
        ) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    return Optional.of(new User(
                            it.getInt("id"),
                            it.getString("email"),
                            it.getString("password"),
                            it.getString("name")
                    ));
                }
            }
        } catch (SQLException e) {
            LOG.error("Failed connection when looking for id:", e);
        }
        return Optional.empty();
    }

    public Optional<User> findUserByEmailAndPassword(String email, String password) {
        try (Connection cn = pool.getConnection();
        PreparedStatement ps = cn.prepareStatement(FIND_BY_EMAIL_PASSWORD)) {
            ps.setString(1, email);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new User(
                            rs.getInt("id"),
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getString("name")
                    ));
                }
            }
        } catch (SQLException e) {
            LOG.error("Failed connection when find:", e);
        }
        return Optional.empty();
    }
}
