package com.xebia.itr.scala

// root import because our own package is named "scala", which overrides the toplevel scala package
import _root_.scala.xml._

import org.joda.time._
import org.joda.time.format._

import org.scalatest._
import org.scalatest.junit.JUnit3Suite

/*
 * Exercise 1:
 *
 * Your job is to implement the TwiterStatus class (and it's associated classes) in
 * such a way that the tests in this suite all succeed.
 */
class FirstExerciseTest extends JUnit3Suite {
    val twitterDateTimeFormat = DateTimeFormat.forPattern("EE MMM dd HH:mm:ss Z yyyy")

    private def getListOfTweets(): List[TwitterStatus] = {
        val xml = XML.load(this.getClass.getResourceAsStream("/friends_timeline_agemooij.xml"))
        val statuses = xml \\ "status"

        // This is where the TwitterStatus domain class is instantiated with a scala.xml.Node
        // representing the <status> element.
        statuses.elements.toList.map(s => TwitterStatus(s))
    }


    // ========================================================================
    // The tests
    // ========================================================================

	def testTwitterStatusParsing() {
	    val tweets = getListOfTweets()

	    // there should be 20 tweets
	    expect(20) {tweets.size}
    }

    def testAttributesOfFirstTweet() {
	    val firstTweet = getListOfTweets()(0)

	    expect(3362029699L) {firstTweet.id}

        expect(None) {firstTweet.inReplyToStatusId}
        expect(None) {firstTweet.inReplyToUserId}
        expect(false) {firstTweet.truncated}
        expect (false) {firstTweet.favorited}

	    expect("Having much more fun working on #jaoo talks than  yesterday's hard drive crash recovery.") {
	        firstTweet.text
        }

	    expect(twitterDateTimeFormat.parseDateTime("Mon Aug 17 14:19:06 +0000 2009")) {
	        firstTweet.createdAt
        }
	}

    def testAttributesOfUserAssociatedWithFirstTweet() {
	    val firstTweetUser: TwitterUser = getListOfTweets()(0).user

	    expect(16665197L) {firstTweetUser.id}
        expect("Martin Fowler") {firstTweetUser.name}
	    expect("martinfowler")   {firstTweetUser.screen_name}
	    expect("Loud Mouth, ThoughtWorks") {firstTweetUser.description}
	    expect("Boston") {firstTweetUser.location}
	    expect("http://www.martinfowler.com/") {firstTweetUser.url}
	    expect("http://a3.twimg.com/profile_images/79787739/mf-tg-sq_normal.jpg") {firstTweetUser.profileImageUrl}
	    expect(787) {firstTweetUser.statusesCount}
	    expect(166) {firstTweetUser.friendsCount}
	    expect(8735) {firstTweetUser.followersCount}
	}

}