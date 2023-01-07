CREATE TABLE `travel_search` (
                                    `idx` int(11) NOT NULL AUTO_INCREMENT,
                                    `search_keyword` varchar(255) NOT NULL,
                                    `create_time` datetime(6) DEFAULT NULL,
                                    `creator` varchar(255) DEFAULT NULL,
                                    `update_time` datetime(6) DEFAULT NULL,
                                    `updater` varchar(255) DEFAULT NULL,
                                    PRIMARY KEY (`idx`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;