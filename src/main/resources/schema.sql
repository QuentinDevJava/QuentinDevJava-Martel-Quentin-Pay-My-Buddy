-- 
-- Database: `paymybuddy`
-- 
CREATE DATABASE IF NOT EXISTS paymybuddy CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE paymybuddy;

-- --------------------------------------------------------
-- 
-- Structure of the `user` table
-- 
CREATE TABLE IF NOT EXISTS `user` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(255) NOT NULL, 
  `email` VARCHAR(255) NOT NULL UNIQUE,      
  `password` VARCHAR(255) NOT NULL, 
  PRIMARY KEY (`id`)                         
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------
-- 
-- Structure of the `transaction` table
-- 
CREATE TABLE IF NOT EXISTS `transaction` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `sender_id` INT(11) NOT NULL, 
  `receiver_id` INT(11) NOT NULL, 
  `description` TEXT DEFAULT NULL, 
  `amount` DECIMAL(10,2) NOT NULL,
  PRIMARY KEY (`id`),  
  CONSTRAINT `transaction_sender_id_user_id` FOREIGN KEY (`sender_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `transaction_receiver_id_user_id` FOREIGN KEY (`receiver_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------
-- 
-- Structure of the `user_connections` table
-- 
CREATE TABLE IF NOT EXISTS `user_connections` (
  `user_id` INT(11) NOT NULL, 
  `connection_id` INT(11) NOT NULL, 
  PRIMARY KEY (`user_id`, `connection_id`),  
  CONSTRAINT `user_connections_user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `user_connections_connection_id_fk` FOREIGN KEY (`connection_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CHECK (`user_id` != `connection_id`)      
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------
-- 
-- Index for the `transaction` table
-- 
ALTER TABLE `transaction`
  ADD KEY IF NOT EXISTS `transaction_sender_id_user_id` (`sender_id`),
  ADD KEY IF NOT EXISTS `transaction_receiver_id_user_id` (`receiver_id`);

-- --------------------------------------------------------
-- 
-- Index for the `user_connections` table
-- 
ALTER TABLE `user_connections`
  ADD KEY IF NOT EXISTS `connection_id` (`connection_id`);

-- 
-- Commit to apply all changes
COMMIT;