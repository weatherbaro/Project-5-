import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Project 4 Marketplace
 *
 * @version 2023-04-07
 * @author Willie Chen, Yixun Lu
 */
public class Main implements Runnable{

    Socket socket;

    public Main(Socket socket) {
        this.socket = socket;
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1000);
        while (true) {
            Main main = new Main(serverSocket.accept());
            new Thread(main).start();
        }
    }

    private void updateCustomerStores(ArrayList<Store> stores) throws IOException {
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
            oos.writeObject("Error updating store data!");
        }
    }

    private void updateCustomerProducts(ArrayList<Product> products) throws IOException {
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
            oos.writeObject("Error updating product data.");
        }
    }

    private static void updateCustomerHistory(String email, ArrayList<Product> history) throws IOException {
        if (!history.isEmpty()) {
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
                oos.writeObject("Error updating customer purchase history!");
            }
        }
    }

    private static void updateSellerStores(ArrayList<Store> allStores, ArrayList<Store> ownedstores) throws IOException {
        try {
            FileWriter fw = new FileWriter("Stores.txt", false);
            PrintWriter pw = new PrintWriter(fw, false);
            pw.flush();
            for (Store s: allStores) {
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
                for (Store owned: ownedstores) {
                    if (owned.getName().equalsIgnoreCase(s.getName())) {
                        sales = owned.getsales();
                        productNames = "";
                        storeProducts = owned.getProducts();
                        for (int i = 0; i < storeProducts.size(); i++) {
                            productNames += storeProducts.get(i).getName();
                            if (i < storeProducts.size() - 1) {
                                productNames += ", ";
                            }
                        }
                        customernames = "none";
                        if (s.getCustomers().size() > 0) {
                            ArrayList<String> c = s.getCustomers();
                            for (int i = 0; i < c.size() - 1; i++) {
                                customernames += c.get(i);
                                if (i < c.size() - 1) {
                                    customernames += ", ";
                                }
                            }
                        }
                    }
                }
                pw.write(String.format("%s; %s; %d; %s\n", storeName, productNames, sales, customernames));
            }
            for (Store s: ownedstores) {
                boolean isNew = true;
                for (Store st: allStores) {
                    if (s.getName().equalsIgnoreCase(st.getName())) {
                        isNew = false;
                    }
                }
                if (isNew) {
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
                    pw.write(String.format("%s; %s; %d; %s\n", storeName, productNames, sales, customernames));
                }
            }
            pw.close();
            fw.close();
        } catch(Exception e) {
            e.printStackTrace();
            oos.writeObject("Error updating store data!");
        }
    }

    private static void updateSellerProducts(ArrayList<Product> allproducts, ArrayList<Product> ownedproducts, ArrayList<Product> deletedProducts) throws IOException {
        String productName;
        String StoreName;
        boolean onSale;
        String description;
        int quantity;
        double price;
        int sold;
        try {
            FileWriter fw = new FileWriter("Products.txt", false);
            PrintWriter pw = new PrintWriter(fw, false);
            pw.flush();

            // first updates current products owned by the seller
            for (Product p: allproducts) {
                // deletes old products
                boolean wasDeleted = false;
                productName = p.getName();
                StoreName = p.getStoreName();
                onSale = p.isOnSale();
                description = p.getDescription();
                quantity = p.getQuantity();
                price = p.getPrice();
                sold = p.getQuantitySold();
                for (Product owned: ownedproducts) {
                    if (owned.getName().equalsIgnoreCase(p.getName())) {
                        quantity = owned.getQuantity();
                        price = owned.getPrice();
                        sold = owned.getQuantitySold();
                    }
                }
                for (Product deleted: deletedProducts) {
                    if (deleted.getName().equalsIgnoreCase(p.getName())) {
                        wasDeleted = true;
                    }
                }
                if (!wasDeleted) {
                    pw.write(String.format("%s; %s; %s; %s; %d; %.2f; %d\n",
                            productName, StoreName, onSale, description, quantity, price, sold));
                }
            }
            // then updates new products into data file
            for (Product owned: ownedproducts) {
                boolean isNew = true;
                for (Product p: allproducts) {
                    if (p.getName().equalsIgnoreCase(owned.getName())) {
                        isNew = false;
                    }
                }
                if (isNew) {
                    productName = owned.getName();
                    StoreName = owned.getStoreName();
                    onSale = owned.isOnSale();
                    description = owned.getDescription();
                    quantity = owned.getQuantity();
                    price = owned.getPrice();
                    sold = owned.getQuantitySold();
                    pw.write(String.format("%s; %s; %s; %s; %d; %.2f; %d\n",
                            productName, StoreName, onSale, description, quantity, price, sold));
                }
            }
            pw.close();
            fw .close();
        } catch (Exception e) {
            e.printStackTrace();
            oos.writeObject("Error updating product data!");
        }
    }

    private static void updateSellerAccount(String email, ArrayList<Store> stores) throws IOException {
        if (!stores.isEmpty()) {
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
                oos.writeObject("Error updating account data.");
            }
        }
    }

    // method that tries logging user in with provided email and password, currently it will iterate through file lines of data and return a boolean if user exists or not
    public boolean trylogging(String email, String password) throws IOException {
        try {
            BufferedReader bfr = new BufferedReader(new FileReader("Accounts.txt"));
            String line;
            String[] accountData;
            while ((line = bfr.readLine()) != null) {
                accountData = line.split("; ");
                if (accountData[0].equals(email) && accountData[1].equals(password)) {
                    oos.writeObject("success");
                    System.out.printf("Welcome back, %s\n", accountData[2]);
                    bfr.close();
                    return true;
                }
            }
            bfr.close();
            oos.writeObject("failed");
            return false;
        } catch (Exception e) {
            oos.writeObject("failed");
            return false;
        }
    }

    // method of account deletion dependent solely on email, on the assumption same email cannot have multiple accounts
    public boolean deleteAccount(String email) {
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
    public boolean createAccount(String email, String password, String nickname, String type) {
        try {
            BufferedWriter bfw = new BufferedWriter(new FileWriter("Accounts.txt", true));
            bfw.write(String.format("%s; %s; %s; %s", email, password, nickname, type));
            if (type.equals("customer")) {
                bfw.write(String.format("; %s\n", "none"));
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
    public boolean editAccount(String email, String check, String edit, int checking) {
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
                    oos.writeObject(accountData[checking]);
                    oos.writeObject(check);
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
    public ArrayList<String> getAccountInfo(String email) {
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
    public ArrayList<Product> importPurchaseHistory(String purchases) throws IOException {

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

    public boolean exportPurchaseHistory(ArrayList<Product> purchaseHistory, String nickname) {
        try {
            String filename = String.format("%s's Purchase History.txt", nickname);
            File historyfile = new File(filename);
            BufferedWriter w = new BufferedWriter(new FileWriter(historyfile));

            for (Product p: purchaseHistory) {
                String name = p.getName();
                String storeName = p.getStoreName();
                String description = p.getDescription();
                boolean sale = p.isOnSale();
                double price = p.getPrice();
                w.write(String.format("%s, %s, %s, $%.2f, ", name, storeName, description, price));
                if (sale) {
                    w.write("Bought on sale.\n");
                } else {
                    w.write("\n");
                }
            }
            w.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<Store> getStores() throws IOException {
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
            oos.writeObject("There was an error getting store information!");
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

    public ArrayList<Product> getProducts() throws IOException {
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
            oos.writeObject("There was an error getting store information!");
            return null;
        }
    }

    public ArrayList<Product> getOwnedProducts(ArrayList<Store> ownedStores) throws IOException {
        try {

            ArrayList<Product> products = new ArrayList<>();
            for (Store s: ownedStores) {
                for (Product p: s.getProducts()) {
                    products.add(p);
                }
            }
            return products;
        } catch (Exception e) {
            oos.writeObject("There was an error getting product information");
            return null;
        }
    }

    public boolean getProductPage(ArrayList<Product> directory, String name) throws IOException {
        for (Product p: directory) {
            if (name.equalsIgnoreCase(p.getName())) {
                oos.writeObject(p.productPage());
                return true;
            }
        }
        return false;
    }

    static ObjectOutputStream oos;
    ObjectInputStream ois;

    @Override
    public void run() {
        //Scanner scan = new Scanner(System.in);

        String choice; // stores user navigation inputs

        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
            oos.writeObject("Welcome!");
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
            ArrayList<Product> deletedProducts = new ArrayList<Product>();
            ArrayList<Store> ownedStores = null;
            ArrayList<Product> ownedProducts = null;
            ArrayList<Product> purchaseHistory = null;

            while (on) {
                choice = (String)ois.readObject();

                if (choice.equals("1")) { // user chooses to login
                    email = (String)ois.readObject();
                    String password = (String)ois.readObject();

                    if (trylogging(email, password)) {
                        logged = true;
                        accountData = getAccountInfo(email); // load account info into arraylist
                        scenario = "normal";
                        sortBy = "normal";
                    }

                    while (logged) {
                        // 发送账号信息给客户端
                        oos.writeObject(accountData);
                        if (accountData.get(3).equals("customer")) { // if the logged account is a customer
                            if (first) {
                                purchaseHistory = importPurchaseHistory(accountData.get(5));
                                first = !first;
                            }
                            Customer customer = new Customer(email, password, accountData.get(2), purchaseHistory, ois, oos);
                            customer.displayMarket(productDirectory, scenario, sortBy, sale, searching, search);
                            System.out.println("""
                                1. Sort products
                                2. Search for a product
                                3. Display Dashboard
                                4. Select a product
                                5. Account Settings
                                6. View purchase history
                                7. Logout""");
                            choice = (String)ois.readObject();

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
                                    choice = (String)ois.readObject();
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
                                    search = (String)ois.readObject();
                                    System.out.println("Searching for products...");
                                }
                                case "3" -> {
                                    boolean dashboard = true;
                                    sortBy = "normal";
                                    while (dashboard) {
                                        Customer.displayDashboard(storeDirectory, sortBy, purchaseHistory);
                                        System.out.println("1. Sort dashboard\n2. Return to Marketplace");
                                        choice = (String)ois.readObject();
                                        if (choice.equals("1")) {
                                            System.out.println("""
                                                1. Sort by ascending sales
                                                2. Sort by descending sales
                                                3. Return to dashboard menu""");
                                            choice = (String)ois.readObject();
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
                                    String productname = (String)ois.readObject();
                                    boolean exists = getProductPage(productDirectory, productname);
                                    oos.writeObject(exists);
                                    if (!exists) {
                                        System.out.println("The product does not exist!\n Returning to the marketplace...");
                                    } else {
                                        oos.writeObject("1. Purchase\n2. Return to market");
                                        choice = (String)ois.readObject();
                                        if (choice.equals("1")) {
                                            boolean purchased = Customer.purchase(productDirectory, storeDirectory,
                                                    productname, purchaseHistory, customer.getNickname());
                                            oos.writeBoolean(purchased);
                                            if (purchased) {
                                                System.out.println("The product has been successfully purchased!\nReturning to the marketplace...");
                                                updateCustomerHistory(email, purchaseHistory);
                                                updateCustomerProducts(productDirectory);
                                                updateCustomerStores(storeDirectory);
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
                                        choice = (String)ois.readObject();
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
                                                choice = (String)ois.readObject();
                                                checking = Integer.parseInt(choice) - 1;
                                                switch (choice) {
                                                    case "1" -> {
                                                        System.out.println("Enter the updated email:");
                                                        edit = (String)ois.readObject();
                                                        check = email;
                                                    }
                                                    case "2" -> {
                                                        System.out.println("Enter the updated password:");
                                                        edit = (String)ois.readObject();
                                                        System.out.println(accountData.get(1));
                                                        check = accountData.get(1);
                                                    }
                                                    case "3" -> {
                                                        System.out.println("Enter the new nickname:");
                                                        edit = (String)ois.readObject();
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
                                        ArrayList<String> historys = new ArrayList<>();
                                        for (Product p : purchaseHistory) {
                                            System.out.printf("Name: %s\nDescription: %s\nPurchased from %s at $%.2f\n",
                                                    p.getName(), p.getDescription(), p.getStoreName(), p.getPrice());
                                            historys.add(String.format("Name: %s\nDescription: %s\nPurchased from %s at $%.2f\n",
                                                    p.getName(), p.getDescription(), p.getStoreName(), p.getPrice()));
                                        }
                                        oos.writeObject(historys);
                                        System.out.println("====================");
                                        System.out.println("1. Export Purchase history\n2. Return to marketplace");
                                        choice = (String)ois.readObject();
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
                        }
                        else if (accountData.get(3).equals("seller")) {
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
                            choice = (String)ois.readObject();
                            switch (choice) {
                                case "1" -> {
                                    System.out.println("Enter the name for the store");
                                    String storeName = (String)ois.readObject();
                                    Store store = new Store(storeName, 0, new ArrayList<>(), new ArrayList<>());
                                    seller.addStore(store);
                                    System.out.println("Your new store has been created!");
                                    updateSellerAccount(email, ownedStores);
                                    updateSellerStores(storeDirectory, ownedStores);
                                    updateSellerProducts(productDirectory, ownedProducts, deletedProducts);
                                }
                                case "2" -> {
                                    System.out.println("Enter the name of the store:");
                                    String selectedStore = (String)ois.readObject();
                                    boolean productMenu = true;
                                    while (productMenu) {
                                        updateSellerAccount(email, ownedStores);
                                        updateSellerStores(storeDirectory, ownedStores);
                                        updateSellerProducts(productDirectory, ownedProducts, deletedProducts);
                                        System.out.println("""
                                            1. Create a product
                                            2. Edit a product
                                            3. Delete a product
                                            4. Import Products
                                            5. Export Products
                                            6. Choose a different store
                                            7. Return to Seller Menu""");
                                        choice = (String)ois.readObject();
                                        switch (choice) {
                                            case "1" -> {
                                                System.out.println("Enter the name of the product:");
                                                String pName = (String)ois.readObject();
                                                System.out.println("Enter the description of the product:");
                                                String des = (String)ois.readObject();
                                                System.out.println("Enter the quantity of the product:");
                                                int quantity = ois.readInt();
                                                System.out.println("Enter the price of the product:");
                                                double price = ois.readDouble();
                                                Product toBeAdded = new Product(pName, selectedStore, false, des, quantity, price, 0);
                                                ownedStores = seller.getStores();
                                                ownedProducts = getOwnedProducts(ownedStores);
                                                for (Store s : seller.getStores()) {
                                                    if (selectedStore.equals(s.getName())) {
                                                        try {
                                                            s.addProduct(toBeAdded);
                                                            System.out.println("Product has been added.");
                                                            updateSellerAccount(email, ownedStores);
                                                            updateSellerStores(storeDirectory, ownedStores);
                                                            updateSellerProducts(productDirectory, ownedProducts, deletedProducts);
                                                        } catch (NotAProductException e) {
                                                            System.out.println(e);
                                                        }
                                                    }
                                                }
                                            }
                                            case "2" -> {
                                                System.out.println("Enter the name of the product:");
                                                String pName = (String)ois.readObject();
                                                Product selectedProduct = null;
                                                for (Product p : ownedProducts) {
                                                    if (pName.equalsIgnoreCase(p.getName())) {
                                                        selectedProduct = p;
                                                    }
                                                }
                                                System.out.println("What would you like to change about the product?");
                                                System.out.println("1.Name\n2. Description\n3. Quantity\n4. Price");
                                                choice = (String)ois.readObject();
                                                switch (choice) {
                                                    case "1" -> {
                                                        System.out.println("Enter the new name for the product:");
                                                        String newName = (String)ois.readObject();
                                                        selectedProduct.setName(newName);
                                                    }
                                                    case "2" -> {
                                                        System.out.println("Enter the new description for the product:");
                                                        String newDes = (String)ois.readObject();
                                                        selectedProduct.setDescription(newDes);
                                                    }
                                                    case "3" -> {
                                                        System.out.println("Enter the new quantity for the product:");
                                                        int newQuantity = ois.readInt();
                                                        selectedProduct.setQuantity(newQuantity);
                                                    }
                                                    case "4" -> {
                                                        System.out.println("Enter the new price for the product:");
                                                        double newPrice = ois.readDouble();
                                                        selectedProduct.setPrice(newPrice);
                                                    }
                                                }
                                                System.out.println("The product has been edited.");
                                                updateSellerAccount(email, ownedStores);
                                                updateSellerStores(storeDirectory, ownedStores);
                                                updateSellerProducts(productDirectory, ownedProducts, deletedProducts);
                                            }
                                            case "3" -> {
                                                System.out.println("Enter the name of the product to be deleted:");
                                                String pName = (String)ois.readObject();
                                                for (Store s : ownedStores) {
                                                    Iterator<Product> products = s.getProducts().iterator();
                                                    while(products.hasNext()) {
                                                        Product p = products.next();
                                                        if (pName.equalsIgnoreCase(p.getName())) {
                                                            deletedProducts.add(p);
                                                            products.remove();
                                                        }
                                                    }
                                                }
                                                System.out.println("The product has been deleted.");
                                                updateSellerAccount(email, ownedStores);
                                                updateSellerStores(storeDirectory, ownedStores);
                                                updateSellerProducts(productDirectory, ownedProducts, deletedProducts);
                                            }
                                            case "4" -> {
                                                System.out.println("Enter the path of the file to be imported:");
                                                try {
                                                    File importFile = new File((String)ois.readObject());
                                                    BufferedReader br = new BufferedReader(new FileReader(importFile));
                                                    String line;
                                                    String[] productInfo;
                                                    String productName;
                                                    String productDescription;
                                                    boolean onSale;
                                                    double productPrice;
                                                    int productQuantity;
                                                    while ((line = br.readLine()) != null) {
                                                        productInfo = line.split("; ");
                                                        productName = productInfo[0];
                                                        onSale = Boolean.parseBoolean(productInfo[1]);
                                                        productDescription = productInfo[2];
                                                        productQuantity = Integer.parseInt(productInfo[3]);
                                                        productPrice = Double.parseDouble(productInfo[4]);
                                                        for (Store s : seller.getStores()) {
                                                            if (selectedStore.equals(s.getName())) {
                                                                s.addProduct(new Product(productName, selectedStore, onSale,
                                                                        productDescription, productQuantity, productPrice, 0));
                                                            }
                                                        }
                                                    }
                                                    br.close();
                                                    importFile.delete();
                                                    System.out.println("The product(s) in the file have been imported!");
                                                    updateSellerAccount(email, ownedStores);
                                                    updateSellerStores(storeDirectory, ownedStores);
                                                    updateSellerProducts(productDirectory, ownedProducts, deletedProducts);
                                                } catch (Exception e) {
                                                    System.out.println("Error importing new products!");
                                                }
                                            }
                                            case "5" -> {
                                                System.out.println("Enter the names of the products to be exported separated by spaced commas:");
                                                String export = (String)ois.readObject();
                                                String[] exports = export.split(", ");
                                                System.out.println("Enter the name of the exported file");
                                                String exportedName = (String)ois.readObject();
                                                try {
                                                    File exportFile = new File(exportedName);
                                                    BufferedWriter bw = new BufferedWriter(new FileWriter(exportFile));
                                                    for (String productName: exports) {
                                                        for (Store s : ownedStores) {
                                                            Iterator<Product> products = s.getProducts().iterator();
                                                            while(products.hasNext()) {
                                                                Product p = products.next();
                                                                if (productName.equalsIgnoreCase(p.getName())) {
                                                                    Product product = p;
                                                                    products.remove();
                                                                    deletedProducts.add(p);
                                                                    bw.write(String.format("%s; %b; %s; %d; %.2f\n",
                                                                            productName, product.isOnSale(),
                                                                            product.getDescription(), product.getQuantity(), product.getPrice()));
                                                                }
                                                            }
                                                        }
                                                    }
                                                    bw.close();
                                                    System.out.println("Products have been successfully exported!");
                                                    updateSellerAccount(email, ownedStores);
                                                    updateSellerStores(storeDirectory, ownedStores);
                                                    updateSellerProducts(productDirectory, ownedProducts, deletedProducts);
                                                } catch (Exception e) {
                                                    System.out.println("Error exporting products!");
                                                }
                                            }
                                            case "6" -> {
                                                System.out.println("Enter the name of the store:");
                                                selectedStore = (String)ois.readObject();
                                            }
                                            default -> productMenu = false;
                                        }
                                    }
                                }
                                case "3" -> {
                                    boolean dash = true;
                                    sortBy = "normal";
                                    while (dash) {
                                        oos.writeObject(sortBy);
                                        seller.displayDashboard(ownedStores, sortBy);
                                        System.out.println("1. Sort by Ascending Sales\n2. Sort by Descending Sales\n3. Return to Seller Menu");
                                        choice = (String)ois.readObject();
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
                                        choice = (String)ois.readObject();
                                        if (choice.equals("1")) {
                                            if (deleteAccount(email)) {
                                                oos.writeBoolean(true);
                                                System.out.println("Your account has been deleted!\nLogging out...");
                                                logged = false;
                                            } else {
                                                oos.writeBoolean(first);
                                                System.out.println("Your account couldn't be deleted!");
                                            }
                                        } else if (choice.equals("2")) {
                                            String edit = "";
                                            String check = "";
                                            int checking;
                                            System.out.println("What would you like to change?");
                                            System.out.println("1. Email\n2. Password\n3. Nickname");
                                            choice = (String)ois.readObject();
                                            checking = Integer.parseInt(choice) - 1;
                                            if (choice.equals("1")) {
                                                System.out.println("Enter the updated email:");
                                                edit = (String)ois.readObject();
                                                check = email;
                                            } else if (choice.equals("2")) {
                                                System.out.println("Enter the updated password:");
                                                edit = (String)ois.readObject();
                                                check = accountData.get(1);
                                            } else if (choice.equals("3")) {
                                                System.out.println("Enter the new nickname:");
                                                edit = (String)ois.readObject();
                                                check = accountData.get(2);
                                            }
                                            boolean editAccount = editAccount(email, check, edit, checking);
                                            oos.writeBoolean(editAccount);
                                            if (editAccount) {
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
                }
                else if (choice.equals("2")) { // Account Creation Section
                    email = (String)ois.readObject();
                    String password = (String)ois.readObject();
                    String nickname = (String)ois.readObject();
                    String type = (String)ois.readObject();
                    created = createAccount(email, password, nickname, type);
                    if (created) {
                        oos.writeObject("success");
                    } else {
                        oos.writeObject("failed");
                    }
                }
                else { // EXIT option
                    if (!(accountData == null)) {
                        if (accountData.get(3).equals("seller")) {
                            updateSellerAccount(email, ownedStores);
                            updateSellerStores(storeDirectory, ownedStores);
                            updateSellerProducts(productDirectory, ownedProducts, deletedProducts);
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
            ois.close();
            oos.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
