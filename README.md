# Project-4
Run with the following files:
  - Client.java
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

  # Client.java
    - Contains all the GUI
    - Connects with the server (Main.java)
    - Provides the user with a way to interact with the marketplace through simple and complex GUI.
    - Sends data from the user to the server to be processed.
    - Using the Client, users can choose to cfreate an account or log into an existing one.
    - After logging in, the class will provide the user with different options based on their role (Seller/Customer).
  # Testing.md
    - contains the test cases that were performed
  # Main.java
    - Receives data from the client class and processes it, communicates with the client class to show user the information they request.
    - This class calls on methods from other classes. It is also responsible for saving any user details, products, stores, etc 
    into text files, in order to save that information for when the user logs out of the program. This class will read from those 
    text files and edit them when needed, to keep the most up to date marketplace information.
  # User.java
    - Interface for returning the email, nickname, password and role of a user.
  # UserBase.java
    - Abstract class that implements user.
    - Contains methods for setting and returning parameters of a user.
  # Customer.java
    - Class that extends UserBase, contains functionality associated with a customer user.
    - This includes methods for viewing the marketplace, the dashboard and making a purchase.
  # Seller.java
    - Class that extends UserBase, has functionality for sellers.
    - This includes methods for the creation of stores, viewing the dashboard of stores that a seller has.
  # Store.java
    - A class for storing a list of products from the store.
    - Has methods for creating/adding a product, editing or removing products from the store.
    - Stores a list of customers of that store.
  # Product.java
    - Class that stores all parameters associated with a product, such as its name, price, quantity, etc.
    - Contains the methods for returning the product as it would appear in the marketplace, and the product desctription page.
    - Contains code that provides sellers with the option to hold a sale until a quantity of the product is sold. 
  # NotAProductException.java
    - Class that checks whether a product that was created/added by a seller contains all needed parameters.


# Contributions:
- Pranati Patchigolla - Submitted Report on Brightspace.
- Yixun Lu - Submitted Presentation
- Willie Chen - Submitted Vocareum workspace.

