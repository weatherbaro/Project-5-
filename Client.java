import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class Client {

    static ObjectInputStream ois;
    static ObjectOutputStream oos;
    
    //account login and product creation gui
    JTextField xemail = new JTextField(5);
    JTextField xpassword = new JTextField(5);
    JPanel login = new JPanel();
    login.setLayout(new GridLayout(2,1));
    login.add(new JLabel("Email:"));
    login.add(xemail);
    login.add(new JLabel("Password:"));
    login.add(xpassword);
    
    JTextField xname = new JTextField(5);
    JTextField xdescription = new JTextField(5);
    JTextField xquantity = new JTextField(5);
    JTextField xprice = new JTextField(5);
    JPanel xproduct = new JPanel();
    xproduct.setLayout(new GridLayout(4,1));
    xproduct.add(new JLabel("Name:"));
    xproduct.add(xname);
    xproduct.add(new JLabel("Description:"));
    xproduct.add(xdescription);
    xproduct.add(new JLabel("Quantity:"));
    xproduct.add(xquantity);
    xproduct.add(new JLabel("Price:"));
    xproduct.add(xprice);


    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Socket socket = new Socket("localhost", 1000);
        ois = new ObjectInputStream(socket.getInputStream());
        oos = new ObjectOutputStream(socket.getOutputStream());
        showMessageDialog(ois.readObject());
        
        showWelcomeMessageDialog();
        
        while (true) {
            int select = mainMenuInputDialog();

            if (select == 1) {
                // login
                login();
            } else if (select == 2) {
                // create an account
                createAccount();
            } else {
                // exit
                showFarewellMessageDialog();
                break;
            }

        }
    }

    private static void createAccount() throws IOException, ClassNotFoundException {
        // 通知服务端选择创建账号
        oos.writeObject("2");
        String email = emailCreateInputDialog();
        oos.writeObject(email);
        String password = passCreateInputDialog();
        oos.writeObject(password);
        String nickname = nameCreateInputDialog());
        oos.writeObject(nickname);
        int role = roleCreateInputDialog();
        oos.writeObject(roles[role]);
        
        String createResult = (String) ois.readObject();
        if ("success".equalsIgnoreCase(createResult)) {
            accountSuccessMessageDialog();
        } else {
            accountFailMessageDialog();
        }
        
    }

    private static void  login() throws IOException, ClassNotFoundException {
        oos.writeObject("1");
        int res = JOptionPane.showConfirmDialog(null, login,
                "Account Login", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            String email = xemail.getText();
            String password = xpassword.getText();
        }
        
        oos.writeObject(email);
        oos.writeObject(password);
        String loginResult = (String) ois.readObject();
        if ("success".equalsIgnoreCase(loginResult)) {
            ArrayList<String> accountData = (ArrayList<String>) ois.readObject();
            if (accountData.get(3).equals("customer")) {
                // customer
                customer();
            } else if (accountData.get(3).equals("seller")) {
                // seller
                seller();
            }
        } else {
            loginErrorMessageDialog();
        }

    }

    private static void seller() throws IOException, ClassNotFoundException {
        
        int choice = sellerMenuInputDialog();
        oos.writeObject(choice);
        if (choice == 1) {
            String storeName = storeCreateInputDialog();
            oos.writeObject(storeName);
            storeSuccessMessageDialog();
        } else if (choice == 2) {
            String storeName = storeInputDialog();
            oos.writeObject(storeName);
            while (true) {
                choice = storeMenuInputDialog();
                oos.writeObject(choice + "");
                if (choice == 1) {
                    
                    String pName;
                    String des;
                    int quantity;
                    double price;
                    
                    //Product creation jpanel
                    int result = JOptionPane.showConfirmDialog(null, product, "Product Creation", JOptionPane.OK_CANCEL_OPTION);
                    if (result == JOptionPane.OK_OPTION) {
                        if (xname.getText() == null || xdescription.getText() == null || xquantity.getText() == null || xprice.getText() == null) {
                            productFailMessageDialog();
                        } else {
                            pname = xname.getText();
                            des = xdescription.getText();
                            quantity = xquantity.getText();
                            price = xprice.getText();
                        }
                    }
                    
                    oos.writeObject(pName);
                    oos.writeObject(des);
                    oos.writeInt(quantity);
                    oos.writeDouble(price);
                    
                    
                } else if (choice == 2) {
                    String pName = productInputDialog();
                    oos.writeObject(pName);
                    choice = changeProductInputDialog();
                    oos.writeObject(choice + "");
                    if (choice == 1) {
                        String newName = pNameInputDialog();
                        oos.writeObject(newName);
                    } else if (choice == 2){
                        String newDes = pDesInputDialog();
                        oos.writeObject(newDes);
                    } else if (choice == 3) {
                        int newQuantity = pQuanInputDialog();
                        oos.writeInt(newQuantity);
                    } else if (choice == 4) {
                        double newPrice = pPriceInputDialog();
                        oos.writeDouble(newPrice);
                    }
                } else if (choice == 3) {
                    String pName = productInputDialog();
                    oos.writeObject(pName);
                    pDeleteMessageDialog();
                } else if (choice == 4) {
                    String selectedStore = storeInputDialog();
                    oos.writeObject(selectedStore);
                } else {
                    break;
                }
            }
        } else if (choice == 3) {
            while (true) {
                //asks how to sort
                choice = sellerDashboardInputDialog();
                oos.writeObject(choice + "");
                if (choice == 3) {
                    break;
                }
                //sorts
                String sortBy = (String) ois.readObject();
                sellerDisplayDashboard(sortBy);
            }
        } else if (choice == 4) {
            while (true) {
                choice = accountMenuInputDialog();
                oos.writeObject(choice + "");
                if (choice == 1) {
                    boolean deleteAccount = ois.readBoolean();
                    if (deleteAccount) {
                        accDeleteMessageDialog();
                    } else {
                        accDeleteErrMessageDialog();
                    }
                } else if (choice == 2) {
                    choice = changeAccountInputDialog();
                    oos.writeObject(choice + "");
                    if (choice == 1) {
                        String edit = accEmailInputDialog();
                        oos.writeObject(edit);
                    } else if (choice == 2) {
                        String edit = accPassInputDialog();
                        oos.writeObject(edit);
                    } else if (choice == 3) {
                        String edit = accNameInputDialog();
                        oos.writeObject(edit);
                    }
                    boolean editAccount = ois.readBoolean();
                    if (editAccount) {
                        accUpMessageDialog();
                    } else {
                        accUpErrMessageDialog();
                    }
                } else {
                    break;
                }
            }
        }
    }

    private static void sellerDisplayDashboard(String sortBy) {
        // TODO: 2023/4/23
    }

    private static void customer() throws IOException, ClassNotFoundException {
        while (true) {
            displayMarket();
            
            int choice = customerMainInputDialog();
            oos.writeObject(choice + "");
            if (1 == choice) {
                sortProducts();
            } else if (2 == choice) {
                searchForProduct();
            } else if (3 == choice) {
                displayDashboard();
            } else if (4 == choice) {
                selectProduct();
            } else if (5 == choice) {
                accountSettings();
            } else if (6 == choice) {
                viewPurchaseHistory();
            } else if (7 == choice) {
                break;
            }
        }
    }

    private static void logout() {

    }

    private static void viewPurchaseHistory() throws IOException, ClassNotFoundException {
        // TODO: 2023/4/22 GUI Showing your purchase history...\n====================
        while (true) {
            ArrayList<String> historys = (ArrayList<String>) ois.readObject();
            // TODO: 2023/4/22 GUI ====================\n1. Export Purchase history\n2. Return to marketplace
            int choice = getChoice();
            oos.writeObject(choice + "");
            if (choice == 1) {
                boolean exported = ois.readBoolean();
                // TODO: 2023/4/22 export purchase history
            } else {
                break;
            }
        }
    }

    private static void accountSettings() throws IOException {
        while (true) {
            int choice = getChoice();
            oos.writeObject(choice + "");
            if (choice == 1) {
                boolean delete = ois.readBoolean();
                if (delete) {
                    accDeleteMessageDialog();
                } else {
                    accDeleteErrMessageDialog();
                }
            } else if (choice == 2) {
                choice = changeAccountInputDialog();
                oos.writeObject(choice + "");
                if (choice == 1) {
                    String edit = accEmailInputDialog();
                    oos.writeObject(edit);
                } else if (choice == 2) {
                    String password = accPassInputDialog();
                    oos.writeObject(password);
                } else if (choice == 3) {
                    String nickname = accNameInputDialog();
                    oos.writeObject(nickname);
                }
                boolean editAccount = ois.readBoolean();
                if (editAccount) {
                    accUpMessageDialog();
                } else {
                    accUpErrMessageDialog();
                }
            } else {
                break;
            }
        }
    }

    private static void selectProduct() throws IOException {
        String productName = productInputDialog();
        oos.writeObject(productName);
        boolean exists = ois.readBoolean();
        if (!exists) {
            productErrMessageDialog();
        } else {
            int choice = purchaseInputDialog();
            oos.writeObject(choice + "");
            if (choice == 1) {
                boolean purchased = ois.readBoolean();
                if (purchased) {
                    purchaseMessageDialog();
                } else {
                    purchaseErrMessageDialog();
                }
            }
        }
        returnMarketMessageDialog();
    }

    private static String getProductName() {
        return null;
    }

    private static void displayDashboard() throws IOException {
        // TODO: 2023/4/22 customer.displayBoard
        // TODO: 2023/4/22 GUI 1. Sort dashboard\n2. Return to Marketplace
        int choice = getChoice();
        oos.writeObject(choice + "");
        if (choice == 1) {
            choice = dashboardInputDialog();
            oos.writeObject(choice + "");
        }
    }

    private static int getChoice() {
        // TODO: 2023/4/22
        return 0;
    }

    private static void searchForProduct() throws IOException {
        String search = showInputDialog("Please enter a search term:");
        oos.writeObject(search);
    }


    private static void sortProducts() throws IOException {
        int choice = productSortInputDialog();
        oos.writeObject(choice + "");

    }

    private static void displayMarket() throws IOException, ClassNotFoundException {
        DisplayMarketCondition condition = (DisplayMarketCondition) ois.readObject();
        if (condition.isSale()) {
            showMessageDialog("Now displaying the marketplace...\n====================");
        } else {
            showMessageDialog("Sorting marketplace...\n====================");
        }
        ArrayList<String> productStrings = (ArrayList<String>) ois.readObject();
        showMessageDialog(String.join("\n", productStrings));
    }

    private static void showMessageDialog(Object obj) {
        JOptionPane.showMessageDialog(null, obj);
    }

    private static int showOptionDialog(Object message, String title, Object[] options) {
        return JOptionPane.showOptionDialog(null, message, title, JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, null);
    }

    private static String showInputDialog(String message) {
        return JOptionPane.showInputDialog(message);
    }
    -----------------------------------

    public static void showWelcomeMessageDialog() {
        JOptionPane.showMessageDialog(null, "Welcome to the Marketplace",
                "Marketplace", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showFarewellMessageDialog() {
        JOptionPane.showMessageDialog(null, "Goodbye, have a nice day!",
                "Marketplace", JOptionPane.INFORMATION_MESSAGE);
    }

    public static int mainMenuInputDialog() {
        int choice = 0;
        String[] options = {"Login", "Create Account", "Exit"};
        int x = JOptionPane.showOptionDialog(null, "Select what you would like to do",
                "Marketplace", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options,
                options[0]);
        switch (x) {
            case JOptionPane.YES_OPTION:
                choice = 1;
                break;
            case JOptionPane.NO_OPTION:
                choice = 2;
                break;
            case JOptionPane.CANCEL_OPTION, JOptionPane.CLOSED_OPTION:
                choice = 3;
                break;
        }
        return choice;
    }

    public static String emailCreateInputDialog() {
        String choice;
        choice = JOptionPane.showInputDialog(null, "Please enter an email",
                "Account Creation", JOptionPane.QUESTION_MESSAGE);
        return choice;
    }

    public static String passCreateInputDialog() {
        String choice;
        choice = JOptionPane.showInputDialog(null, "Please enter a password",
                "Account Creation", JOptionPane.QUESTION_MESSAGE);
        return choice;
    }

    public static String nameCreateInputDialog() {
        String choice;
        choice = JOptionPane.showInputDialog(null, "Please enter a username",
                "Account Creation", JOptionPane.QUESTION_MESSAGE);
        return choice;
    }

    public static String roleCreateInputDialog() {
        String choice;
        String[] roles = {"Customer", "Seller"};
        choice = (String) JOptionPane.showInputDialog(null, "Select your role",
                "Account Creation", JOptionPane.QUESTION_MESSAGE, null, roles, roles[0]);
        return choice;
    }

    public static void accountSuccessMessageDialog() {
        JOptionPane.showMessageDialog(null, "Your new account has been successfully created!",
                "Account Creation", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void accountFailMessageDialog() {
        JOptionPane.showMessageDialog(null, "There was a problem with creating your account.",
                "Account Creation", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void loginErrorMessageDialog() {
        JOptionPane.showMessageDialog(null, "You entered the wrong login or password",
                "Marketplace Login", JOptionPane.ERROR_MESSAGE);
    }
    
   
    -------------------------
        
    public static int customerMainInputDialog() {
        String[] options = {"1. Sort products", "2. Search for a product", "3. Display Dashboard",
                "4. Select a product", "5. Account Settings", "6. View purchase history", "Exit"};
        String choice = (String) JOptionPane.showInputDialog(null, "What would you like to do?",
                "Customer Menu", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        int x = 1 + Arrays.asList(options).indexOf(choice);
        return x;
    }
    
    public static int productSortInputDialog() {
        String[] productSort = {"1. Sale filter", "2. Sort by ascending price",
                "3. Sort by descending price", "4. Sort by ascending quantity", "5. Sort by descending quantity",
                "6. Show normal marketplace", "Return"};
        String choice = (String) JOptionPane.showInputDialog(null, "How would you like to sort products?",
                "Product Sort Menu", JOptionPane.QUESTION_MESSAGE, null, productSort, productSort[0]);
        int x = 1 + Arrays.asList(productSort).indexOf(choice);
        return x;
    }
    
    public static int sellerDashboardInputDialog() {
        String[] dashboard = {"1. Sort by ascending sales", "2. Sort by descending sales",
                "3. Return to dashboard menu"};
        String choice = (String) JOptionPane.showInputDialog(null, "How do you want to sort sales?",
                "Product Sort Menu", JOptionPane.QUESTION_MESSAGE, null, dashboard, dashboard[0]);
        int x = 1 + Arrays.asList(dashboard).indexOf(choice);
        return x;
    }
    
    public static int sellerMenuInputDialog() {
        String[] sellerMenu = {"1. Create a store", "2. Select a store", "3. Display Dashboard",
                "4. Account Settings", "5. Logout"};
        String choice = (String) JOptionPane.showInputDialog(null, "What would you like to do?",
                "Seller Menu", JOptionPane.QUESTION_MESSAGE, null, sellerMenu, sellerMenu[0]);
        int x = 1 + Arrays.asList(sellerMenu).indexOf(choice);
        return x;
    }
    
    public static String storeCreateInputDialog() {
        String choice;
        choice = JOptionPane.showInputDialog(null, "Please enter a name for your store",
                "Store Creation", JOptionPane.QUESTION_MESSAGE);
        return choice;
    }

    public static void storeSuccessMessageDialog() {
        JOptionPane.showMessageDialog(null, "Your new store has been successfully created!",
                "Store Creation", JOptionPane.INFORMATION_MESSAGE);
    }

    public static String storeInputDialog() {
        String choice;
        choice = JOptionPane.showInputDialog(null, "Please enter the name of your store",
                "Store", JOptionPane.QUESTION_MESSAGE);
        return choice;
    }
    
    public static int storeMenuInputDialog() {
        String[] storeMenu = {"1. Create a product", "2. Edit a product", "3. Delete a product",
                "4. Choose a different store", "5. Return to Seller Menu"};
        String choice = (String) JOptionPane.showInputDialog(null, "What would you like to do?",
                "Store Menu", JOptionPane.QUESTION_MESSAGE, null, storeMenu, storeMenu[0]);
        int x = 1 + Arrays.asList(storeMenu).indexOf(choice);
        return x;
    }
    
    public static void productFailMessageDialog() {
        JOptionPane.showMessageDialog(null, "Error: please enter product details in all fields.",
                "Product Creation", JOptionPane.ERROR_MESSAGE);
    }
    
    public static String productInputDialog() {
        String choice;
        choice = JOptionPane.showInputDialog(null, "Please enter the name of the product",
                "Store", JOptionPane.QUESTION_MESSAGE);
        return choice;
    }

    public static int changeProductInputDialog() {
        String[] storeMenu = {"1.Name", "2. Description", "3. Quantity", "4. Price"};
        String choice = (String) JOptionPane.showInputDialog(null, "What would you like to change?",
                "Product", JOptionPane.QUESTION_MESSAGE, null, storeMenu, storeMenu[0]);
        int x = 1 + Arrays.asList(storeMenu).indexOf(choice);
        return x;
    }
    
    public static int changeAccountInputDialog() {
        String[] account = {"1. Name", "2. Password", "3. Nickname"};
        String choice = (String) JOptionPane.showInputDialog(null, "What would you like to change?",
                "Store", JOptionPane.QUESTION_MESSAGE, null, account, account[0]);
        int x = 1 + Arrays.asList(account).indexOf(choice);
        return x;
    }

    public static int accountMenuInputDialog() {
        String[] menu = {"1. Delete Account", "2. Edit Account", "3. Return"};
        String choice = (String) JOptionPane.showInputDialog(null, "What would you like to change?",
                "Store", JOptionPane.QUESTION_MESSAGE, null, menu, menu[0]);
        int x = 1 + Arrays.asList(menu).indexOf(choice);
        return x;
    }

    public static String pNameInputDialog() {
        String choice;
        choice = JOptionPane.showInputDialog(null, "Enter the new name of the product",
                "Product", JOptionPane.QUESTION_MESSAGE);
        return choice;
    }

    public static String pDesInputDialog() {
        String choice;
        choice = JOptionPane.showInputDialog(null, "Enter the new description of the product",
                "Product", JOptionPane.QUESTION_MESSAGE);
        return choice;
    }

    public static int pQuanInputDialog() {
        int choice;
        choice = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter the new quantity of the product",
                "Product", JOptionPane.QUESTION_MESSAGE));
        return choice;
    }

    public static double pPriceInputDialog() {
        double choice;
        choice = Double.parseDouble(JOptionPane.showInputDialog(null, "Enter the new price of the product",
                "Product", JOptionPane.QUESTION_MESSAGE));
        return choice;
    }

    public static void pDeleteMessageDialog() {
        JOptionPane.showMessageDialog(null, "The product has been deleted.",
                "Product", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static String accEmailInputDialog() {
        String choice;
        choice = JOptionPane.showInputDialog(null, "Enter your new email",
                "Account", JOptionPane.QUESTION_MESSAGE);
        return choice;
    }

    public static String accPassInputDialog() {
        String choice;
        choice = JOptionPane.showInputDialog(null, "Enter your new password",
                "Account", JOptionPane.QUESTION_MESSAGE);
        return choice;
    }

    public static int accNameInputDialog() {
        int choice;
        choice = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter your new nickname",
                "Account", JOptionPane.QUESTION_MESSAGE));
        return choice;
    }

    public static void accUpMessageDialog() {
        JOptionPane.showMessageDialog(null, "Your account has been updated!",
                "Account", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void accUpErrMessageDialog() {
        JOptionPane.showMessageDialog(null, "There was a problem updating your account, try again.",
                "Account", JOptionPane.WARNING_MESSAGE);
    }

    public static void accDeleteMessageDialog() {
        JOptionPane.showMessageDialog(null, "Your account has been deleted. Logging out...",
                "Account", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void accDeleteErrMessageDialog() {
        JOptionPane.showMessageDialog(null, "Your account could not be deleted",
                "Account", JOptionPane.WARNING_MESSAGE);
    }
    
    public static void productErrMessageDialog() {
        JOptionPane.showMessageDialog(null, "The product you searched for does not exist",
                "Product", JOptionPane.ERROR_MESSAGE);
    }

    public static int purchaseInputDialog() {
        int choice = 1 + JOptionPane.showConfirmDialog(null, "Would you like to buy this product?",
                "Product", JOptionPane.YES_NO_OPTION);
        return choice;
    }

    public static void purchaseMessageDialog() {
        JOptionPane.showMessageDialog(null, "Product has been purchased!",
                "Product", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void purchaseErrMessageDialog() {
        JOptionPane.showMessageDialog(null, "There was an error purchasing the product.",
                "Product", JOptionPane.ERROR_MESSAGE);
    }

    public static void returnMarketMessageDialog() {
        JOptionPane.showMessageDialog(null, "Returning to the marketplace...",
                "Marketplace", JOptionPane.INFORMATION_MESSAGE);
    }
    
    
}
