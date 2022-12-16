package ru.job4j.dreamjob.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.dreamjob.model.Candidate;
import ru.job4j.dreamjob.store.CandidateDBStore;

import java.util.Collection;

@Service
@ThreadSafe
public class CandidateService {

    private final CandidateDBStore candidateStore;
    private final CityService cityService;

    public CandidateService(CandidateDBStore candidateStore, CityService cityService) {
        this.candidateStore = candidateStore;
        this.cityService = cityService;
    }

    public Collection<Candidate> findAll() {
        return candidateStore.findAll();
    }

    public void add(Candidate candidate) {
        candidateStore.add(candidate);
    }

    public Candidate findById(int id) {
        return candidateStore.findById(id);
    }

    public void update(Candidate candidate) {
        candidateStore.update(candidate);
    }
}
