-- grant ALL opensocial.* 'opensocial'@'localhost' identified by 'opensocial';
-- create database gifts;

DROP TABLE IF EXISTS `gifts`;
CREATE TABLE `gifts` (
  `from` varchar(50) NOT NULL default '0',
  `to` varchar(50) NOT NULL default '0',
  `gid` varchar(20) NOT NULL default '0',
  `ts` int(11) NOT NULL default '0',
  KEY `from` (`from`),
  KEY `to` (`to`)
)

