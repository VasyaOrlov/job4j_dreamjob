package ru.job4j.dreamjob.store;

import ru.job4j.dreamjob.model.Candidate;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class CandidateStore {

    private static final CandidateStore INST = new CandidateStore();

    private final Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();

    private final AtomicInteger size = new AtomicInteger(3);

    private CandidateStore() {
        candidates.put(1, new Candidate(1, "Dima", LocalDateTime.now(), "junior"));
        candidates.put(2, new Candidate(2, "Sasha", LocalDateTime.now(), "middle"));
        candidates.put(3, new Candidate(3, "Dasha", LocalDateTime.now(), "senior"));
    }

    public static CandidateStore instOf() {
        return INST;
    }

    public Collection<Candidate> findAll() {
        return candidates.values();
    }

    public Candidate findById(int id) {
        return candidates.get(id);
    }

    public void update(Candidate candidate) {
        candidates.replace(candidate.getId(), candidates.get(candidate.getId()), candidate);
    }

    public void add(Candidate candidate) {
        int id = size.incrementAndGet();
        candidate.setId(id);
        candidate.setCreated(LocalDateTime.now());
        candidates.putIfAbsent(candidate.getId(), candidate);
    }
}
