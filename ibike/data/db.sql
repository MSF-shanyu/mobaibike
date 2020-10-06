create database ibike;

use ibike;

create table ibike(
	bid bigint primary key auto_increment,
	status int default 0,
	qrcode varchar(100) default '',
	latitude double,
	longitude double
);