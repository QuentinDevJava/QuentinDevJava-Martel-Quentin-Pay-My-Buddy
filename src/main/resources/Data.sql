--
-- Base de données : `paymybuddy`
--

-- --------------------------------------------------------

INSERT INTO `transaction` (`id`, `sender_id`, `receiver_id`, `description`, `amount`) VALUES
(1, 1, 2, 'Paiement pour le dîner', 25.50),
(2, 2, 3, 'Remboursement du cinéma', 15.00),
(3, 3, 1, 'Part pour le cadeau commun', 30.00),
(4, 3, 2, 'Part pour le cadeau commun', 50.00);

--
-- Chargement des données de la table `user`
--

INSERT INTO `user` (`id`, `username`, `email`, `password`) VALUES
(1, 'JohnDoe', 'john@example.com', 'hashedpassword123'),
(2, 'JaneSmith', 'jane@example.com', 'securepass456'),
(3, 'BobJohnson', 'bob@example.com', 'strongpass789');

--
-- Chargement des données de la table `user_connections`
--

INSERT INTO `user_connections` (`user_id`, `connection_id`) VALUES
(1, 2),
(1, 3),
(2, 1),
(2, 3),
(3, 1),
(3, 2);

COMMIT;
