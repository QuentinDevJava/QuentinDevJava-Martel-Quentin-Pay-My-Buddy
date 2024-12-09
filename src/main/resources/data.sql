-- Base de données : `paymybuddy`
USE paymybuddy;

-- --------------------------------------------------------
-- Loading data into the `user` table only if it is empty
-- --------------------------------------------------------
INSERT INTO `user` (`username`, `email`, `password`)
SELECT * FROM (SELECT 'JohnDoe', 'john@example.com', 'hashedpassword123' UNION ALL
               SELECT 'JaneSmith', 'jane@example.com', 'securepass456' UNION ALL
               SELECT 'BobJohnson', 'bob@example.com', 'strongpass789') AS v
WHERE NOT EXISTS (
    SELECT 1 FROM `user` LIMIT 1
);

-- --------------------------------------------------------
-- Loading data into the `transaction` table only if it is empty
-- --------------------------------------------------------
INSERT INTO `transaction` (`sender_id`, `receiver_id`, `description`, `amount`)
SELECT * FROM (SELECT 1, 2, 'Payment for dinner', 25.50 UNION ALL
               SELECT 2, 3, 'Movie reimbursement', 15.00 UNION ALL
               SELECT 3, 1, 'Share for the group gift', 30.00 UNION ALL
               SELECT 3, 2, 'Share for the group gift', 50.00) AS v
WHERE NOT EXISTS (
    SELECT 1 FROM `transaction` LIMIT 1
);

-- --------------------------------------------------------
-- Loading data into the `user_connections` table only if it is empty
-- --------------------------------------------------------
INSERT INTO `user_connections` (`user_id`, `connection_id`)
SELECT * FROM (SELECT 1, 2 UNION ALL
               SELECT 1, 3 UNION ALL
               SELECT 2, 1 UNION ALL
               SELECT 2, 3 UNION ALL
               SELECT 3, 1 UNION ALL
               SELECT 3, 2) AS v
WHERE NOT EXISTS (
    SELECT 1 FROM `user_connections` LIMIT 1
);

COMMIT;