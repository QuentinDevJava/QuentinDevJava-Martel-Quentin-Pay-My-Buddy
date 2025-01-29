package com.openclassrooms.paymybuddy.exception;

/**
* Exception thrown when an error occurs during password encryption.
*/
public class PasswordEncryptionError extends RuntimeException {

 /** The constant {@code serialVersionUID}. */
 private static final long serialVersionUID = 1L;

 /**
  * Creates a new exception with the specified error message.
  * 
  * @param message The error message.
  */
 public PasswordEncryptionError(String message) {
     super(message);
 }
}
