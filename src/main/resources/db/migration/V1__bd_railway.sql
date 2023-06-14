-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema railway
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema railway
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `railway` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish2_ci ;
USE `railway` ;

-- -----------------------------------------------------
-- Table `railway`.`flyway_schema_history`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `railway`.`flyway_schema_history` (
  `installed_rank` INT NOT NULL,
  `version` VARCHAR(50) NULL DEFAULT NULL,
  `description` VARCHAR(200) NOT NULL,
  `type` VARCHAR(20) NOT NULL,
  `script` VARCHAR(1000) NOT NULL,
  `checksum` INT NULL DEFAULT NULL,
  `installed_by` VARCHAR(100) NOT NULL,
  `installed_on` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `execution_time` INT NOT NULL,
  `success` TINYINT(1) NOT NULL,
  PRIMARY KEY (`installed_rank`),
  INDEX `flyway_schema_history_s_idx` (`success` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_spanish2_ci;


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
AUTO_INCREMENT = 6
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
AUTO_INCREMENT = 4
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


-- -----------------------------------------------------
-- Table `railway`.`sprints`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `railway`.`sprints` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `create_time` DATETIME(6) NOT NULL,
  `create_user_id` VARCHAR(50) NULL DEFAULT NULL,
  `deleted` BIT(1) NULL DEFAULT NULL,
  `update_time` DATETIME(6) NULL DEFAULT NULL,
  `update_user_id` VARCHAR(50) NULL DEFAULT NULL,
  `description` VARCHAR(500) NULL DEFAULT NULL,
  `end_date` DATETIME(6) NOT NULL,
  `name` VARCHAR(50) NOT NULL,
  `state` INT NOT NULL,
  `projects_id` BIGINT NOT NULL,
  `date` DATETIME(6) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `FKoeo3tmlarfb4q1viy1qdl5adv` (`projects_id` ASC) VISIBLE,
  CONSTRAINT `FKoeo3tmlarfb4q1viy1qdl5adv`
    FOREIGN KEY (`projects_id`)
    REFERENCES `railway`.`projects` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 25
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_spanish2_ci;


-- -----------------------------------------------------
-- Table `railway`.`tasks`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `railway`.`tasks` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `create_time` DATETIME(6) NOT NULL,
  `create_user_id` VARCHAR(50) NULL DEFAULT NULL,
  `deleted` BIT(1) NULL DEFAULT NULL,
  `update_time` DATETIME(6) NULL DEFAULT NULL,
  `update_user_id` VARCHAR(50) NULL DEFAULT NULL,
  `deadline` DATETIME(6) NOT NULL,
  `description` VARCHAR(500) NULL DEFAULT NULL,
  `name` VARCHAR(50) NOT NULL,
  `state` INT NOT NULL,
  `sprints_id` BIGINT NOT NULL,
  `date` DATETIME(6) NULL DEFAULT NULL,
  `users_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `FKbh75jvxo5nr2q0jwk8rsdv1da` (`sprints_id` ASC) VISIBLE,
  INDEX `FKgv5kftowjl0g49sk17alufjon` (`users_id` ASC) VISIBLE,
  CONSTRAINT `FKbh75jvxo5nr2q0jwk8rsdv1da`
    FOREIGN KEY (`sprints_id`)
    REFERENCES `railway`.`sprints` (`id`),
  CONSTRAINT `FKgv5kftowjl0g49sk17alufjon`
    FOREIGN KEY (`users_id`)
    REFERENCES `railway`.`users` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 25
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_spanish2_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
