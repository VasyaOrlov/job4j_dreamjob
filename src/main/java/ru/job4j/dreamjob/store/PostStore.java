package ru.job4j.dreamjob.store;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Post;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
@ThreadSafe
public class PostStore {

    private final Map<Integer, Post> posts = new ConcurrentHashMap<>();

    private final AtomicInteger size = new AtomicInteger(3);

    private PostStore() {
        posts.put(1, new Post(1, "Junior Java Job", "little interesting"));
        posts.put(2, new Post(2, "Middle Java Job", "interesting"));
        posts.put(3, new Post(3, "Senior Java Job", "very interesting"));
    }

    public void add(Post post) {
        int id = size.incrementAndGet();
        post.setId(id);
        posts.putIfAbsent(post.getId(), post);
    }

    public Collection<Post> findAll() {
        return posts.values();
    }

    public Post findById(int id) {
        return posts.get(id);
    }

    public boolean update(Post post) {
        return posts.replace(post.getId(), posts.get(post.getId()), post);
    }
}