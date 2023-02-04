CREATE TABLE `tv_group_user`
(
    `idx`       int(11) NOT NULL AUTO_INCREMENT,
    `user_idx`  int(11) NOT NULL,
    `group_idx` int(11) NOT NULL,
    PRIMARY KEY (`idx`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;

CREATE TABLE `tv_group_mst`
(
    `idx`               int(11)  NOT NULL AUTO_INCREMENT,
    `travel_idx`        int(11)  NOT NULL,
    `group_name`        longtext NOT NULL,
    `group_description` longtext NOT NULL,
    `visible`           varchar(255) DEFAULT 'Y',
    `create_time`       datetime(6)  DEFAULT NULL,
    `creator`           varchar(255) DEFAULT NULL,
    `update_time`       datetime(6)  DEFAULT NULL,
    `updater`           varchar(255) DEFAULT NULL,
    PRIMARY KEY (`idx`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;
