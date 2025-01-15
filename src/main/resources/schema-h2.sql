CREATE TABLE IF NOT EXISTS users (
  id INT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(255) NOT NULL, 
  email VARCHAR(255) NOT NULL UNIQUE,      
  password VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS `transaction` (
  id INT PRIMARY KEY AUTO_INCREMENT,
  sender_id INT NOT NULL, 
  receiver_id INT NOT NULL, 
  description TEXT DEFAULT NULL, 
  amount DECIMAL NOT NULL
);

CREATE TABLE IF NOT EXISTS `user_connections` (
  user_id INT NOT NULL,
  connection_id INT NOT NULL,
  PRIMARY KEY (user_id, connection_id)
);