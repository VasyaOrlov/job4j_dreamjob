package ru.job4j.dreamjob.store;


import net.jcip.annotations.ThreadSafe;
import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Candidate;
import ru.job4j.dreamjob.model.City;
import ru.job4j.dreamjob.service.CityService;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Repository
@ThreadSafe
public class CandidateDBStore {

    private static final String FIND_ALL = "select * from candidate";
    private static final String ADD = "insert into candidate(name, created, "
            + "description, city_id, photo) values (?, ?, ?, ?, ?)";
    private static final String FIND_BY_ID = "select * from candidate where id = ?";
    private static final String UPDATE = "update candidate set name = ?, "
            + "created = ?, description = ?, city_id = ?, photo = ?";
    private static final Logger LOG = LoggerFactory.getLogger(CandidateDBStore.class.getName());
    private final BasicDataSource pool;

    public CandidateDBStore(BasicDataSource pool) {
        this.pool = pool;
    }

    public Collection<Candidate> findAll() {
        List<Candidate> candidates = new ArrayList<>();
        try (Connection cn = pool.getConnection();
            PreparedStatement ps = cn.prepareStatement(FIND_ALL)) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    candidates.add(createCandidate(it));
                }
            }
        } catch (SQLException e) {
            LOG.error("Failed connection when find all:", e);
        }
        return candidates;
    }

    public void add(Candidate candidate) {
        try (Connection cn = pool.getConnection();
        PreparedStatement ps = cn.prepareStatement(ADD, PreparedStatement.RETURN_GENERATED_KEYS)) {
            setStatement(ps, candidate);
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    candidate.setId(id.getInt(1));
                }
            }
        } catch (SQLException e) {
            LOG.error("Failed connection when add:", e);
        }
    }

    public Candidate findById(int id) {
        Candidate candidate = new Candidate();
        try (Connection cn = pool.getConnection();
        PreparedStatement ps = cn.prepareStatement(FIND_BY_ID)) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    return createCandidate(it);
                }
            }
        } catch (SQLException e) {
            LOG.error("Failed connection when looking for id:", e);
        }
        return candidate;
    }

    public boolean update(Candidate candidate) {
        boolean rsl = false;
        try (Connection cn = pool.getConnection();
        PreparedStatement ps = cn.prepareStatement(UPDATE)) {
            setStatement(ps, candidate);
            rsl = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOG.error("Failed connection when update:", e);
        }
        return rsl;
    }

    private Candidate createCandidate(ResultSet it) throws SQLException {
        Candidate candidate = new Candidate(it.getInt("id"),
                it.getString("name"),
                it.getTimestamp("created").toLocalDateTime(),
                it.getString("description"),
                new City(it.getInt("city_id"), ""));
        candidate.setPhoto(it.getBytes("photo"));
        return candidate;
    }

    private void setStatement(PreparedStatement ps, Candidate candidate) throws SQLException {
        ps.setString(1, candidate.getName());
        ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
        ps.setString(3, candidate.getDescription());
        ps.setInt(4, candidate.getCity().getId());
        ps.setBytes(5, candidate.getPhoto());
    }
}
