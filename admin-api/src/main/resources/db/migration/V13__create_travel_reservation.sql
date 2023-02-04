CREATE TABLE `travel_reservation`
(
    `idx`            int(11)      NOT NULL AUTO_INCREMENT,
    `common_code`    int(11)      NOT NULL,
    `title`          longtext     NOT NULL,
    `description`    longtext     NOT NULL,
    `address`        varchar(255) NOT NULL,
    `zip_code`       varchar(255) NOT NULL,
    `price`          int(11)      NOT NULL,
    `possible_count` int(11)      NOT NULL,
    `start_date`     datetime(6)  NOT NULL,
    `end_date`       datetime(6)  NOT NULL,
    `status`         boolean      DEFAULT FALSE,
    `popular`        boolean      DEFAULT false,
    `create_time`    datetime(6)  DEFAULT NULL,
    `creator`        varchar(255) DEFAULT NULL,
    `update_time`    datetime(6)  DEFAULT NULL,
    `updater`        varchar(255) DEFAULT NULL,
    PRIMARY KEY (`idx`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;
