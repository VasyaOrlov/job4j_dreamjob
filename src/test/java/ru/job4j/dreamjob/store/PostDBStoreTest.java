package ru.job4j.dreamjob.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.job4j.dreamjob.config.JdbcConfiguration;
import ru.job4j.dreamjob.model.City;
import ru.job4j.dreamjob.model.Post;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import static org.assertj.core.api.Assertions.*;

class PostDBStoreTest {

    @AfterEach
    public void wipeTable() throws SQLException {
        BasicDataSource pool = new JdbcConfiguration().loadPool();
        try (Connection connection = pool.getConnection();
             PreparedStatement statement = connection.prepareStatement("delete from post")) {
            statement.execute();
        }
    }

    @Test
     public void findAll() {
        PostDBStore store = new PostDBStore(new JdbcConfiguration().loadPool());
        Post post1 = new Post(1, "Junior Java Job", "little interesting", true,
                new City());
        Post post2 = new Post(2, "Middle Java Job", "interesting", true,
                new City());
        store.add(post1);
        store.add(post2);
        Collection<Post> posts = new ArrayList<>();
        posts.add(post1);
        posts.add(post2);
        assertThat(posts).isEqualTo(store.findAll());
    }

    @Test
    public void whenCreatePost() {
        PostDBStore store = new PostDBStore(new JdbcConfiguration().loadPool());
        Post post = new Post(1, "Junior Java Job", "little interesting", true, new City());
        store.add(post);
        Post postInDb = store.findById(post.getId());
        assertThat(postInDb.getName()).isEqualTo(post.getName());
    }

    @Test
    public void update() {
        PostDBStore store = new PostDBStore(new JdbcConfiguration().loadPool());
        Post post1 = new Post(1, "Junior Java Job", "little interesting", true,
                new City());
        store.add(post1);
        int id = post1.getId();
        Post post2 = new Post(id, "Junior Java Job2", "little interesting!", true,
                new City());
        store.update(post2);
        assertThat(post2.getName()).isEqualTo(store.findById(id).getName());
    }

    @Test
    public void findById() {
        PostDBStore store = new PostDBStore(new JdbcConfiguration().loadPool());
        Post post1 = new Post(1, "Junior Java Job", "little interesting", true,
                new City());
        Post post2 = new Post(2, "Middle Java Job", "interesting", true,
                new City());
        store.add(post1);
        store.add(post2);
        assertThat(post1).isEqualTo(store.findById(post1.getId()));
        assertThat(post2).isEqualTo(store.findById(post2.getId()));
    }
}