# Project-4-
Run with the following files:
  - Main.java
  - User.java
  - UserBase.java
  - Customer.java
  - Seller.java
  - Store.java
  - Product.java
  - NotAProductException.java
  
Data for the economy is split into three files whose values are separated by semicolons then commas:
  - Accounts.txt
    - [Email]; [Password]; [Nickname]; [Account Type]; +
    - If the account is a Customer:
      - [Balance]; [Purchase History]
      - Example: chen3927@purdue.edu; 12345; Willie; customer; 132.43; Rope, Stapler, Chair, Shovel
    - If the account is a Seller:
      - [Stores owned by the seller];
      - Example: chen3927@purdue.edu; 12345; Willie; seller; Follett's, Macy's, Target
      
  - Stores.txt
    - [Name of the Store]; [Products of the Store]; [Total items sold by the store]; [Customers that purchased a product from the store]
    - Example: Follett's; Stapler, Chair, Rope; 345; Willie
    
  - Products.txt
    - [Name of the Product]; [Name of the product's store]; [Boolean for if product is on sale]; [Description of Product]; [Quantity of product in stock]; [Price of product]; [Number of Products sold thus far]
     - Example: Stapler; Follett's; true; Staples paper; 80; 8.99; 5

A detailed description of each class. This should include the functionality included in the class, the testing done to verify it works properly, and its relationship to other classes in the project. 

# Contributions:
A list of who submitted which parts of the assignment on Brightspace and Vocareum. 
For example: Student 1 - Submitted Report on Brightspace. Student 2 - Submitted Vocareum workspace.


ERASE THIS LINE IF YOU SEE IT
