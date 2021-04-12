package com.example.tweet.service;

import com.example.tweet.model.CreateTweetRequest;
import org.springframework.stereotype.Service;

import java.util.List;

public interface TweetService {

    void createTweet(CreateTweetRequest tweetRequest);

    List<String> getTrendingTweets();
}
