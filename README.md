
# **Pay My Buddy Project - Database**

## **Overview**

This project is a **money transfer application** designed to allow users to send money to other users, manage their accounts, and establish connections with other users for easy transaction management.

This document outlines the database structure, the relationships between tables, and constraints to ensure data integrity within the system.

## **Database Structure**

The physical data model (MPD) describes the tables, columns, and relationships between the main entities in the application. The following tables are included in the database design:

![Diagramme](https://github.com/user-attachments/assets/04a94dcb-1629-4e7a-968a-60c3bc9e206e)


### 1. **User Table**
This table stores information about users in the application.
- **Columns**: 
  - `id` (Primary Key)
  - `username` (Unique)
  - `email` (Unique)
  - `password`

### 2. **Transaction Table**
This table records the details of money transactions.
- **Columns**:
  - `id` (Primary Key)
  - `sender_id` (Foreign Key, references `User(id)`)
  - `receiver_id` (Foreign Key, references `User(id)`)
  - `description`
  - `amount` (Positive Value)

### 3. **User_Connections Table**
This table represents the **many-to-many relationship** between users, allowing them to connect with each other.
- **Columns**:
  - `user_id` (Foreign Key, references `User(id)`)
  - `connection_id` (Foreign Key, references `User(id)`)
  - **Primary Key**: Combination of `user_id` and `connection_id`

## **Relationships Between Tables**

The key relationships between the tables are outlined below:

1. **User ↔ Transaction**: One-to-many relationship (1:N)
   - A user can send and receive multiple transactions.
   - `Transaction.sender_id` and `Transaction.receiver_id` are foreign keys referencing `User.id`.

2. **User ↔ User_Connections**: Many-to-many relationship (M:N)
   - A user can connect with multiple other users, and vice versa.
   - The `User_Connections` table represents this relationship, where each entry contains a pair of `user_id` and `connection_id` referencing the `User.id`.

### **Foreign Keys and Integrity Constraints**
- The foreign keys ensure referential integrity between the tables:
  - `sender_id` and `receiver_id` in the **Transaction** table refer to `User.id`.
  - `user_id` and `connection_id` in the **User_Connections** table refer to `User.id`.
- The primary key for **User_Connections** is a composite key consisting of `user_id` and `connection_id`.

## **Database Constraints**

The following constraints have been applied to the database to maintain data integrity:

1. **Unique Email**: Each user’s email address must be unique. This is enforced by a unique constraint on the `email` column in the **User** table.
   
2. **Positive Transaction Amount**: The amount of a transaction must always be positive. This constraint ensures that no transaction can have a negative or zero value in the **Transaction** table.

3. **No Self-Connections**: A user cannot connect to themselves. This is ensured by a check constraint or validation logic to prevent entries where `user_id` is equal to `connection_id` in the **User_Connections** table.

4. **Primary Keys**: 
   - The primary key for the **User** table is `id`.
   - The primary key for the **Transaction** table is `id`.
   - The primary key for the **User_Connections** table is the combination of `user_id` and `connection_id`.

5. **Foreign Keys**: 
   - In the **Transaction** table, both `sender_id` and `receiver_id` are foreign keys referring to the **User** table.
   - The **User_Connections** table contains foreign keys, `user_id` and `connection_id`, both referring to `User.id`.

## **Development Setup**

To run the application locally, follow the steps below:
