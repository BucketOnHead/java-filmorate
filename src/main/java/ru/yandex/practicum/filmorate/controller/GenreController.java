package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.service.genre.GenreService;

import java.util.Collection;

import static ru.yandex.practicum.filmorate.service.Service.DEPENDENCY_MESSAGE;

@Slf4j
@RestController
@RequestMapping("/genres")
public class GenreController {
    private final GenreService genreService;

    @Autowired
    public GenreController(GenreService genreService) {
        log.debug("GenreController({}).", genreService.getClass().getSimpleName());
        this.genreService = genreService;
        log.info(DEPENDENCY_MESSAGE, genreService.getClass().getName());
    }

    @GetMapping
    public Collection<Genre> getGenres() {
        return genreService.getAll();
    }

    @GetMapping("/{id}")
    public Genre getGenre(@PathVariable int id) {
        return genreService.get(id);
    }
}
