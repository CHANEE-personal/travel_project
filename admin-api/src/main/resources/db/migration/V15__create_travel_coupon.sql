CREATE TABLE `travel_coupon`
(
    `idx`               int(11)     NOT NULL AUTO_INCREMENT,
    `title`             longtext    NOT NULL,
    `description`       longtext    NOT NULL,
    `percentage_status` boolean     NOT NULL DEFAULT FALSE,
    `sale_price`        int(11)     NOT NULL DEFAULT 0,
    `start_date`        datetime(6) NOT NULL,
    `end_date`          datetime(6) NOT NULL,
    `count`             int(11)     NOT NULL DEFAULT 0,
    `percentage`        int(11)     NOT NULL DEFAULT 0,
    `status`            boolean     NOT NULL DEFAULT FALSE,
    `create_time`       datetime(6)          DEFAULT NULL,
    `creator`           varchar(255)         DEFAULT NULL,
    `update_time`       datetime(6)          DEFAULT NULL,
    `updater`           varchar(255)         DEFAULT NULL,
    PRIMARY KEY (`idx`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;
