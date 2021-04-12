package com.example.tweet.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CreateTweetRequest {

    @NotBlank
    private String tweet;
}
