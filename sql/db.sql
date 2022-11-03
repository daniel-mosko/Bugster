-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema bugster2022
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema bugster2022
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `bugster2022` DEFAULT CHARACTER SET utf8 COLLATE utf8_slovak_ci ;
USE `bugster2022` ;

-- -----------------------------------------------------
-- Table `bugster2022`.`role`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bugster2022`.`role` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(15) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `bugster2022`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bugster2022`.`user` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `surname` VARCHAR(45) NOT NULL,
  `username` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  `role_id` INT NOT NULL,
  `active` TINYINT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_user_role1_idx` (`role_id` ASC) VISIBLE,
  CONSTRAINT `fk_user_role1`
    FOREIGN KEY (`role_id`)
    REFERENCES `bugster2022`.`role` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `bugster2022`.`project`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bugster2022`.`project` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `description` VARCHAR(200) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `bugster2022`.`status`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bugster2022`.`status` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(15) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `bugster2022`.`severity`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bugster2022`.`severity` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `severity_level` INT NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `bugster2022`.`bug`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bugster2022`.`bug` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `description` VARCHAR(80) NOT NULL,
  `created_at` DATETIME NOT NULL DEFAULT NOW(),
  `updated_at` TIMESTAMP NULL,
  `project_id` INT NOT NULL,
  `assginee_id` INT NULL,
  `assigner_id` INT NOT NULL,
  `status_id` INT NOT NULL,
  `severity_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_bug_project1_idx` (`project_id` ASC) VISIBLE,
  INDEX `fk_bug_user1_idx` (`assginee_id` ASC) VISIBLE,
  INDEX `fk_bug_user2_idx` (`assigner_id` ASC) VISIBLE,
  INDEX `fk_bug_status1_idx` (`status_id` ASC) VISIBLE,
  INDEX `fk_bug_severity1_idx` (`severity_id` ASC) VISIBLE,
  CONSTRAINT `fk_bug_project1`
    FOREIGN KEY (`project_id`)
    REFERENCES `bugster2022`.`project` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_bug_user1`
    FOREIGN KEY (`assginee_id`)
    REFERENCES `bugster2022`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_bug_user2`
    FOREIGN KEY (`assigner_id`)
    REFERENCES `bugster2022`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_bug_status1`
    FOREIGN KEY (`status_id`)
    REFERENCES `bugster2022`.`status` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_bug_severity1`
    FOREIGN KEY (`severity_id`)
    REFERENCES `bugster2022`.`severity` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `bugster2022`.`user_has_project`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bugster2022`.`user_has_project` (
  `user_id` INT NOT NULL,
  `project_id` INT NOT NULL,
  PRIMARY KEY (`user_id`, `project_id`),
  INDEX `fk_user_has_project_project1_idx` (`project_id` ASC) VISIBLE,
  INDEX `fk_user_has_project_user1_idx` (`user_id` ASC) VISIBLE,
  CONSTRAINT `fk_user_has_project_user1`
    FOREIGN KEY (`user_id`)
    REFERENCES `bugster2022`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_has_project_project1`
    FOREIGN KEY (`project_id`)
    REFERENCES `bugster2022`.`project` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
