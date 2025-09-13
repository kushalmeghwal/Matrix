package com.kushal.matrix.service;

import com.kushal.matrix.dtos.MovieRequest;
import com.kushal.matrix.dtos.MovieResponse;
import com.kushal.matrix.model.Movie;
import com.kushal.matrix.model.User;
import com.kushal.matrix.repository.MovieRepository;
import com.kushal.matrix.repository.UserRepository;
import com.kushal.matrix.security.CustomUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieService {
    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository, UserRepository userRepository) {
        this.movieRepository = movieRepository;
    }

    public MovieResponse addMovie(MovieRequest request) {
        CustomUserDetails userDetails =
                (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User producer = userDetails.getUser();

        Movie movie= Movie.builder()
                .title(request.getTitle())
                .duration(request.getDuration())
                .releaseDate(request.getReleaseDate())
                .producer(producer)
                .build();

        Movie saved=movieRepository.save(movie);

        return new MovieResponse(
                saved.getId(),
                saved.getTitle(),
                saved.getDuration(),
                saved.getReleaseDate(),
                saved.getProducer().getName()
        );
    }

    public List<MovieResponse> getAllMovies() {
        return movieRepository.findAll().stream()
                .map(movie->new MovieResponse(
                        movie.getId(),
                        movie.getTitle(),
                        movie.getDuration(),
                        movie.getReleaseDate(),
                        movie.getProducer().getName()
                ))
                .collect(Collectors.toList());
    }

    public MovieResponse getMovieById(Long id) {
        Movie movie = movieRepository.findById(id).orElseThrow(()->new RuntimeException("movie id not found"));

        return new MovieResponse(
                movie.getId(),
                movie.getTitle(),
                movie.getDuration(),
                movie.getReleaseDate(),
                movie.getProducer().getName()
        );
    }
}
