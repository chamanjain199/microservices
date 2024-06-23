package com.example.movie.rate.dto;

import java.time.LocalDateTime;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class MovieCommentDTO {

    private Long id;
    @NotBlank(message = "Please enter comment")
    @Length(min = 1,max = 200,message = "Min 1 and max 200 character are allowed")
    private String comment;
    @NotNull(message = "Please provide movie id")
    private Long movieId;
    @NotBlank(message = "Comment by can not be empty")
    private String commnendBy;
    private LocalDateTime commentDate= LocalDateTime.now();
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