package com.example.tweet.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TweetWithFrequency {
    private String tweet;
    private int frequency;
}
