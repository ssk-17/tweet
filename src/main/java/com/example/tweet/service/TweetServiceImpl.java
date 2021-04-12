package com.example.tweet.service;

import com.example.tweet.model.CreateTweetRequest;
import com.example.tweet.model.TweetWithFrequency;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TweetServiceImpl implements TweetService {

    @Value("${app.trendingTweets:25}")
    private int MAX_HEAP_SIZE;

    private String minFrequencyTweet;
    private int minFrequency = Integer.MAX_VALUE;

    Map<String, Integer> tweetFrequencyMap = new HashMap<>();

    PriorityQueue<TweetWithFrequency> trendingTweets = new PriorityQueue<TweetWithFrequency>(
            new Comparator<TweetWithFrequency>() {
                @Override
                public int compare(TweetWithFrequency tweet1, TweetWithFrequency tweet2) {
                    return tweet2.getFrequency() - tweet1.getFrequency();
                }
            });

    @Override
    public void createTweet(CreateTweetRequest tweetRequest) {
        String unifiedTweet = unifyTweet(tweetRequest.getTweet());
        if (tweetFrequencyMap.containsKey(unifiedTweet))
            tweetFrequencyMap.put(unifiedTweet, tweetFrequencyMap.get(unifiedTweet) + 1);
        else
            tweetFrequencyMap.put(unifiedTweet, 1);

        /* add tweet to priority queue
         *if heap size is not full add tweet to heap
         *if heap is full and tweet has frequency less than minimum frequency in heap ignore it
         *else pop the one which has low frequency and insert this.*/

        int freq = tweetFrequencyMap.get(unifiedTweet);
        TweetWithFrequency tweetToBeAdded = TweetWithFrequency.builder()
                .tweet(unifiedTweet).frequency(freq).build();
        if (trendingTweets.size() < MAX_HEAP_SIZE) {
            TweetWithFrequency tweetWithFrequency = findTweetObjectWithFrequency(unifiedTweet);
            if (tweetWithFrequency != null) //update tweet frequency object
                trendingTweets.remove(tweetWithFrequency);
            trendingTweets.add(tweetToBeAdded);
            if (tweetFrequencyMap.get(unifiedTweet) < minFrequency) {
                minFrequency = tweetFrequencyMap.get(unifiedTweet);
                minFrequencyTweet = unifiedTweet;
            }
        } else {
            if (tweetFrequencyMap.get(unifiedTweet) > minFrequency) {
                TweetWithFrequency tweetWithFrequency = findTweetObjectWithFrequency(unifiedTweet);
                if (tweetWithFrequency == null)
                    trendingTweets.remove(findTweetObjectWithFrequency(minFrequencyTweet));
                trendingTweets.remove(tweetWithFrequency);
                trendingTweets.add(tweetToBeAdded);   //update the priority queue tweet object with updated frequency.

                minFrequency = Integer.MAX_VALUE;
                Object[] tweetArray = trendingTweets.toArray(); // Max size of this array is 25 as max heap size is 25
                for (Object obj : tweetArray) {
                    TweetWithFrequency twt = (TweetWithFrequency) obj;
                    if (tweetFrequencyMap.get(twt.getTweet()) < minFrequency) {
                        minFrequency = tweetFrequencyMap.get(twt.getTweet());
                        minFrequencyTweet = twt.getTweet();
                    }
                }
                // Since we are not deleting any tweet we need not to keep track of next more frequent tweet
            }
        }
    }

    @Override
    public List<String> getTrendingTweets() {
        List<TweetWithFrequency> trendingTweetsFrequencyList = new ArrayList<>();
        Object[] tweetArray = trendingTweets.toArray(); // Max size of this array is 25 as max heap size is 25
        for (Object obj : tweetArray) {
            TweetWithFrequency twt = (TweetWithFrequency) obj;
            trendingTweetsFrequencyList.add(twt);
        }

        trendingTweetsFrequencyList.sort(new Comparator<TweetWithFrequency>() {
            @Override
            public int compare(TweetWithFrequency twf1, TweetWithFrequency twf2) {
                return twf2.getFrequency() - twf1.getFrequency();
            }
        });

        List<String> trendingTweetsList = new ArrayList<>();
        for (TweetWithFrequency twf : trendingTweetsFrequencyList) {
            trendingTweetsList.add(twf.getTweet());
        }
        return trendingTweetsList;
    }

    private String unifyTweet(String tweet) {
        String unifiedTweet = "";
        tweet = tweet.toLowerCase();
        // remove spaces from tweet while having record and retrieving
        for (int i = 0; i < tweet.length(); i++) {
            if (tweet.charAt(i) != ' ') unifiedTweet += tweet.charAt(i);
        }
        return unifiedTweet;
    }

    private TweetWithFrequency findTweetObjectWithFrequency(String tweet) {
        Object[] tweetArray = trendingTweets.toArray(); // Max size of this array is 25 as max heap size is 25
        for (Object obj : tweetArray) {
            TweetWithFrequency twt = (TweetWithFrequency) obj;
            if (tweet.equals(twt.getTweet()))
                return twt;
        }
        return null;
    }
}
