package ru.job4j.dreamjob.service;

import ru.job4j.dreamjob.model.Post;
import ru.job4j.dreamjob.store.PostStore;

import java.util.Collection;

public class PostService {

    private final static PostService INST = new PostService();
    private final PostStore postStore = PostStore.instOf();
    public static PostService instOf() {
        return INST;
    }

    public Collection<Post> findAll() {
        return postStore.findAll();
    }

    public void add(Post post) {
        postStore.add(post);
    }

    public Post findById(int id) {
        return postStore.findById(id);
    }

    public boolean update(Post post) {
        return postStore.update(post);
    }
}
