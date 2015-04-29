
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
	feedsTypeId int references feedTypes(feedTypesId),
	timezone varchar(10),
	htmlFieldName varchar(50),
	publishedDateFieldName varchar(25),
	linkFieldName varchar(50)
);

INSERT INTO feeds (url, name, dateformat, feedsTypeId, timezone, htmlFieldName, publishedDateFieldName, linkFieldName) 
	VALUES ('http://feeds.gawker.com/gizmodo/full', 'Gawker', 'EEE, dd MMM yyyy HH:mm:ss zzz', 1, 'UTC', 'description', 'pubDate', 'link');
INSERT INTO feeds (url, name, dateformat, feedsTypeId, timezone, htmlFieldName, publishedDateFieldName, linkFieldName) 
	VALUES ('http://feeds.feedburner.com/TechCrunch', 'TechCrunch', 'EEE, dd MMM yyyy HH:mm:ss Z', 1, 'UTC', 'description', 'pubDate', 'link');
INSERT INTO feeds (url, name, dateformat, feedsTypeId, timezone, htmlFieldName, publishedDateFieldName, linkFieldName) 
	VALUES ('http://feeds.ign.com/IGNPS4All', 'IGN PS4', 'EEE, dd MMM yyyy HH:mm:ss zzz', 1, 'UTC',  '//*[name()=''content:encoded'']', 'pubDate', 'link');
INSERT INTO feeds (url, name, dateformat, feedsTypeId, timezone, htmlFieldName, publishedDateFieldName, linkFieldName) 
	VALUES ('http://feeds.ign.com/ign/ps-vita-all', 'IGN PS Vita', 'EEE, dd MMM yyyy HH:mm:ss Z', 1, 'UTC',  '//*[name()=''content:encoded'']', 'pubDate', 'link');

create table posts (
	postsId serial primary key,
	feedsId serial references feeds(feedsId),
	url varchar(1000),
	title varchar(250),
	html bytea,
	publishedDate timestamp with time zone,
	facebookLikes int,
	twitterTweets int
)

ALTER TABLE posts ADD CONSTRAINT unique_posts_constraint UNIQUE (feedsId,url,title);