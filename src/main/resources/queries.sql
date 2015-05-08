DROP ALL OBJECTS  DELETE FILES;

create table feedTypes (
	feedTypesId int primary key,
	description varchar(50)
);

INSERT INTO feedTypes VALUES (1, 'feedburner');

create table feeds (
	feedsId serial primary key,
	name varchar(50),
	url varchar(250),
	dateformat varchar(50),
	feedTypes int references feedTypes(feedTypesId),
	timezone varchar(50),
	htmlFieldName varchar(50),
	publishedDateFieldName varchar(25),
	linkFieldName varchar(50)
);

INSERT INTO feeds (url, name, dateformat, feedTypes, timezone, htmlFieldName, publishedDateFieldName, linkFieldName) 
	VALUES ('http://feeds.gawker.com/gizmodo/full', 'Gawker', 'EEE, dd MMM yyyy HH:mm:ss zzz', 1, 'UTC', 'description', 'pubDate', 'link');
INSERT INTO feeds (url, name, dateformat, feedTypes, timezone, htmlFieldName, publishedDateFieldName, linkFieldName) 
	VALUES ('http://feeds.feedburner.com/TechCrunch', 'TechCrunch', 'EEE, dd MMM yyyy HH:mm:ss Z', 1, 'UTC', 'description', 'pubDate', 'link');
INSERT INTO feeds (url, name, dateformat, feedTypes, timezone, htmlFieldName, publishedDateFieldName, linkFieldName) 
	VALUES ('http://feeds.ign.com/IGNPS4All', 'IGN PS4', 'EEE, dd MMM yyyy HH:mm:ss zzz', 1, 'UTC',  '//*[name()=''content:encoded'']', 'pubDate', 'link');
INSERT INTO feeds (url, name, dateformat, feedTypes, timezone, htmlFieldName, publishedDateFieldName, linkFieldName) 
	VALUES ('http://feeds.ign.com/ign/ps-vita-all', 'IGN PS Vita', 'EEE, dd MMM yyyy HH:mm:ss Z', 1, 'UTC',  '//*[name()=''content:encoded'']', 'pubDate', 'link');
INSERT INTO feeds (url, name, dateformat, feedTypes, timezone, htmlFieldName, publishedDateFieldName, linkFieldName) 
	VALUES ('http://blogof.francescomugnai.com/feed/', 'Francesco Mugnai', 'EEE, dd MMM yyyy HH:mm:ss Z', 1, 'UTC',  '//*[name()=''content:encoded'']', 'pubDate', 'link');
INSERT INTO feeds (url, name, dateformat, feedTypes, timezone, htmlFieldName, publishedDateFieldName, linkFieldName) 
	VALUES ('http://feeds.feedburner.com/design-milk', 'Design Milk', 'EEE, dd MMM yyyy HH:mm:ss zzz', 1, 'UTC',  '//*[name()=''content:encoded'']', 'pubDate', 'link');
INSERT INTO feeds (url, name, dateformat, feedTypes, timezone, htmlFieldName, publishedDateFieldName, linkFieldName) 
	VALUES ('http://pitchfork.com/rss/news/', 'Pitchfork News', 'EEE, dd MMM yyyy HH:mm:ss Z', 1, 'EST',  'description', 'pubDate', 'link');
INSERT INTO feeds (url, name, dateformat, feedTypes, timezone, htmlFieldName, publishedDateFieldName, linkFieldName) 
	VALUES ('http://pitchfork.com/rss/reviews/best/tracks/', 'Pitchfork Best Tracks', 'EEE, dd MMM yyyy HH:mm:ss Z', 1, 'America/New_York',  'description', 'pubDate', 'link');
INSERT INTO feeds (url, name, dateformat, feedTypes, timezone, htmlFieldName, publishedDateFieldName, linkFieldName) 
	VALUES ('http://gawker.com/index.xml', 'Gawker', 'EEE, dd MMM yyyy HH:mm:ss zzz', 1, 'UTC',  'description', 'pubDate', '//*[name()=''feedburner:origLink'']');
INSERT INTO feeds (url, name, dateformat, feedTypes, timezone, htmlFieldName, publishedDateFieldName, linkFieldName) 
	VALUES ('http://feeds.feedburner.com/DigitalPhotographySchool', 'Digital Photography School', 'EEE, dd MMM yyyy HH:mm:ss Z', 1, 'UTC',  'description', 'pubDate', 'link');
INSERT INTO feeds (url, name, dateformat, feedTypes, timezone, htmlFieldName, publishedDateFieldName, linkFieldName) 
	VALUES ('http://feeds.feedburner.com/The99Percent', 'The 99 Percent', 'EEE, dd MMM yyyy HH:mm:ss Z', 1, 'UTC',  'description', 'pubDate', 'link');
INSERT INTO feeds (url, name, dateformat, feedTypes, timezone, htmlFieldName, publishedDateFieldName, linkFieldName) 
	VALUES ('http://syndication.thedailywtf.com/thedailywtf', 'The Daily WTF', 'EEE, dd MMM yyyy HH:mm:ss zzz', 1, 'UTC',  'description', 'pubDate', 'link');
INSERT INTO feeds (url, name, dateformat, feedTypes, timezone, htmlFieldName, publishedDateFieldName, linkFieldName) 
	VALUES ('http://feeds.feedburner.com/FuelYourCreativity', 'Fuel Your Creativity', 'EEE, dd MMM yyyy HH:mm:ss zzz', 1, 'America/Tijuana',  'content', 'pubDate', 'link');
INSERT INTO feeds (url, name, dateformat, feedTypes, timezone, htmlFieldName, publishedDateFieldName, linkFieldName) 
	VALUES ('http://www.newyorker.com/services/rss/feeds/everything.xml', 'The New Yorker', 'EEE, dd MMM yyyy HH:mm:ss Z', 1, 'UTC',  'description', 'pubDate', 'link');
INSERT INTO feeds (url, name, dateformat, feedTypes, timezone, htmlFieldName, publishedDateFieldName, linkFieldName) 
	VALUES ('http://feeds.boston.com/boston/bigpicture/index', 'Boston Big Picture', 'EEE, dd MMM yyyy HH:mm:ss zzz', 1, 'UTC',  'description', 'pubDate', 'link');
INSERT INTO feeds (url, name, dateformat, feedTypes, timezone, htmlFieldName, publishedDateFieldName, linkFieldName) 
	VALUES ('http://www.huffingtonpost.com/feeds/index.xml', 'Huffington Post', 'EEE, dd MMM yyyy HH:mm:ss Z', 1, 'America/New_York',  'description', 'pubDate', 'link');
	
create table posts (
	postsId serial primary key,
	feedsId serial references feeds(feedsId),
	url varchar(1000),
	title varchar(250),
	html bytea,
	publishedDate timestamp,
	facebookLikes int,
	twitterTweets int
);

ALTER TABLE posts ADD CONSTRAINT unique_posts_constraint UNIQUE (feedsId,url,title);