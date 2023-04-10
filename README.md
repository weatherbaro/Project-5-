# Project-4
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

  # Main.java
    -
  # User.java
    -
  # UserBase.java
    -
  # Customer.java
    -
  # Seller.java
    -
  # Store.java
    - A class for storing a list of products from the store
    - Has methods for creating/adding a product, editing or removing products from the store.
    - Stores a list of customers of that store
  # Product.java
    - Class that stores all parameters associated with a product, such as its name, price, quantity, etc.
    - Contains the methods for returning the product as it would appear in the marketplace, and the product desctription page.
    - Contains code that provides sellers with the option to hold a sale until a quantity of the product is sold. 
  # NotAProductException.java
    - Class that checks whether a product that was created/added by a seller contains all needed parameters.

# Contributions:
- YiXun Lu - Submitted Report on Brightspace.
- Willie Chen - Submitted Vocareum workspace.

