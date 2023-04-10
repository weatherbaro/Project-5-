import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
/**
 * Project 4 Marketplace
 *
 * @version 2023-04-07
 * @author Willie Chen
 */
public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        String choice; // stores user navigation inputs
        System.out.println("Welcome!");

        boolean logged = false; // status on whether user has logged in successfully
        boolean created; // checks if account creation was successful
        boolean on = true; // controls if program is running or not

        String email = "";
        String scenario = "";
        String sortBy = "";
        boolean sale = false;
        boolean searching = false;
        boolean first = true;
        String search = null;

        ArrayList<String> accountData = null; // contains all of account information
        ArrayList<Store> storeDirectory = getStores(); // gets all stores into one arraylist for menu to interact with
        ArrayList<Product> productDirectory = getProducts(); // gets all products into one arraylist
        ArrayList<Store> ownedStores = null;
        ArrayList<Product> ownedProducts = null;
        ArrayList<Product> purchaseHistory = null;

        while (on) {
            System.out.println("=====Login Menu=====\n1. Login\n2. Create An Account\n3. Exit"); // right now user is going to enter just email and password
            choice = scan.nextLine();

            if (choice.equals("1")) { // user chooses to login
                System.out.println("Enter your email:");
                email = scan.nextLine();
                System.out.println("Enter your password:");
                String password = scan.nextLine();

                if (trylogging(email, password)) {
                    logged = true;
                    accountData = getAccountInfo(email); // load account info into arraylist
                    scenario = "normal";
                    sortBy = "normal";
                }

                while (logged) {
                    if (accountData.get(3).equals("customer")) { // if the logged account is a customer
                        if (first) {
                            purchaseHistory = importPurchaseHistory(accountData.get(5));
                            first = !first;
                        }
                        Customer customer = new Customer(email, password, accountData.get(2),
                                Double.parseDouble(accountData.get(4)), purchaseHistory);
                        customer.displayMarket(productDirectory, scenario, sortBy, sale, searching, search);
                        System.out.println("""
                                1. Sort products
                                2. Search for a product
                                3. Display Dashboard
                                4. Select a product
                                5. Account Settings
                                6. View purchase history
                                7. Logout""");
                        choice = scan.nextLine();

                        switch (choice) {
                            case "1" -> {  // customer wants to sort the marketplace
                                System.out.println("""
                                        1. Sale filter
                                        2. Sort by ascending price
                                        3. Sort by descending price
                                        4. Sort by ascending quantity
                                        5. Sort by descending quantity
                                        6. Show normal marketplace
                                        7. Return""");
                                choice = scan.nextLine();
                                switch (choice) {
                                    case "1" -> sale = !sale;
                                    case "2" -> {
                                        scenario = "price";
                                        sortBy = "ascending";
                                    }
                                    case "3" -> {
                                        scenario = "price";
                                        sortBy = "descending";
                                    }
                                    case "4" -> {
                                        scenario = "quantity";
                                        sortBy = "ascending";
                                    }
                                    case "5" -> {
                                        scenario = "quantity";
                                        sortBy = "descending";
                                    }
                                    case "6" -> {
                                        scenario = "normal";
                                        sortBy = "normal";
                                        sale = false;
                                        searching = false;
                                    }
                                }
                            }
                            case "2" -> {
                                searching = true;
                                System.out.println("Please enter a search term:");
                                search = scan.nextLine();
                                System.out.println("Searching for products...");
                            }
                            case "3" -> {
                                boolean dashboard = true;
                                sortBy = "normal";
                                while (dashboard) {
                                    Customer.displayDashboard(storeDirectory, sortBy, purchaseHistory);
                                    System.out.println("1. Sort dashboard\n2. Return to Marketplace");
                                    choice = scan.nextLine();
                                    if (choice.equals("1")) {
                                        System.out.println("""
                                                1. Sort by ascending sales
                                                2. Sort by descending sales
                                                3. Return to dashboard menu""");
                                        choice = scan.nextLine();
                                        if (choice.equals("1")) {
                                            sortBy = "ascending";
                                        } else if (choice.equals("2")) {
                                            sortBy = "descending";
                                        } else {
                                            sortBy = "normal";
                                        }
                                    } else {
                                        dashboard = false;
                                        scenario = "normal";
                                        sortBy = "normal";
                                    }
                                }
                            }
                            case "4" -> {
                                System.out.println("Please enter the name of the product:");
                                String productname = scan.nextLine();
                                boolean exists = getProductPage(productDirectory, productname);
                                if (!exists) {
                                    System.out.println("The product does not exist!\n Returning to the marketplace...");
                                } else {
                                    System.out.println("1. Purchase\n2. Return to market");
                                    choice = scan.nextLine();
                                    if (choice.equals("1")) {
                                        boolean purchased = Customer.purchase(productDirectory, storeDirectory,
                                                productname, purchaseHistory, customer.getNickname());
                                        if (purchased) {
                                            System.out.println("The product has been successfully purchased!\nReturning to the marketplace...");
                                        } else {
                                            System.out.println("The product could not be purchased!\n Returning to the marketplace...");
                                        }
                                    } else {
                                        System.out.println("Returning to the marketplace...");
                                    }
                                }
                            }
                            case "5" -> {
                                boolean settings = true; // status on whether settings page is running or not
                                while (settings) {
                                    System.out.println("1. Delete Account\n2. Edit Account\n3. Return");
                                    choice = scan.nextLine();
                                    switch (choice) {
                                        case "1":
                                            if (deleteAccount(email)) {
                                                System.out.println("Your account has been deleted!\nLogging out...");
                                                logged = false;
                                            } else {
                                                System.out.println("Your account couldn't be deleted!");
                                            }
                                            break;
                                        case "2":
                                            String edit = "";
                                            String check = "";
                                            int checking;
                                            System.out.println("What would you like to change?");
                                            System.out.println("1. Email\n2. Password\n3. Nickname");
                                            choice = scan.nextLine();
                                            checking = Integer.parseInt(choice) - 1;
                                            switch (choice) {
                                                case "1" -> {
                                                    System.out.println("Enter the updated email:");
                                                    edit = scan.nextLine();
                                                    check = email;
                                                }
                                                case "2" -> {
                                                    System.out.println("Enter the updated password:");
                                                    edit = scan.nextLine();
                                                    System.out.println(accountData.get(1));
                                                    check = accountData.get(1);
                                                }
                                                case "3" -> {
                                                    System.out.println("Enter the new nickname:");
                                                    edit = scan.nextLine();
                                                    System.out.println(accountData.get(2));
                                                    check = accountData.get(2);
                                                }
                                            }
                                            if (editAccount(email, check, edit, checking)) {
                                                System.out.println("Your account has been updated!");
                                            } else {
                                                System.out.println("There was a problem with updating your account.");
                                            }
                                            break;
                                        case "3":
                                            settings = false;
                                            break;
                                    }
                                }
                            }
                            case "6" -> {
                                boolean viewingHistory = true;
                                System.out.println("Showing your purchase history...");
                                System.out.println("====================");
                                while (viewingHistory) {
                                    for (Product p : purchaseHistory) {
                                        System.out.printf("Name: %s\nDescription: %s\nPurchased from %s at $%.2f\n",
                                                p.getName(), p.getDescription(), p.getStoreName(), p.getPrice());
                                    }
                                    System.out.println("====================");
                                    System.out.println("1. Export Purchase history\n2. Return to marketplace");
                                    choice = scan.nextLine();
                                    if (choice.equals("1")) {
                                        boolean exported = exportPurchaseHistory(customer.getPurchaseHistory(), accountData.get(2));
                                        if (exported) {
                                            System.out.println("Your purchase history has been exported.");
                                        } else {
                                            System.out.println("There was a problem exporting your purchase history.");
                                        }
                                    } else {
                                        viewingHistory = false;
                                    }
                                }
                            }
                            case "7" -> logged = false;
                        }
                        purchaseHistory = customer.getPurchaseHistory();
                    } else if (accountData.get(3).equals("seller")) {
                        if (first) {
                            first = false;
                            ownedStores = getSellerStores(storeDirectory, accountData.get(4));
                            ownedProducts = getOwnedProducts(ownedStores);
                        }
                        String nickname = accountData.get(2);
                        Seller seller = new Seller(email, password, nickname, ownedStores);
                        System.out.println("""
                                1. Create a store
                                2. Select a store
                                3. Display Dashboard
                                4. Account Settings
                                5. Logout""");
                        choice = scan.nextLine();
                        switch (choice) {
                            case "1" -> {
                                System.out.println("Enter the name for the store");
                                String storeName = scan.nextLine();
                                Store store = new Store(storeName, 0, new ArrayList<>(), new ArrayList<>());
                                seller.addStore(store);
                                System.out.println("Your new store has been created!");
                            }
                            case "2" -> {
                                System.out.println("Enter the name of the store:");
                                String selectedStore = scan.nextLine();
                                boolean productMenu = true;
                                while (productMenu) {
                                    System.out.println("""
                                            1. Create a product
                                            2. Edit a product
                                            3. Delete a product
                                            4. Choose a different store
                                            5. Return to Seller Menu""");
                                    choice = scan.nextLine();
                                    switch (choice) {
                                        case "1" -> {
                                            System.out.println("Enter the name of the product:");
                                            String pName = scan.nextLine();
                                            System.out.println("Enter the description of the product:");
                                            String des = scan.nextLine();
                                            System.out.println("Enter the quantity of the product:");
                                            int quantity = scan.nextInt();
                                            scan.nextLine();
                                            System.out.println("Enter the price of the product:");
                                            double price = scan.nextDouble();
                                            scan.nextLine();
                                            Product toBeAdded = new Product(pName, selectedStore, false, des, quantity, price, 0);
                                            ownedStores = seller.getStores();
                                            ownedProducts = getOwnedProducts(ownedStores);
                                            for (Store s : seller.getStores()) {
                                                if (selectedStore.equals(s.getName())) {
                                                    try {
                                                        s.addProduct(toBeAdded);
                                                        System.out.println("Product has been added.");
                                                    } catch (NotAProductException e) {
                                                        System.out.println(e);
                                                    }
                                                }
                                            }
                                        }
                                        case "2" -> {
                                            System.out.println("Enter the name of the product:");
                                            String pName = scan.nextLine();
                                            Product selectedProduct = null;
                                            for (Product p : ownedProducts) {
                                                if (pName.equalsIgnoreCase(p.getName())) {
                                                    selectedProduct = p;
                                                }
                                            }
                                            System.out.println("What would you like to change about the product?");
                                            System.out.println("1.Name\n2. Description\n3. Quantity\n4. Price");
                                            choice = scan.nextLine();
                                            switch (choice) {
                                                case "1" -> {
                                                    System.out.println("Enter the new name for the product:");
                                                    String newName = scan.nextLine();
                                                    selectedProduct.setName(newName);
                                                }
                                                case "2" -> {
                                                    System.out.println("Enter the new description for the product:");
                                                    String newDes = scan.nextLine();
                                                    selectedProduct.setDescription(newDes);
                                                }
                                                case "3" -> {
                                                    System.out.println("Enter the new quantity for the product:");
                                                    int newQuantity = scan.nextInt();
                                                    scan.nextLine();
                                                    selectedProduct.setQuantity(newQuantity);
                                                }
                                                case "4" -> {
                                                    System.out.println("Enter the new price for the product:");
                                                    double newPrice = scan.nextDouble();
                                                    scan.nextLine();
                                                    selectedProduct.setPrice(newPrice);
                                                }
                                            }
                                            System.out.println("The product has been edited.");
                                        }
                                        case "3" -> {
                                            System.out.println("Enter the name of the product to be deleted:");
                                            String pName = scan.nextLine();
                                            for (Store s : ownedStores) {
                                                for (Product p : s.getProducts()) {
                                                    if (pName.equalsIgnoreCase(p.getName())) {
                                                        s.removeProduct(p);
                                                    }
                                                }
                                            }
                                            System.out.println("The product has been deleted.");
                                        }
                                        case "4" -> {
                                            System.out.println("Enter the name of the store:");
                                            selectedStore = scan.nextLine();
                                        }
                                        default -> productMenu = false;
                                    }
                                }
                            }
                            case "3" -> {
                                boolean dash = true;
                                sortBy = "normal";
                                while (dash) {
                                    seller.displayDashboard(ownedStores, sortBy);
                                    System.out.println("1. Sort by Ascending Sales\n2. Sort by Descending Sales\n3. Return to Seller Menu");
                                    choice = scan.nextLine();
                                    if (choice.equals("1")) {
                                        sortBy = "ascending";
                                    } else if (choice.equals("2")) {
                                        sortBy = "descending";
                                    } else {
                                        dash = false;
                                    }
                                }
                            }
                            case "4" -> {
                                boolean settings = true; // status on whether settings page is running or not
                                while (settings) {
                                    System.out.println("1. Delete Account\n2. Edit Account\n3. Return");
                                    choice = scan.nextLine();
                                    if (choice.equals("1")) {
                                        if (deleteAccount(email)) {
                                            System.out.println("Your account has been deleted!\nLogging out...");
                                            logged = false;
                                        } else {
                                            System.out.println("Your account couldn't be deleted!");
                                        }
                                    } else if (choice.equals("2")) {
                                        String edit = "";
                                        String check = "";
                                        int checking;
                                        System.out.println("What would you like to change?");
                                        System.out.println("1. Email\n2. Password\n3. Nickname");
                                        choice = scan.nextLine();
                                        checking = Integer.parseInt(choice) - 1;
                                        if (choice.equals("1")) {
                                            System.out.println("Enter the updated email:");
                                            edit = scan.nextLine();
                                            check = email;
                                        } else if (choice.equals("2")) {
                                            System.out.println("Enter the updated password:");
                                            edit = scan.nextLine();
                                            check = accountData.get(1);
                                        } else if (choice.equals("3")) {
                                            System.out.println("Enter the new nickname:");
                                            edit = scan.nextLine();
                                            check = accountData.get(2);
                                        }
                                        if (editAccount(email, check, edit, checking)) {
                                            System.out.println("Your account has been updated!");
                                        } else {
                                            System.out.println("There was a problem with updating your account.");
                                        }
                                    } else if (choice.equals("3")) {
                                        settings = false;
                                    }
                                }
                            }
                            case "5" -> logged = false;
                        }
                        ownedStores = seller.getStores();
                        ownedProducts = getOwnedProducts(ownedStores);
                    }
                }
            } else if (choice.equals("2")) { // Account Creation Section
                System.out.println("Enter your email:");
                email = scan.nextLine();
                System.out.println("Enter your password:");
                String password = scan.nextLine();
                System.out.println("Enter a nickname for yourself:");
                String nickname = scan.nextLine();
                System.out.println("1. Seller\n2. Customer");
                String type = scan.nextLine();
                if (type.equals("1")) {
                    type = "seller";
                } else {
                    type = "customer";
                }
                created = createAccount(email, password, nickname, type);
                if (created) {
                    System.out.println("Your new account has been successfully created!");
                } else {
                    System.out.println("There was a problem with creating yuor account.");
                }
            } else { // EXIT option
                if (!(accountData == null)) {
                    if (accountData.get(3).equals("seller")) {
                        updateSellerAccount(email, ownedStores);
                        updateSellerStores(ownedStores);
                        updateSellerProducts(ownedProducts);
                    } else if (accountData.get(3).equals("customer")) {
                        updateCustomerHistory(email, purchaseHistory);
                        updateCustomerProducts(productDirectory);
                        updateCustomerStores(storeDirectory);
                    }
                }
                System.out.println("Goodbye!");
                on = false;
            }
        }
    }

    private static void updateCustomerStores(ArrayList<Store> stores) {
        try {
            File originalStores = new File("Stores.txt");
            File newStores = new File("newStores.txt");
            BufferedReader r = new BufferedReader(new FileReader(originalStores));
            BufferedWriter w = new BufferedWriter(new FileWriter(newStores));
            for (Store s : stores) {
                String storeName = s.getName();
                String productNames = "";
                ArrayList<Product> storeProducts = s.getProducts();
                for (int i = 0; i < storeProducts.size(); i++) {
                    productNames += storeProducts.get(i).getName();
                    if (i < storeProducts.size() - 1) {
                        productNames += ", ";
                    }
                }
                int sales = s.getsales();
                String customernames = "";
                ArrayList<String> c = s.getCustomers();
                if (c.isEmpty()) {
                    customernames = "none";
                } else {
                    for (int i = 0; i < c.size(); i++) {
                        customernames += c.get(i);
                        if (i < c.size() - 1) {
                            customernames += ", ";
                        }
                    }
                }
                w.write(String.format("%s; %s; %d; %s\n", storeName, productNames, sales, customernames));
            }
            w.close();
            r.close();
            originalStores.delete();
            newStores.renameTo(originalStores);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error updating store data!");
        }
    }

    private static void updateCustomerProducts(ArrayList<Product> products) {
        try {
            FileWriter fw = new FileWriter("Products.txt", false);
            PrintWriter pw = new PrintWriter(fw, false);
            pw.flush();

            for (Product p : products) {
                String productName = p.getName();
                String StoreName = p.getStoreName();
                boolean onSale = p.isOnSale();
                String description = p.getDescription();
                int quantity = p.getQuantity();
                double price = p.getPrice();
                int sold = p.getQuantitySold();
                pw.write(String.format("%s; %s; %s; %s; %d; %.2f; %d\n",
                        productName, StoreName, onSale, description, quantity, price, sold));
            }
            pw.close();
            fw .close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error updating product data.");
        }
    }

    private static void updateCustomerHistory(String email, ArrayList<Product> history) {
        try {
            File originalAccount = new File("Accounts.txt"); // original account file
            File newAccount = new File("newAccounts.txt"); // new account file without the current account information
            BufferedReader r = new BufferedReader(new FileReader(originalAccount));
            BufferedWriter w = new BufferedWriter(new FileWriter(newAccount));
            String line;
            String[] data;
            while ((line = r.readLine()) != null) {
                data = line.split("; ");
                if (data[0].equals(email)) {
                    String purchased = "";
                    for (int i = 0; i < history.size(); i++) {
                        purchased += history.get(i).getName();
                        if (i < history.size() - 1) {
                            purchased += ", ";
                        }
                    }
                    data[5] = purchased;
                    String newline = String.join("; ", data);
                    w.write(newline + "\n");
                } else {
                    w.write(line + "\n");
                }
            }
            w.close();
            r.close();
            originalAccount.delete();
            newAccount.renameTo(originalAccount);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error updating customer purchase history!");
        }
    }

    private static void updateSellerProducts(ArrayList<Product> products) {
        try {
            File originalProducts = new File("Products.txt");
            File newProducts = new File("newProducts.txt");
            BufferedReader r = new BufferedReader(new FileReader(originalProducts));
            BufferedWriter w = new BufferedWriter(new FileWriter(newProducts));
            String line;
            String[] data;
            while ((line = r.readLine()) != null) {
                data = line.split("; ");
                for (Product p : products) {
                    if (data[0].equalsIgnoreCase(p.getName())) {
                        String productName = p.getName();
                        String StoreName = p.getStoreName();
                        boolean onSale = p.isOnSale();
                        String description = p.getDescription();
                        int quantity = p.getQuantity();
                        double price = p.getPrice();
                        int sold = p.getQuantitySold();
                        w.write(String.format("%s; %s; %s; %s; %d; %.2f, %d\n",
                                productName, StoreName, onSale, description, quantity, price, sold));
                    } else {
                        w.write(line + "\n");
                    }
                }
            }
            r.close();
            w.close();
            originalProducts.delete();
            newProducts.renameTo(originalProducts);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error updating product data!");
        }
    }

    private static void updateSellerStores(ArrayList<Store> stores) {
        try {
            File originalStore = new File("Stores.txt");
            File newStore = new File("newStores.txt");
            BufferedReader r = new BufferedReader(new FileReader(originalStore));
            BufferedWriter w = new BufferedWriter(new FileWriter(newStore));
            String line;
            String[] data;
            while ((line = r.readLine()) != null) {
                data = line.split("; ");
                for (Store s : stores) {
                    String storeName = s.getName();
                    String productNames = "";
                    ArrayList<Product> storeProducts = s.getProducts();
                    for (int i = 0; i < storeProducts.size(); i++) {
                        productNames += storeProducts.get(i).getName();
                        if (i < storeProducts.size() - 1) {
                            productNames += ", ";
                        }
                    }
                    int sales = s.getsales();
                    String customernames = "none";
                    if (s.getCustomers().size() > 0) {
                        ArrayList<String> c = s.getCustomers();
                        for (int i = 0; i < c.size() - 1; i++) {
                            customernames += c.get(i);
                            if (i < c.size() - 1) {
                                customernames += ", ";
                            }
                        }
                    }
                    if (storeName.equals(data[0])) {
                        w.write(String.format("%s; %s; %d; %s\n", storeName, productNames, sales, customernames));
                    } else {
                        w.write(line + "\n");
                    }
                }
            }
            for (Store s : stores) {
                String storeName = s.getName();
                String productNames = "";
                ArrayList<Product> storeProducts = s.getProducts();
                for (int i = 0; i < storeProducts.size(); i++) {
                    productNames += storeProducts.get(i).getName();
                    if (i < storeProducts.size() - 1) {
                        productNames += ", ";
                    }
                }
                int sales = s.getsales();
                String customernames = "none";
                if (s.getCustomers().size() > 0) {
                    ArrayList<String> c = s.getCustomers();
                    for (int i = 0; i < c.size() - 1; i++) {
                        customernames += c.get(i);
                        if (i < c.size() - 1) {
                            customernames += ", ";
                        }
                    }
                }
                if (isNew(storeName, "store")) {
                    w.write(String.format("%s; %s; %d; %s\n", storeName, productNames, sales, customernames));
                }
            }
            r.close();
            w.close();
            originalStore.delete();
            newStore.renameTo(originalStore);
        } catch(Exception e) {
            e.printStackTrace();
            System.out.println("Error updating store data!");
        }
    }

    private static void updateSellerAccount(String email, ArrayList<Store> stores) {
        try {
            File originalAccount = new File("Accounts.txt"); // original account file
            File newAccount = new File("newAccounts.txt"); // new account file without the current account information
            BufferedReader r = new BufferedReader(new FileReader(originalAccount));
            BufferedWriter w = new BufferedWriter(new FileWriter(newAccount));
            String line;
            String[] data;
            while ((line = r.readLine()) != null) {
                data = line.split("; ");
                if (data[0].equals(email)) {
                    String ownedStores = "";
                    for (int i = 0; i < stores.size(); i++) {
                        ownedStores += stores.get(i).getName();
                        if (i < stores.size() - 1) {
                            ownedStores += ", ";
                        }
                    }
                    data[4] = ownedStores;
                    String newaccount = String.join("; ", data);
                    w.write(newaccount + "\n");
                } else {
                    w.write(line + "\n");
                }
            }
            w.close();
            r.close();
            originalAccount.delete();
            newAccount.renameTo(originalAccount);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error updating account data.");
        }
    }

    // method that tries logging user in with provided email and password, currently it will iterate through file lines of data and return a boolean if user exists or not
    public static boolean trylogging(String email, String password) {
        try {
            BufferedReader bfr = new BufferedReader(new FileReader("Accounts.txt"));
            String line;
            String[] accountData;
            while ((line = bfr.readLine()) != null) {
                accountData = line.split("; ");
                if (accountData[0].equals(email) && accountData[1].equals(password)) {
                    System.out.println("Login Successful!");
                    System.out.printf("Welcome back, %s\n", accountData[2]);
                    bfr.close();
                    return true;
                }
            }
            bfr.close();
            System.out.println("There was an error logging in!");
            return false;
        } catch (Exception e) {
            System.out.println("There was an error logging in!");
            return false;
        }
    }

    // method of account deletion dependent solely on email, on the assumption same email cannot have multiple accounts
    public static boolean deleteAccount(String email) {
        try {
            File original = new File("Accounts.txt"); // original account file
            File neww = new File("new.txt"); // new account file without the current account information

            BufferedReader r = new BufferedReader(new FileReader(original));
            BufferedWriter w = new BufferedWriter(new FileWriter(neww));

            String line;
            while((line = r.readLine()) != null) {
                if(!(line.split("; ")[0].equals(email))) {
                    w.write(line + "\n");
                }
            }
            w.close();
            r.close();
            original.delete();
            return (neww.renameTo(original));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // method for account creation
    public static boolean createAccount(String email, String password, String nickname, String type) {
        try {
            BufferedWriter bfw = new BufferedWriter(new FileWriter("Accounts.txt", true));
            bfw.write(String.format("%s; %s; %s; %s", email, password, nickname, type));
            if (type.equals("customer")) {
                bfw.write(String.format("; %.2f; %s\n", 0.0, "none"));
            } else if (type.equals("seller")) {
                bfw.write(String.format("; %s\n", "none"));
            }
            bfw.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // account editing
    // check is the data to be edited
    // checking is the index of that data
    // always checks matching email first
    public static boolean editAccount(String email, String check, String edit, int checking) {
        try {
            File original = new File("Accounts.txt"); // original account file
            File neww = new File("new.txt"); // new account file without the current account information

            BufferedReader r = new BufferedReader(new FileReader(original));
            BufferedWriter w = new BufferedWriter(new FileWriter(neww));

            String line;
            String[] accountData;

            while ((line = r.readLine()) != null) {
                accountData = line.split("; ");
                if (accountData[0].equals(email) && accountData[checking].equals(check)) {
                    System.out.println(accountData[checking]);
                    System.out.println(check);
                    accountData[checking] = edit;
                    String newline = String.join("; ", accountData);
                    w.write(newline + "\n");
                } else {
                    w.write(line + "\n");
                }
            }
            w.close();
            r.close();
            original.delete();
            return (neww.renameTo(original));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // separates and stores all data on account line into an arraylist
    public static ArrayList<String> getAccountInfo(String email) {
        ArrayList<String> accountInfo = new ArrayList<>();
        try {
            BufferedReader bfr = new BufferedReader(new FileReader("Accounts.txt"));
            String line;
            String[] accountData;
            while ((line = bfr.readLine()) != null) {
                accountData = line.split("; ");
                if (accountData[0].equals(email)) {
                    for (String data: accountData) {
                        accountInfo.add(data);
                    }
                }
            }
            bfr.close();
            return accountInfo;
        } catch (Exception e) {
            return null;
        }
    }

    // turns purchase history from account file into an arraylist to put into the customer class
    public static ArrayList<Product> importPurchaseHistory(String purchases) {

        ArrayList<Product> history = new ArrayList<>();
        if (purchases.equals("none")) {
            return history;
        }
        String[] purchasearr = purchases.split(", ");

        for (String purchaseName : purchasearr) {
            try {
                File productfile = new File("Products.txt"); // product data file
                BufferedReader productreader = new BufferedReader(new FileReader(productfile));
                String productline;
                String[] productData;
                while ((productline = productreader.readLine()) != null) {
                    productData = productline.split("; ");
                    String productname = productData[0];
                    if (purchaseName.equalsIgnoreCase(productname)) {
                        String store = productData[1];
                        boolean onSale = Boolean.parseBoolean(productData[2]);
                        String description = productData[3];
                        int quantity = Integer.parseInt(productData[4]);
                        double price = Double.parseDouble(productData[5]);
                        int sold = Integer.parseInt(productData[6]);
                        history.add(new Product(productname, store, onSale, description, quantity, price, sold));
                    }
                }
                productreader.close();
            } catch (Exception e) {
                System.out.println("Error getting customer purchase history!");
            }
        }
        return history;
    }

    public static boolean exportPurchaseHistory(ArrayList<Product> purchaseHistory, String nickname) {
        try {
            String filename = String.format("%s's Purchase History.txt", nickname);
            File historyfile = new File(filename);
            BufferedWriter w = new BufferedWriter(new FileWriter(historyfile));

            for (Product p: purchaseHistory) {
                String name = p.getName();
                String storeName = p.getStoreName();
                double price = p.getPrice();
                w.write(String.format("%s, %s, $%.2f\n", name, storeName, price));
            }
            w.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static ArrayList<Store> getStores() {
        try {
            File storefile = new File("Stores.txt"); // store data file
            BufferedReader storereader = new BufferedReader(new FileReader(storefile));
            String storeline;
            String[] storeData;

            String storeName;
            int sales;

            ArrayList<Store> stores = new ArrayList<>();
            String[] productarr;
            ArrayList<Product> products;
            // initializing store arraylist
            while ((storeline = storereader.readLine()) != null) {
                storeData = storeline.split("; ");
                storeName = storeData[0];
                productarr = storeData[1].split(", ");
                products = new ArrayList<>();
                for (String product : productarr) {
                    File productfile = new File("Products.txt"); // product data file
                    BufferedReader productreader = new BufferedReader(new FileReader(productfile));
                    String productline;
                    String[] productData;
                    while ((productline = productreader.readLine()) != null) {
                        productData = productline.split("; ");
                        String productname = productData[0];
                        if (product.equalsIgnoreCase(productname)) {
                            String store = productData[1];
                            boolean onSale = Boolean.parseBoolean(productData[2]);
                            String description = productData[3];
                            int quantity = Integer.parseInt(productData[4]);
                            double price = Double.parseDouble(productData[5]);
                            int sold = Integer.parseInt(productData[6]);
                            products.add(new Product(productname, store, onSale, description, quantity, price, sold));
                        }
                    }
                    productreader.close();
                }
                sales = Integer.parseInt(storeData[2]);
                ArrayList<String> customers = new ArrayList<>();
                String[] c = storeData[3].split(", ");
                if (!c[0].equals("none")) {
                    for (String s : c) {
                        customers.add(s);
                    }
                }
                stores.add(new Store(storeName, sales, products, customers));
            }
            storereader.close();
            return stores;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("There was an error getting store information!");
            return null;
        }
    }

    public static ArrayList<Store> getSellerStores(ArrayList<Store> storeDirectory, String ownedStores) {
        ArrayList<Store> sellerStores = new ArrayList<>();
        if (ownedStores.equals("none")) {
            return sellerStores;
        }
        String[] owned = ownedStores.split(", ");
        for (String name: owned) {
            for (Store s : storeDirectory) {
                if (name.equalsIgnoreCase(s.getName())) {
                    sellerStores.add(s);
                }
            }
        }
        return sellerStores;
    }

    public static ArrayList<Product> getProducts() {
        try {
            File productfile = new File("Products.txt"); // product data file
            BufferedReader r = new BufferedReader(new FileReader(productfile));
            String line;
            String[] productData;

            String name;
            String store;
            String description;
            boolean onSale;
            int quantity;
            double price;
            int sold;

            ArrayList<Product> products = new ArrayList<>();
            while ((line = r.readLine()) != null) {
                productData = line.split("; ");
                name = productData[0];
                store = productData[1];
                onSale = Boolean.parseBoolean(productData[2]);
                description = productData[3];
                quantity = Integer.parseInt(productData[4]);
                price = Double.parseDouble(productData[5]);
                sold = Integer.parseInt(productData[6]);
                products.add(new Product(name, store, onSale, description, quantity, price, sold));
            }
            return products;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("There was an error getting store information!");
            return null;
        }
    }

    public static ArrayList<Product> getOwnedProducts(ArrayList<Store> ownedStores) {
        try {

            ArrayList<Product> products = new ArrayList<>();
            for (Store s: ownedStores) {
                for (Product p: s.getProducts()) {
                    products.add(p);
                }
            }
            return products;
        } catch (Exception e) {
            System.out.println("There was an error getting product information");
            return null;
        }
    }

    public static boolean getProductPage(ArrayList<Product> directory, String name) {
        for (Product p: directory) {
            if (name.equalsIgnoreCase(p.getName())) {
                System.out.println(p.productPage());
                return true;
            }
        }
        return false;
    }

    // checks if product or store is new
    public static boolean isNew(String name, String type) {
        try {
            File f;
            if (type.equals("store")) {
                f = new File("Stores.txt");
            } else {
                f = new File("Products.txt");
            }
            BufferedReader r = new BufferedReader(new FileReader(f));
            String line;
            String[] data;
            while ((line = r.readLine()) != null) {
                data = line.split("; ");
                if (name.equalsIgnoreCase(data[0])) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
