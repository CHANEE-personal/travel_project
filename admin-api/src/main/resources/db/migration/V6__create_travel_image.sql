CREATE TABLE `travel_image` (
                             `idx` int(11) NOT NULL AUTO_INCREMENT,
                             `file_num` int(11) DEFAULT NULL,
                             `file_name` varchar(255) DEFAULT NULL,
                             `file_mask` varchar(255) DEFAULT NULL,
                             `file_path` varchar(255) DEFAULT NULL,
                             `file_size` int(11) DEFAULT NULL,
                             `type_idx` int(11) DEFAULT NULL,
                             `type_name` varchar(255) DEFAULT NULL,
                             `image_type` varchar(255) DEFAULT NULL,
                             `visible` varchar(255) DEFAULT 'N',
                             `reg_date` datetime(6) DEFAULT NULL,
                             PRIMARY KEY (`idx`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;