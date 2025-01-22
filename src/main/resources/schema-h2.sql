-- 
-- Database: `paymybuddy`
-- 
-- --------------------------------------------------------
-- 
-- Structure of the `pmb_user` table
CREATE TABLE pmb_user (
  id INT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(255) NOT NULL, 
  email VARCHAR(255) NOT NULL UNIQUE,      
  password VARCHAR(255) NOT NULL
);
-- --------------------------------------------------------
-- 
-- Structure of the `pmb_transaction` table
-- 
CREATE TABLE pmb_transaction (
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
CREATE TABLE pmb_user_connections (
  user_id INT NOT NULL,
  connection_id INT NOT NULL,
  PRIMARY KEY (user_id, connection_id)
);

ALTER TABLE pmb_transaction
ADD CONSTRAINT "transaction_sender_id_user_id"
FOREIGN KEY (sender_id)
REFERENCES pmb_user (id)
ON DELETE CASCADE;

ALTER TABLE pmb_user_connections
ADD CONSTRAINT "user_connections_user_id_fk"
FOREIGN KEY (user_id)
REFERENCES pmb_user (id)
ON DELETE CASCADE;

ALTER TABLE pmb_user_connections
ADD CONSTRAINT "user_connections_connection_id_fk"
FOREIGN KEY (connection_id)
REFERENCES pmb_user (id)
ON DELETE CASCADE;