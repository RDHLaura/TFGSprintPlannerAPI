-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema railway
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `railway` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish2_ci ;
USE `railway` ;

-- -----------------------------------------------------
-- Table `railway`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `railway`.`users` (
                                                          `id` BIGINT NOT NULL AUTO_INCREMENT,
                                                          `create_time` DATETIME(6) NOT NULL,
    `create_user_id` VARCHAR(50) NULL DEFAULT NULL,
    `deleted` BIT(1) NULL DEFAULT NULL,
    `update_time` DATETIME(6) NULL DEFAULT NULL,
    `update_user_id` VARCHAR(50) NULL DEFAULT NULL,
    `avatar` VARCHAR(200) NULL DEFAULT NULL,
    `email` VARCHAR(100) NOT NULL,
    `password` VARCHAR(200) NOT NULL,
    `username` VARCHAR(50) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `UK_6dotkott2kjsp8vw4d0m25fb7` (`email` ASC) VISIBLE,
    UNIQUE INDEX `UK_r43af9ap4edm43mmtq01oddj6` (`username` ASC) VISIBLE)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_spanish2_ci;


-- -----------------------------------------------------
-- Table `railway`.`projects`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `railway`.`projects` (
                                                             `id` BIGINT NOT NULL AUTO_INCREMENT,
                                                             `create_time` DATETIME(6) NOT NULL,
    `create_user_id` VARCHAR(50) NULL DEFAULT NULL,
    `deleted` BIT(1) NULL DEFAULT NULL,
    `update_time` DATETIME(6) NULL DEFAULT NULL,
    `update_user_id` VARCHAR(50) NULL DEFAULT NULL,
    `description` VARCHAR(500) NULL DEFAULT NULL,
    `state` INT NOT NULL,
    `title` VARCHAR(255) NOT NULL,
    `users_id` BIGINT NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    INDEX `FKc7n80muxvf9gx2w5ae4wg0knj` (`users_id` ASC) VISIBLE,
    CONSTRAINT `FKc7n80muxvf9gx2w5ae4wg0knj`
    FOREIGN KEY (`users_id`)
    REFERENCES `railway`.`users` (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_spanish2_ci;


-- -----------------------------------------------------
-- Table `railway`.`participations`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `railway`.`participations` (
                                                                   `projects_id` BIGINT NOT NULL,
                                                                   `users_id` BIGINT NOT NULL,
                                                                   INDEX `FKjknx8qjryles0rkm6no0t4den` (`users_id` ASC) VISIBLE,
    INDEX `FKqc2cf8f2281af2sfypslegh7r` (`projects_id` ASC) VISIBLE,
    CONSTRAINT `FKjknx8qjryles0rkm6no0t4den`
    FOREIGN KEY (`users_id`)
    REFERENCES `railway`.`users` (`id`),
    CONSTRAINT `FKqc2cf8f2281af2sfypslegh7r`
    FOREIGN KEY (`projects_id`)
    REFERENCES `railway`.`projects` (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_spanish2_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;