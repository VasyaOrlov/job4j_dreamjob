package ru.job4j.dreamjob.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.job4j.dreamjob.config.JdbcConfiguration;
import ru.job4j.dreamjob.model.Candidate;
import ru.job4j.dreamjob.model.City;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import static org.assertj.core.api.Assertions.*;

class CandidateDBStoreTest {

    @AfterEach
    public void wipeTable() throws SQLException {
        BasicDataSource pool = new JdbcConfiguration().loadPool();
        try (Connection connection = pool.getConnection();
             PreparedStatement statement = connection.prepareStatement("delete from candidate")) {
            statement.execute();
        }
    }

    @Test
    public void whenFindAll() {
        CandidateDBStore store = new CandidateDBStore(new JdbcConfiguration().loadPool());
        Candidate candidate1 = new Candidate(1, "Junior Java Job", LocalDateTime.now(),
                "little interesting", new City());
        Candidate candidate2 = new Candidate(2, "Middle Java Job", LocalDateTime.now(),
                "interesting", new City());
        store.add(candidate1);
        store.add(candidate2);
        Collection<Candidate> candidates = new ArrayList<>();
        candidates.add(candidate1);
        candidates.add(candidate2);
        assertThat(candidates).isEqualTo(store.findAll());
    }

    @Test
    public void whenCreateCandidate() {
        CandidateDBStore store = new CandidateDBStore(new JdbcConfiguration().loadPool());
        Candidate candidate = new Candidate(1, "Junior Java Job", LocalDateTime.now(),
                "little interesting", new City());
        store.add(candidate);
        Candidate candidateInDb = store.findById(candidate.getId());
        assertThat(candidateInDb.getName()).isEqualTo(candidate.getName());
    }

    @Test
    public void whenUpdate() {
        CandidateDBStore store = new CandidateDBStore(new JdbcConfiguration().loadPool());
        Candidate candidate1 = new Candidate(1, "Junior Java Job", LocalDateTime.now(),
                "little interesting", new City());
        store.add(candidate1);
        int id = candidate1.getId();
        Candidate candidate2 = new Candidate(id, "Junior Java Job2", LocalDateTime.now(),
                "little interesting!", new City());
        store.update(candidate2);
        assertThat(candidate2.getName()).isEqualTo(store.findById(id).getName());
    }

    @Test
    public void whenFindById() {
        CandidateDBStore store = new CandidateDBStore(new JdbcConfiguration().loadPool());
        Candidate candidate1 = new Candidate(1, "Junior Java Job", LocalDateTime.now(),
                "little interesting", new City());
        Candidate candidate2 = new Candidate(2, "Middle Java Job", LocalDateTime.now(),
                "interesting", new City());
        store.add(candidate1);
        store.add(candidate2);
        assertThat(candidate1).isEqualTo(store.findById(candidate1.getId()));
        assertThat(candidate2).isEqualTo(store.findById(candidate2.getId()));
    }
}