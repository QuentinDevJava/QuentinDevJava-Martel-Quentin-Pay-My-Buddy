--
-- Base de données : `paymybuddy`
--

-- --------------------------------------------------------

--
-- Structure de la table `transaction`
--
CREATE IF NOT EXISTS TABLE `transaction` (
  `id` int(11) NOT NULL,
  `sender_id` int(11) NOT NULL,
  `receiver_id` int(11) NOT NULL,
  `description` text DEFAULT NULL,
  `amount` decimal(10,2) NOT NULL
)
--
-- Déclencheurs `transaction`
--

DELIMITER $$
CREATE TRIGGER `before_insert_transaction` BEFORE INSERT ON `transaction` FOR EACH ROW BEGIN
    IF NEW.amount <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Le montant de la transaction doit être positif';
    END IF;
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `before_update_transaction` BEFORE UPDATE ON `transaction` FOR EACH ROW BEGIN
    IF NEW.amount <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Le montant de la transaction doit être positif';
    END IF;
END
$$
DELIMITER ;
-- --------------------------------------------------------

--
-- Structure de la table `user`
--

CREATE IF NOT EXISTS TABLE `user` (
  `id` int(11) NOT NULL,
  `username` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL
) 
-- --------------------------------------------------------

--
-- Structure de la table `user_connections`
--

CREATE IF NOT EXISTS TABLE `user_connections` (
  `user_id` int(11) NOT NULL,
  `connection_id` int(11) NOT NULL
)
--
-- Déclencheurs `user_connections`
--
DELIMITER $$
CREATE TRIGGER `check_self_connection_before_insert` BEFORE INSERT ON `user_connections` FOR EACH ROW BEGIN
    -- Vérifie si l'utilisateur tente de se connecter à lui-même
    IF NEW.user_id = NEW.connection_id THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Un utilisateur ne peut pas se connecter à lui-même';
    END IF;
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `check_self_connection_before_update` BEFORE UPDATE ON `user_connections` FOR EACH ROW BEGIN
    -- Vérifie si l'utilisateur tente de se connecter à lui-même
    IF NEW.user_id = NEW.connection_id THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Un utilisateur ne peut pas se connecter à lui-même';
    END IF;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Index pour la table `transaction`
--
ALTER TABLE `transaction`
  ADD PRIMARY KEY (`id`),
  ADD KEY `transaction_sender_id_user_id` (`sender_id`),
  ADD KEY `transaction_receiver_id_user_id` (`receiver_id`);

--
-- Index pour la table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email_unique` (`email`) USING BTREE;

--
-- Index pour la table `user_connections`
--
ALTER TABLE `user_connections`
  ADD PRIMARY KEY (`user_id`,`connection_id`),
  ADD KEY `connection_id` (`connection_id`);
  --
-- Contraintes pour la table `transaction`
--
ALTER TABLE `transaction`
  ADD CONSTRAINT `transaction_receiver_id_user_id` FOREIGN KEY (`receiver_id`) REFERENCES `user` (`id`),
  ADD CONSTRAINT `transaction_sender_id_user_id` FOREIGN KEY (`sender_id`) REFERENCES `user` (`id`);

--
-- Contraintes pour la table `user_connections`
--
ALTER TABLE `user_connections`
  ADD CONSTRAINT `user_connections_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  ADD CONSTRAINT `user_connections_2` FOREIGN KEY (`connection_id`) REFERENCES `user` (`id`);

COMMIT;
