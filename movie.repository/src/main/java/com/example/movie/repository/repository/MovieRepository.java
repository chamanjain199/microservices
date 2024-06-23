package com.example.movie.repository.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.movie.repository.entity.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
}