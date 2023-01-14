CREATE TABLE `travel_post` (
                              `idx` int(11) NOT NULL AUTO_INCREMENT,
                              `post_title` varchar(255) NOT NULL,
                              `post_description` longtext NOT NULL,
                              `visible` varchar(255) DEFAULT 'N',
                              `view_count` int(11) DEFAULT 0,
                              `favorite_count` int(11) DEFAULT 0,
                              `popular` boolean DEFAULT FALSE,
                              `create_time` datetime(6) DEFAULT NULL,
                              `creator` varchar(255) DEFAULT NULL,
                              `update_time` datetime(6) DEFAULT NULL,
                              `updater` varchar(255) DEFAULT NULL,
                              PRIMARY KEY (`idx`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

alter table travel_post add index `popular_index` (`popular`);

CREATE TABLE `travel_post_comment` (
                               `idx` int(11) NOT NULL AUTO_INCREMENT,
                               `comment_title` varchar(255) NOT NULL,
                               `comment_description` longtext NOT NULL,
                               `post_id` int(11) NOT NULL,
                               `parent_id` int(11) DEFAULT NULL,
                               `visible` varchar(255) DEFAULT 'N',
                               `favorite_count` int(11) DEFAULT 0,
                               `create_time` datetime(6) DEFAULT NULL,
                               `creator` varchar(255) DEFAULT NULL,
                               `update_time` datetime(6) DEFAULT NULL,
                               `updater` varchar(255) DEFAULT NULL,
                               PRIMARY KEY (`idx`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

