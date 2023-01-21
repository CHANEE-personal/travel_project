CREATE TABLE `travel_review` (
       `idx` int(11) NOT NULL AUTO_INCREMENT,
       `travel_idx` int(11) NOT NULL,
       `review_title` longtext NOT NULL,
       `review_description` longtext NOT NULL,
       `view_count` int(11) NOT NULL,
       `favorite_count` int(11) NOT NULL,
       `visible` varchar(1) NOT NULL,
       `popular` boolean DEFAULT false,
       `create_time` datetime(6) DEFAULT NULL,
       `creator` varchar(255) DEFAULT NULL,
       `update_time` datetime(6) DEFAULT NULL,
       `updater` varchar(255) DEFAULT NULL,
       PRIMARY KEY (`idx`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;