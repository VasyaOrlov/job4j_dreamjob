package ru.job4j.dreamjob.store;

import net.jcip.annotations.ThreadSafe;
import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.City;
import ru.job4j.dreamjob.model.Post;
import ru.job4j.dreamjob.service.CityService;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
@ThreadSafe
public class PostDBStore {

    private static final String FIND_ALL = "SELECT * FROM post";
    private static final String ADD = "INSERT INTO post(name, description, created, "
            + "visible, city_id) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE post SET name = ?, description = ?, "
            + "created = ?, visible = ?, city_id = ?  where id = ?";
    private static final String FIND_BY_ID = "SELECT * FROM post WHERE id = ?";
    private static final Logger LOG = LoggerFactory.getLogger(PostDBStore.class.getName());
    private final BasicDataSource pool;

    public PostDBStore(BasicDataSource pool) {
        this.pool = pool;
    }

    public List<Post> findAll() {
        List<Post> posts = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(FIND_ALL)
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    posts.add(createPost(it));
                }
            }
        } catch (SQLException e) {
            LOG.error("Failed connection when find all:", e);
        }
        return posts;
    }

    public Post add(Post post) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(ADD, PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            setStatement(ps, post);
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    post.setId(id.getInt(1));
                }
            }
        } catch (SQLException e) {
            LOG.error("Failed connection when add:", e);
        }
        return post;
    }

    public boolean update(Post post) {
        boolean rsl = false;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(UPDATE)) {
            setStatement(ps, post);
            ps.setInt(6, post.getId());
            rsl = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOG.error("Failed connection when update:", e);
        }
        return rsl;
    }

    public Post findById(int id) {
        Post post = new Post();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(FIND_BY_ID)
        ) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    return createPost(it);
                }
            }
        } catch (SQLException e) {
            LOG.error("Failed connection when looking for id:", e);
        }
        return post;
    }

    private Post createPost(ResultSet resultSet) throws SQLException {
        Post post = new Post(resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getBoolean("visible"),
                new City(resultSet.getInt("city_id"), ""));
        post.setCreated(resultSet.getTimestamp("created").toLocalDateTime());
        return post;
    }

    private void setStatement(PreparedStatement ps, Post post) throws SQLException {
        ps.setString(1, post.getName());
        ps.setString(2, post.getDescription());
        ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
        ps.setBoolean(4, post.isVisible());
        ps.setInt(5, post.getCity().getId());
    }
}