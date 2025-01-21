-- 
-- Database: `paymybuddy`
-- 
-- --------------------------------------------------------
-- 
-- Structure of the `pmb_user` table
CREATE TABLE IF NOT EXISTS pmb_user (
  id INT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(255) NOT NULL, 
  email VARCHAR(255) NOT NULL UNIQUE,      
  password VARCHAR(255) NOT NULL
);
-- --------------------------------------------------------
-- 
-- Structure of the `pmb_transaction` table
-- 
CREATE TABLE IF NOT EXISTS pmb_transaction (
  id INT PRIMARY KEY AUTO_INCREMENT,
  sender_id INT NOT NULL, 
  receiver_id INT NOT NULL, 
  description TEXT DEFAULT NULL, 
  amount DECIMAL NOT NULL
);
-- --------------------------------------------------------
-- 
-- Structure of the `pmb_user_connections` table
-- 
CREATE TABLE IF NOT EXISTS pmb_user_connections (
  user_id INT NOT NULL,
  connection_id INT NOT NULL,
  PRIMARY KEY (user_id, connection_id)
);