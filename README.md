
# **Pay My Buddy Project - Database**

## **Overview**

This project is a **money transfer application** designed to allow users to send money to other users, manage their accounts, and establish connections with other users for easy transaction management.

This document outlines the database structure, the relationships between tables, and constraints to ensure data integrity within the system.

## **Database Structure**

The physical data model (MPD) describes the tables, columns, and relationships between the main entities in the application. The following tables are included in the database design:

![Diagramme](https://github.com/user-attachments/assets/e8ca84f0-ef6c-456d-8b1a-3dfc5b13a0c7)


### 1. **pbm_user Table**
This table stores information about users in the application.
- **Columns**: 
  - `id` (Primary Key)
  - `username` (Unique)
  - `email` (Unique)
  - `password`

### 2. **pbm_transaction Table**
This table records the details of money transactions.
- **Columns**:
  - `id` (Primary Key)
  - `sender_id` (Foreign Key, references `User(id)`)
  - `receiver_id` (Foreign Key, references `User(id)`)
  - `description`
  - `amount` (Positive Value)

### 3. **pbm_user_connections Table**
This table represents the **many-to-many relationship** between users, allowing them to connect with each other.
- **Columns**:
  - `user_id` (Foreign Key, references `User(id)`)
  - `connection_id` (Foreign Key, references `User(id)`)
  - **Primary Key**: Combination of `user_id` and `connection_id`

## **Relationships Between Tables**

The key relationships between the tables are outlined below:

1. **pbm_user ↔ pbm_transaction**: One-to-many relationship (1:N)
   - A user can send and receive multiple transactions.
   - `Transaction.sender_id` and `Transaction.receiver_id` are foreign keys referencing `User.id`.

2. **pbm_user ↔ pbm_user_connections**: Many-to-many relationship (M:N)
   - A user can connect with multiple other users, and vice versa.
   - The `pbm_user_Connections` table represents this relationship, where each entry contains a pair of `user_id` and `connection_id` referencing the `User.id`.

### **Foreign Keys and Integrity Constraints**
- The foreign keys ensure referential integrity between the tables:
  - `sender_id` and `receiver_id` in the **pbm_transaction** table refer to `User.id`.
  - `user_id` and `connection_id` in the **pbm_user_connections** table refer to `User.id`.
- The primary key for **pbm_user_connections** is a composite key consisting of `user_id` and `connection_id`.

## **Database Constraints**

The following constraints have been applied to the database to maintain data integrity:

1. **Unique Email**:

   Each user’s email address must be unique. This is enforced by a unique constraint on the `email` column in the **pbm_user** table.

2. **Unique Username**: 

   Each user’s username must be unique. This is enforced by a unique constraint on the `username` column in the **pbm_user** table.
   
3. **Positive Transaction Amount**:

   The amount of a transaction must always be positive. This constraint ensures that no transaction can have a negative or zero value in the **pbm_transaction** table.

4. **No Self-Connections**:

   A user cannot connect to themselves. This is ensured by a check constraint or validation logic to prevent entries where `user_id` is equal to `connection_id` in the **pbm_user_connections** table.

5. **Primary Keys**: 
   - The primary key for the **pbm_user** table is `id`.
   - The primary key for the **pbm_transaction** table is `id`.
   - The primary key for the **pbm_user_connections** table is the combination of `user_id` and `connection_id`.

6. **Foreign Keys**: 
   - In the **pbm_transaction** table, both `sender_id` and `receiver_id` are foreign keys referring to the **pbm_user** table.
   - The **pbm_user_connections** table contains foreign keys, `user_id` and `connection_id`, both referring to `User.id`.

## **Setup**

## Running the Application Locally

To run the application locally, you need to activate the `local` profile. The application uses two separate configuration files: `application-local.properties` and `application-prod.properties`.

### Steps:

1. **Activate the Local Profile**

   When starting the application, make sure to use the `local` profile to configure the application in development mode. This profile will create and use the H2 database.

2. **Encryption Key**

   The encryption key is used to encrypt the password of the user. This key is configured in the `application.properties` file.

   You need to configure one environment variable for the encryption key:

   - `ENCRYPTION_KEY`: The encryption key for the password encoder.

   Or it's possible to change the key in the `application.properties` file:

   ```properties
   encryption.key=your_encryption_key
   ```
3. **Using Test Users**

   To use test users with default credentials, you need to load the necessary test data and configure the encryption key. Here's how to set it up:

   - Load test data : 
   
   You need to ensure that the data-h2.sql file is loaded automatically when the application starts. This file contains predefined test data, including users with default passwords.
   To load the data, uncomment and configure the following line in the application.properties file:

   ```properties
   spring.sql.init.data-locations=classpath:/data-h2.sql
   ```
   - Configure the encryption key :
   
   For testing purposes, you can use the following encryption key:
   ```properties
   encryption.key=ThisIsASecureKeyForProtectPassword
   ```
   This key will be used to encode the passwords for test users. By default, the password for all test users is set to 123.

   Now, once the application starts, the test data will be loaded, and users will be able to log in with the default password 123.

3. **Start the Application** 

   You can start the application with the `local` profile by running the following command:

   ```bash
   java -Dspring.profiles.active=local -jar PayMyBuddy.jar
   ```

## Running the Application in Production

To run the application in a production environment, follow the steps below:

### Steps:

1. **Create the Database**  
   Before starting the application, ensure that a MySQL database named `paymybuddy` is created.

2. **Set Environment Variables**  
   You need to configure two environment variables on your machine to provide the database connection credentials:
   - `DB_USERNAME`: The username for the database.
   - `DB_PASSWORD`: The password associated with the username.

   Additionally, you need to configure one environment variable for the encryption key:
   - `ENCRYPTION_KEY`: The encryption key for the password encoder.

   These environment variables are required to ensure both secure database connections and proper encryption for sensitive data within the application.

   Example for setting environment variables:

   - **Linux/macOS**:
     ```bash
     export DB_USERNAME=your_db_username
     export DB_PASSWORD=your_db_password
     export ENCRYPTION_KEY=your_secure_encryption_key
     ```

   - **Windows**:
     ```bash
     set DB_USERNAME=your_db_username
     set DB_PASSWORD=your_db_password
     set ENCRYPTION_KEY=your_secure_encryption_key
     ```

3. **Start the Application**  
   After the database is created and the environment variables are set, you can start the application. Spring Boot will automatically read these variables to establish the database connection and the encryption key and run the application properly.

   To start the application in production, use the following command:

   ```bash
   java -Dspring.profiles.active=prod -jar PayMyBuddy.jar
   ```