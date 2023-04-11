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
    - Provides the user with a menu from which they can choose to login or create an account.
    - After logging in, the class will provide the user with different options based on their role (Seller/Customer).
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

# Testing:
Since our program works with a main method, testing was done manually.
Below are some examples of the testing we have done. Each comma below indicates next input after the program prints a message.
  1: User creates a seller account, logs in and creates a store, closes program 
  - Program prints "Welcome!", "=====Login Menu=====\n1. Login\n2. Create An Account\n3. Exit"
  - User inputs: "2" (create account), "seller@purdue.edu", "password", "IUsucks", "1"
  - After this the program should print "Your new account has been successfully created!",
   "=====Login Menu=====\n1. Login\n2. Create An Account\n3. Exit"
  - User inputs "3", program prints "Goodbye!"
  
  
  2: Same user logs in and creates a store and a product
  - Program prints "Welcome!", "=====Login Menu=====\n1. Login\n2. Create An Account\n3. Exit"
  - User inputs "1", "seller@purdue.edu", "password"
  - Program prints """\n1. Create a store\n2. Select a store\n3. Display Dashboard\n4. Account Settings\n5. Logout"""
  - User inputs "1", "Purdue Bookstore"
  - Program prints "Your new store has been created!", """\n1. Create a store\n2. Select a store\n3. Display Dashboard\n4. Account Settings\n5. Logout"""
  - User inputs "2", "Purdue Bookstore"
  - Program prints "1. Create a product\n2. Edit a product\n3. Delete a product\n4. Choose a different store\n5. Return to Seller Menu"
  - User inputs "1", "CS book", "Book for learning computer science basics", "100", "10.99"
  - Program prints "Product has been added.", 
    "1. Create a product\n2. Edit a product\n3. Delete a product\n4. Choose a different store\n5. Return to Seller Menu"
  - User inputs "5", program """\n1. Create a store\n2. Select a store\n3. Display Dashboard\n4. Account Settings\n5. Logout""", user "5"
  - Program prints "=====Login Menu=====\n1. Login\n2. Create An Account\n3. Exit"
  - User inputs "3", program prints "Goodbye!"


  3: Another user creates a customer account and buys a product "CS book"
  - Program prints "Welcome!", "=====Login Menu=====\n1. Login\n2. Create An Account\n3. Exit"
  - User inputs "2", "customer@purdue.edu", "12345678", "student", "2"
  - After this the program should print "Your new account has been successfully created!",
   "=====Login Menu=====\n1. Login\n2. Create An Account\n3. Exit"
  - User inputs "1", "customer@purdue.edu", "12345678"
  - Program prints """ 1. Sort products\n2. Search for a product\n3.Display Dashboard\n4. Select a product\n5. Account Settings\n6. View purchase history\n7. Logout"""
  - User inputs "4", program: "Please enter the name of the product:", user: "shovel", program: 
  "The product does not exist!\n Returning to the marketplace..."
  - Program then prints """ 1. Sort products\n2. Search for a product\n3.Display Dashboard\n4. Select a product\n5. Account Settings\n6. View purchase history\n7. Logout"""
  -  User inputs "4", program: "Please enter the name of the product:", user: "CS book", program: "1. Purchase\n2. Return to market"
  -  User inputs "1", program prints "The product has been successfully purchased!\nReturning to the marketplace..."
  -  """ 1. Sort products\n2. Search for a product\n3.Display Dashboard\n4. Select a product\n5. Account Settings\n6. View purchase history\n7. Logout"""
  -  User inputs "7"
  - Program prints "=====Login Menu=====\n1. Login\n2. Create An Account\n3. Exit"
  - User inputs "3", program prints "Goodbye!"



# Contributions:
- YiXun Lu - Submitted Report on Brightspace.
- Willie Chen - Submitted Vocareum workspace.

