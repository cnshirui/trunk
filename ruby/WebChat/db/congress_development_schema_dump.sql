# CocoaMySQL dump
# Version 0.7b2
# http://cocoamysql.sourceforge.net
#
# Host: localhost (MySQL 5.0.15-standard)
# Database: congress_development
# Generation Time: 2005-12-19 21:29:44 +0000
# ************************************************************

# Dump of table channels
# ------------------------------------------------------------

DROP TABLE IF EXISTS `channels`;

CREATE TABLE `channels` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(255) default NULL,
  `title` varchar(255) default NULL,
  `private` tinyint(4) default '0',
  `topic` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;



# Dump of table channels_users
# ------------------------------------------------------------

DROP TABLE IF EXISTS `channels_users`;

CREATE TABLE `channels_users` (
  `channel_id` int(11) NOT NULL default '0',
  `user_id` int(11) NOT NULL default '0',
  `last_seen` datetime default NULL,
  PRIMARY KEY  (`channel_id`,`user_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;



# Dump of table messages
# ------------------------------------------------------------

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
) ENGINE=MyISAM DEFAULT CHARSET=latin1;



# Dump of table users
# ------------------------------------------------------------

DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
  `id` int(11) NOT NULL auto_increment,
  `nickname` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;



