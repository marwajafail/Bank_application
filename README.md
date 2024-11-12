# Project1-Bank Application
Design a file database for ACME Bank and write the entire Java program using OOP principles, file handling, exception handling, and unit testing. 
## List technologies used.<br/>

* Object-Oriented Programming (OOP): like classes, objects, encapsulation, and inheritance.
* Constructor: Used for initializing object states.
* ArrayList and List.
* Static Variables and Constants: static final variables.
* Conditional Statements (if-else)<br/>
* Output (System.out.println): Used for displaying messages and information during account transactions.
* Setter and Getter Methods: Used to encapsulate and manage access.
* Java Streams: Utilized to filter and find elements
* Exception Handling: Basic error handling through conditional statements for authentication checks.
* File Handling (Input/Output): To read from and write to text files (BufferedReader, FileReader, BufferedWriter, FileWriter).
* Encryption: Password encryption using the org.mindrot.jbcrypt.BCrypt library (hashpw, checkpw) for secure storage and verification of passwords.
* Boolean Logic: For various conditional checks .
* Inheritance: Inherits properties and methods from the UserAccount superclass, enhancing the functionality specific to a custome and Banker.
* Random Number Generation: Utilized java.util.Random for generating random card numbers.
* Switch Statement: Used to set different limits based on the cardType.



## Link to Trello (User Stories and planning).

Trello Link:

https://trello.com/invite/b/TNoSDUBq/ATTIe0c29b9f83dad5b1467db5a46f350905A6E77DE4/marwa-jafail

## Document your planning and development process, and your problem-solving strategy.

* Understanding Requirements: Begin by thoroughly reading and understanding the requirements provided for the banking system.
* Entity-Relationship Diagram (ERD): Draw an ERD to imagine the data entities (like User, Account, Transaction) and their relationships. 
* Design Classes: Start with basic classes based on the requirements and ERD.
* Work on Functional Requirements
  * Login Functionality: Implement user login and password handling.
  * Add New Customer
  * Account Transactions
  * Overdraft Protection

* Work on Advanced Features
  * Display Transaction Data 
  * Password Encryption
  * Debit Card Types 3 types of Mastercard


## List unresolved issues that could be addressed in future versions.
* Hash user password 

## Describe some of your favorite functions and how they work.
### Withdraw function 
The withdraw function allows a customer to take money out of their account
### How it works:
* It first checks if the account is active and if the withdrawal amount exceeds any set limits.
* Verifies the PIN with the debit card used for the transaction.
* Deducts the amount from the account balance and charges an overdraft fee if applicable.
* If the account goes into overdraft more than twice, it deactivates the account to prevent further withdrawals.


## ERD diagram.

![image](https://media.git.generalassemb.ly/user/53368/files/62fefc05-c7ee-4595-b255-6cceb3015e05)

