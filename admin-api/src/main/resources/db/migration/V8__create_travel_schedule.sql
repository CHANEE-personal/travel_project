CREATE TABLE `travel_schedule`
(
    `idx`                  int(11)     NOT NULL AUTO_INCREMENT,
    `user_idx`             int(11)     NOT NULL,
    `travel_code`          int(11)     NOT NULL,
    `schedule_description` longtext    NOT NULL,
    `schedule_time`        datetime(6) NOT NULL,
    `create_time`          datetime(6)  DEFAULT NULL,
    `creator`              varchar(255) DEFAULT NULL,
    `update_time`          datetime(6)  DEFAULT NULL,
    `updater`              varchar(255) DEFAULT NULL,
    PRIMARY KEY (`idx`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;
