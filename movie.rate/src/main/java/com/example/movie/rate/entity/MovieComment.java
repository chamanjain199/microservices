package com.example.movie.rate.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;

@Entity
public class MovieComment {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "movie_comment_seq_gen")
	@SequenceGenerator(initialValue = 1, sequenceName = "movie_comment_seq", name = "movie_comment_seq_gen")
	private Long id;

	private String comment;

	private long movieId;

	private String commnendBy;

	private LocalDateTime commentDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public long getMovieId() {
		return movieId;
	}

	public void setMovieId(long movieId) {
		this.movieId = movieId;
	}

	public String getCommnendBy() {
		return commnendBy;
	}

	public void setCommnendBy(String commnendBy) {
		this.commnendBy = commnendBy;
	}

	public LocalDateTime getCommentDate() {
		return commentDate;
	}

	public void setCommentDate(LocalDateTime commentDate) {
		this.commentDate = commentDate;
	}

}
