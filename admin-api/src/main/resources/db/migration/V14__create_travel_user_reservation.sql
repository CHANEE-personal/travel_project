CREATE TABLE `travel_user_reservation`
(
    `idx`             int(11)     NOT NULL AUTO_INCREMENT,
    `reservation_idx` int(11)     NOT NULL,
    `price`           int(11)     NOT NULL,
    `sale_price`      int(11)     NOT NULL,
    `user_count`      int(11)     NOT NULL,
    `start_date`      datetime(6) NOT NULL,
    `end_date`        datetime(6) NOT NULL,
    `user_idx`        int(11)     NOT NULL,
    `create_time`     datetime(6)  DEFAULT NULL,
    `creator`         varchar(255) DEFAULT NULL,
    `update_time`     datetime(6)  DEFAULT NULL,
    `updater`         varchar(255) DEFAULT NULL,
    PRIMARY KEY (`idx`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;
