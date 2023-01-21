CREATE TABLE `tv_info_mst` (
                                `idx` int(11) NOT NULL AUTO_INCREMENT,
                                `travel_code` varchar(255) NOT NULL,
                                `travel_title` longtext NOT NULL,
                                `travel_description` longtext NOT NULL,
                                `travel_address` varchar(255) NOT NULL,
                                `travel_zip_code` varchar(255) NOT NULL,
                                `view_count` int(11) NOT NULL DEFAULT 0,
                                `favorite_count` int(11) NOT NULL DEFAULT 0,
                                `visible` varchar(255) DEFAULT NULL,
                                `popular` boolean DEFAULT FALSE,
                                `create_time` datetime(6) DEFAULT NULL,
                                `creator` varchar(255) DEFAULT NULL,
                                `update_time` datetime(6) DEFAULT NULL,
                                `updater` varchar(255) DEFAULT NULL,
                                PRIMARY KEY (`idx`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;