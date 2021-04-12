package com.example.tweet.resource;

import com.example.tweet.model.CreateTweetRequest;
import com.example.tweet.service.TweetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
public class TweetResource {

    private TweetService tweetService;

    @Autowired
    TweetResource(TweetService tweetService) {
        this.tweetService = tweetService;
    }

    @PostMapping("/tweet")
    public ResponseEntity createTweet(@RequestBody @Valid @NotNull
                                              CreateTweetRequest createTweetRequest) {
        tweetService.createTweet(createTweetRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("trending-hashtags")
    public ResponseEntity<List<String>> getTrendingTweets() {
        return ResponseEntity.ok(tweetService.getTrendingTweets());
    }
}
