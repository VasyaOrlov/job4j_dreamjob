package ru.job4j.dreamjob.model;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Objects;

public class Candidate {
    private int id;
    private String name;
    private String created;
    private String description;

    public Candidate(int id, String name, GregorianCalendar created, String description) {
        this.id = id;
        this.name = name;
        this.created = new SimpleDateFormat("yyyy-MM-dd  HH:mm").format(created.getTime());
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Candidate candidate = (Candidate) o;
        return id == candidate.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
