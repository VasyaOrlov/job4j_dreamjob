package ru.job4j.dreamjob.controller;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;
import org.springframework.ui.Model;
import ru.job4j.dreamjob.model.City;
import ru.job4j.dreamjob.model.Post;
import ru.job4j.dreamjob.service.CityService;
import ru.job4j.dreamjob.service.PostService;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PostControllerTest {

    @Test
    public void whenPosts() {
        List<Post> posts = Arrays.asList(
                new Post(1, "Junior Java Job", "little interesting", true,
                        new City(1, "Москва")),
                new Post(2, "Middle Java Job", "interesting", true,
                        new City(2, "Омск"))
        );
        Model model = mock(Model.class);
        HttpSession session = mock(HttpSession.class);
        PostService postService = mock(PostService.class);
        CityService cityService = mock(CityService.class);
        PostController postController = new PostController(
                postService,
                cityService
        );
        when(postService.findAll()).thenReturn(posts);
        String page = postController.posts(model, session);
        verify(model).addAttribute("posts", posts);
        assertThat(page).isEqualTo("posts");
    }

    @Test
    public void whenAddPost() {
        List<City> cities = Arrays.asList(new City(1, "Москва"), new City(2, "Омск"));
        HttpSession httpSession = mock(HttpSession.class);
        Model model = mock(Model.class);
        PostService postService = mock(PostService.class);
        CityService cityService = mock(CityService.class);
        when(cityService.getAllCities()).thenReturn(cities);
        PostController postController = new PostController(
                postService,
                cityService
        );
        Post post = new Post(0, "Заполните название",
                "Заполните описание", true, new City());
        String page = postController.addPost(model, httpSession);
        verify(model).addAttribute("post", post);
        verify(model).addAttribute("cities", cities);
        assertThat(page).isEqualTo("addPost");
    }

    @Test
    public void whenCreatePost() {
        Post input = new Post(1, "Junior Java Job", "little interesting", true,
                new City(1, "Москва"));
        PostService postService = mock(PostService.class);
        CityService cityService = mock(CityService.class);
        PostController postController = new PostController(
                postService,
                cityService
        );
        String page = postController.createPost(input);
        verify(postService).add(input);
        assertThat(page).isEqualTo("redirect:/posts");
    }

    @Test
    public void whenFormUpdatePost() {
        List<Post> posts = Arrays.asList(
                new Post(1, "Junior Java Job", "little interesting", true,
                        new City(1, "Москва")),
                new Post(2, "Middle Java Job", "interesting", true,
                        new City(2, "Омск"))
        );
        List<City> cities = Arrays.asList(new City(1, "Spb"), new City(2, "Msc"));
        HttpSession httpSession = mock(HttpSession.class);
        Model model = mock(Model.class);
        PostService postService = mock(PostService.class);
        int id = posts.get(1).getId();
        when(postService.findById(id)).thenReturn(posts.get(1));
        CityService cityService = mock(CityService.class);
        when(cityService.getAllCities()).thenReturn(cities);
        PostController postController = new PostController(
                postService,
                cityService
        );
        String page = postController.formUpdatePost(model, id, httpSession);
        verify(model).addAttribute("post", posts.get(1));
        verify(model).addAttribute("cities", cities);
        assertThat(page).isEqualTo("updatePost");
    }

    @Test
    public void whenUpdatePost() {
        Post input = new Post(1, "Junior Java Job", "little interesting", true,
                new City(1, "Москва"));
        PostService postService = mock(PostService.class);
        CityService cityService = mock(CityService.class);
        PostController postController = new PostController(
                postService,
                cityService
        );
        String page = postController.updatePost(input);
        verify(postService).update(input);
        assertThat(page).isEqualTo("redirect:/posts");
    }
}