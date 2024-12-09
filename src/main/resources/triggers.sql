-- ================================
-- Déclencheurs pour `transaction`
-- ================================

-- Crée le trigger `before_insert_transaction`
CREATE TRIGGER `before_insert_transaction`
BEFORE INSERT ON `transaction`
FOR EACH ROW
BEGIN
    -- Vérifie si le montant de la transaction est supérieur à zéro
    IF NEW.amount <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Transaction amount must be positive';
    END IF;
END;

-- Crée le trigger `before_update_transaction`
CREATE TRIGGER `before_update_transaction`
BEFORE UPDATE ON `transaction`
FOR EACH ROW
BEGIN
    -- Vérifie si le montant de la transaction est supérieur à zéro
    IF NEW.amount <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Transaction amount must be positive';
    END IF;
END;

-- ================================
-- Déclencheurs pour `user_connections`
-- ================================

-- Crée le trigger `check_self_connection_before_insert`
CREATE TRIGGER `check_self_connection_before_insert`
BEFORE INSERT ON `user_connections`
FOR EACH ROW
BEGIN
    -- Vérifie si un utilisateur tente de se connecter à lui-même
    IF NEW.user_id = NEW.connection_id THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'User cannot connect to themselves';
    END IF;
END;

-- Crée le trigger `check_self_connection_before_update`
CREATE TRIGGER `check_self_connection_before_update`
BEFORE UPDATE ON `user_connections`
FOR EACH ROW
BEGIN
    -- Vérifie si un utilisateur tente de se connecter à lui-même
    IF NEW.user_id = NEW.connection_id THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'User cannot connect to themselves';
    END IF;
END;
