package com.xebia.itr.scala

import _root_.scala.util.Random

import org.scalatest._
import org.scalatest.junit.JUnit3Suite


/*
 * Exercise 3: Talking http to the real deal: building a Twitter API
 *
 * This exercise will not really introduce you to all that many new features.
 * It simply makes you use everything you've learned already and apply it to
 * some API design.
 *
 * Your assignment is to implement the twitter API tested below on top of
 * HttpClient. The boring http requst stuff has already been done so you can
 * concentrate on the good stuff.
 *
 * Hints:
 *
 * - All classes that implement the Iterable[T] trait can be treated as any
 *   other type of collection (i.e. they have methods like map, filter, etc.)
 *
 * Bonus:
 *
 * - implement tweeting (i.e. post tweets to twitter). Posting a tweet returns
 *   the xml for the tweet you posted so a good API for tweet would be:
 *
 *   def tweet(text: String): TwitterStatus
 *
 *   The Twitter API docs for posting a status update are here:
 *
 *   http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses%C2%A0update
 *
 *   Note: Twitter will ignore duplicate tweets !!!
 *         Your tweets must be unique so use scala.util.Random !
 *
 */
class ThirdExerciseTest extends JUnit3Suite {
    val testAccountUsername = "XebiaScalaItr"
    val testAccountPassword = "Scala!Is!Cool!"


    // ========================================================================
    // The tests
    // ========================================================================

    def testPublicTimelineWithoutAuthentication {
        val twitter:UnauthenticatedSession = TwitterSession()
        val publicTimeline:TwitterTimeline = twitter.publicTimeline

        expect(20) {publicTimeline.toList.size}
        expect(true) {publicTimeline.forall(_.user != null)}
    }

    def testFriendsTimelineWithAuthentication {
        val twitter:AuthenticatedSession = TwitterSession(testAccountUsername, testAccountPassword)
        val friendsTimeline = twitter.friendsTimeline

        expect(20) {friendsTimeline.toList.size}
        expect(true) {friendsTimeline.forall(_.user != null)}
    }

    def testFriendsTimelineShouldOnlyContainTweetsByFriendsOrByMyself {
        val twitter:AuthenticatedSession = TwitterSession(testAccountUsername, testAccountPassword)

        val friendsTimeline = twitter.friendsTimeline
        val friends:TwitterUsers = twitter.friends

        expect(20) {friendsTimeline.toList.size}
        expect(true) {friendsTimeline.forall(tweet => friends.exists(_ == tweet.user) || testAccountUsername == tweet.user.screenName)}
    }

    def testUserTimelineWithoutAuthentication {
        val twitter:UnauthenticatedSession = TwitterSession()
        val userTimeline:TwitterTimeline = twitter.userTimeline("sgrijpink")

        expect(true) {userTimeline.forall(_.user.screenName == "sgrijpink")}
    }

    def testUserTimelineWithAuthentication {
        val twitter:AuthenticatedSession = TwitterSession(testAccountUsername, testAccountPassword)
        val userTimeline:TwitterTimeline = twitter.userTimeline

        expect(true) {userTimeline.forall(_.user.screenName == testAccountUsername)}
    }

    // Bonus exercise !!!

    def testTweet() {
        val twitter:AuthenticatedSession = TwitterSession(testAccountUsername, testAccountPassword)
        val baseText = "Yet another test tweet from a #Scala unit test. Let's include a random number: "
        val random = new Random

        val tweet = twitter.tweet(baseText + random.nextLong);

        expect(testAccountUsername) {tweet.user.screenName}
        expect(true) {tweet.text.contains(baseText)}
    }

}