package ru.job4j.dreamjob.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.job4j.dreamjob.config.JdbcConfiguration;
import ru.job4j.dreamjob.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.*;

class UserDBStoreTest {

    @AfterEach
    public void wipeTable() throws SQLException {
        BasicDataSource pool = new JdbcConfiguration().loadPool();
        try (Connection connection = pool.getConnection();
             PreparedStatement statement = connection.prepareStatement("delete from users")) {
            statement.execute();
        }
    }

    @Test
    void addAndSuccessEmail() {
        UserDBStore store = new UserDBStore(new JdbcConfiguration().loadPool());
        User user = new User(1, "email", "password");
        store.add(user);
        assertThat(user.getPassword()).isEqualTo(store.findById(user.getId()).orElse(new User()).getPassword());
    }

    @Test
    void addAndFailEmail() {
        UserDBStore store = new UserDBStore(new JdbcConfiguration().loadPool());
        User user = new User(1, "email", "password");
        User user2 = new User(1, "email", "password2");
        store.add(user);
        store.add(user2);
        assertThat(user.getPassword()).isEqualTo(store.findById(user.getId()).orElse(new User()).getPassword());
    }

    @Test
    void findById() {
        UserDBStore store = new UserDBStore(new JdbcConfiguration().loadPool());
        User user = new User(1, "email", "password");
        store.add(user);
        assertThat(user).isEqualTo(store.findById(user.getId()).orElse(new User()));
    }
}