---
title: Excercise Two
---

The [clustering coefficient][cc] is a well known measurement in graph theory.  For the final
excercise of the Fluo Tour, take everything you have learned and incrementally compute the
clustering coefficient.  To make the example interesting, assume the graph is based on Tweets.
Compute a clustering coefficient for each hashtag based on users that mention each other.  Then sort
the clustering coefficients in descending order.   If you are scratching your head, dont worry this
is probably best explained with an example.

Assume we have the following tweets :

| From       | Tweet
| ---------- | -------------------
| **@user1** | *blah blah* **#tag1** *blah blah*
| **@user1** | *blah blah* **@user2** *blah blah*
| **@user2** | *blah blah* **#tag1** *blah blah*
| **@user3** | *blah blah* **#tag1** *blah blah*

These tweets build the following graph.

![tweet graph](/resources/tour/tweets-cc.png){: .center-block }

The dotted lines in the graph are possible edges that do not actually exist.  The hashtag *#tag1*
has 3 neighbors.  There is one edge between those three neighbors.  There are three possible edges
between 3 neighbors.  Therefore the clustering coefficient for *#tag1* is 1/3.

Getting into the mindset of writing incremental solutions can take a bit.  To help an outline of the
solution is provided for the final exercise on the next page.  If you have time, try thinking of how
you would solve this problem incrementally without looking at the outline.  One hint: there are two
edge types (a user/tag edge and user/user edge), think about what to do when a new one of these
types comes into existence.

Below is simple skeleton to get you started.


```java

  //Some simulated data
  static final Tweet[] tweets1 = new Tweet[] {
      Tweet.newTweet("@user1").withTags("#tag1").build(),
      Tweet.newTweet("@user1").withTags("#tag2").build(),
      Tweet.newTweet("@user1").withTags("#tag3").withMentions("@user2").build(),
      Tweet.newTweet("@user2").withTags("#tag1").build(),
      Tweet.newTweet("@user3").withTags("#tag1").build(),
      Tweet.newTweet("@user4").withMentions("@user1", "@user6").build(),
      Tweet.newTweet("@user4").withTags("#tag2").build(),
      Tweet.newTweet("@user5").withTags("#tag2").withMentions("@user1").build(),
      Tweet.newTweet("@user6").withTags("#tag2", "#tag3").withMentions("@user7").build()};

  static final Tweet[] tweets2 = new Tweet[] {
      Tweet.newTweet("@user1").withTags("#tag1","#tag2").build(),
      Tweet.newTweet("@user1").withMentions("@user5","@user7").build(),
      Tweet.newTweet("@user3").withMentions("@user7").build()};

  static final Tweet[] tweets3 = new Tweet[] {
      Tweet.newTweet("@user7").withTags("#tag1","#tag3").build(),
      Tweet.newTweet("@user1").withMentions("@user6").build()};

  static final Tweet[] tweets4 = new Tweet[] {
      Tweet.newTweet("@user1").withTags("#tag4").build(),
      Tweet.newTweet("@user2").withTags("#tag4").withMentions("@user3").build(),
      Tweet.newTweet("@user3").withTags("#tag4").build(),
      Tweet.newTweet("@user4").withTags("#tag4").build(),
      Tweet.newTweet("@user5").withTags("#tag4").build(),
      Tweet.newTweet("@user6").withTags("#tag4").build(),
      Tweet.newTweet("@user7").withTags("#tag4").build()};

  private static void preInit(FluoConfiguration fluoConfig) {
    //TODO add some observers
  }

  private static void load(FluoClient client, Tweet ... tweets) {
    try (LoaderExecutor loader = client.newLoaderExecutor()) {
      for (Tweet tweet : tweets) {
        //TODO create TweetLoader
        loader.execute(new TweetLoader(tweet));
      }
    }
  }

  private static void print(FluoClient client) {
    System.out.println();
    try (Snapshot snap = client.newSnapshot()) {
      //TODO print out cluster coefficients in descending order
    }
  }

  private static void excercise(MiniFluo mini, FluoClient client) {

    load(client, tweets1);
    mini.waitForObservers();
    print(client);

    //should not change anything
    load(client, tweets2);
    mini.waitForObservers();
    print(client);

    load(client, tweets3);
    mini.waitForObservers();
    print(client);

    load(client, tweets4);
    mini.waitForObservers();
    print(client);
  }

```

Once completed, the excercise should print out something like the following for the data provided
above.

```
0.500 : #tag2
0.333 : #tag1
0.000 : #tag3

0.500 : #tag2
0.333 : #tag1
0.000 : #tag3

1.000 : #tag3
0.667 : #tag2
0.500 : #tag1

1.000 : #tag3
0.667 : #tag1
0.667 : #tag2
0.429 : #tag4

```

Below is the code for the Tweet class used above.

```java
package ft.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.ImmutableList;

/**
 * A simple class that holds pertitnent data for a tweet.
 */
public class Tweet {
  public final String from;
  public final List<String> hashtags;
  public final List<String> mentions;

  public Tweet(String from, List<String> hashtags, List<String> mentions) {
    this.from = from;
    this.hashtags = ImmutableList.copyOf(hashtags);
    // TODO remove self mentions
    this.mentions = ImmutableList.copyOf(mentions);
  }

  public static class TweetBuilder {
    private String from;
    private List<String> hashtags = Collections.emptyList();
    private List<String> mentions = Collections.emptyList();

    private TweetBuilder(String from) {
      this.from = from;
    }

    public Tweet.TweetBuilder withTags(String... tags) {
      this.hashtags = Arrays.asList(tags);
      return this;
    }

    public Tweet.TweetBuilder withMentions(String... mentions) {
      this.mentions = Arrays.asList(mentions);
      return this;
    }

    public Tweet build() {
      return new Tweet(from, hashtags, mentions);
    }
  }

  public static Tweet.TweetBuilder newTweet(String from) {
    return new TweetBuilder(from);
  }
}
```

If you code up a solution to this exercise, consider tweeting a link to it with the hashtag
[#apachefluotour][aft].

[cc]: https://en.wikipedia.org/wiki/Clustering_coefficient
[aft]: https://twitter.com/hashtag/apachefluotour


