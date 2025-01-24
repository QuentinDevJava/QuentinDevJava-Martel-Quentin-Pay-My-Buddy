-- 
-- Database: `paymybuddy`
-- 
-- --------------------------------------------------------
-- 
-- Structure of the `pmb_user` table
-- 
CREATE TABLE IF NOT EXISTS `pmb_user` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(255) NOT NULL UNIQUE, 
  `email` VARCHAR(255) NOT NULL UNIQUE,      
  `password` VARCHAR(255) NOT NULL, 
  PRIMARY KEY (`id`)                         
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------
-- 
-- Structure of the `pmb_transaction` table
-- 
CREATE TABLE IF NOT EXISTS `pmb_transaction` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `sender_id` INT(11) NOT NULL, 
  `receiver_id` INT(11) NOT NULL, 
  `description` VARCHAR(255) DEFAULT NULL, 
  `amount` DECIMAL(10,2) NOT NULL,
  PRIMARY KEY (`id`),  
  CONSTRAINT `transaction_sender_id_user_id` FOREIGN KEY (`sender_id`) REFERENCES `pmb_user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `transaction_receiver_id_user_id` FOREIGN KEY (`receiver_id`) REFERENCES `pmb_user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `check_positive_amount` CHECK (`amount` > 0)   
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------
-- 
-- Structure of the `pmb_user_connections` table
-- 
CREATE TABLE IF NOT EXISTS `pmb_user_connections` (
  `user_id` INT(11) NOT NULL, 
  `connection_id` INT(11) NOT NULL, 
  PRIMARY KEY (`user_id`, `connection_id`),  
  CONSTRAINT `user_connections_user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `pmb_user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `user_connections_connection_id_fk` FOREIGN KEY (`connection_id`) REFERENCES `pmb_user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `check_user_id_different_connection_id` CHECK (`user_id` != `connection_id`)      
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 
-- Commit to apply all changes
COMMIT;