/*
SQLyog - Free MySQL GUI v5.16
Host - 4.0.26-nt : Database - congress_development
*********************************************************************
Server version : 4.0.26-nt
*/

create database if not exists `congress_development`;

USE `congress_development`;

/*Table structure for table `channels` */

DROP TABLE IF EXISTS `channels`;

CREATE TABLE `channels` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(255) default NULL,
  `title` varchar(255) default NULL,
  `private` tinyint(4) default '0',
  `topic` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
) TYPE=MyISAM;

/*Data for the table `channels` */

insert into `channels` (`id`,`name`,`title`,`private`,`topic`) values (1,NULL,NULL,0,NULL);
insert into `channels` (`id`,`name`,`title`,`private`,`topic`) values (2,'main',NULL,0,NULL);

/*Table structure for table `channels_users` */

DROP TABLE IF EXISTS `channels_users`;

CREATE TABLE `channels_users` (
  `channel_id` int(11) NOT NULL default '0',
  `user_id` int(11) NOT NULL default '0',
  `last_seen` datetime default NULL,
  PRIMARY KEY  (`channel_id`,`user_id`)
) TYPE=MyISAM;

/*Data for the table `channels_users` */

insert into `channels_users` (`channel_id`,`user_id`,`last_seen`) values (1,1,'2006-08-10 09:28:36');
insert into `channels_users` (`channel_id`,`user_id`,`last_seen`) values (2,1,'2006-08-10 10:08:30');
insert into `channels_users` (`channel_id`,`user_id`,`last_seen`) values (2,2,'2006-08-10 10:08:47');

/*Table structure for table `messages` */

DROP TABLE IF EXISTS `messages`;

CREATE TABLE `messages` (
  `id` int(11) NOT NULL auto_increment,
  `created_at` datetime default NULL,
  `type` varchar(255) default NULL,
  `content` text,
  `channel_id` int(11) default NULL,
  `sender_id` int(11) default NULL,
  `level` varchar(255) default NULL,
  `recipient_id` int(11) default NULL,
  PRIMARY KEY  (`id`)
) TYPE=MyISAM;

/*Data for the table `messages` */

insert into `messages` (`id`,`created_at`,`type`,`content`,`channel_id`,`sender_id`,`level`,`recipient_id`) values (1,'2006-08-10 09:28:36',NULL,'joins the channel',1,1,'sys',NULL);
insert into `messages` (`id`,`created_at`,`type`,`content`,`channel_id`,`sender_id`,`level`,`recipient_id`) values (2,'2006-08-10 10:08:30',NULL,'joins the channel',2,1,'sys',NULL);
insert into `messages` (`id`,`created_at`,`type`,`content`,`channel_id`,`sender_id`,`level`,`recipient_id`) values (3,'2006-08-10 10:08:47',NULL,'joins the channel',2,2,'sys',NULL);

/*Table structure for table `users` */

DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
  `id` int(11) NOT NULL auto_increment,
  `nickname` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
) TYPE=MyISAM;

/*Data for the table `users` */

insert into `users` (`id`,`nickname`) values (1,'Default933');
insert into `users` (`id`,`nickname`) values (2,'Default128');
