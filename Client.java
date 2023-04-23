import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Client {

    static ObjectInputStream ois;
    static ObjectOutputStream oos;

    private static final String[] LOGIN_MENU = {"1. Login", "2. Create An Account", "3. Exit"};

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Socket socket = new Socket("localhost", 1000);
        ois = new ObjectInputStream(socket.getInputStream());
        oos = new ObjectOutputStream(socket.getOutputStream());
        showMessageDialog(ois.readObject());
        while (true) {
            int select = showOptionDialog(null, "Login Menu", LOGIN_MENU);

            if (select == 0) {
                // login
                login();
            } else if (select == 1) {
                // create an account
                createAccount();
            } else {
                // exit
                showMessageDialog("Goodbye!");
                break;
            }

        }
    }

    private static void createAccount() throws IOException, ClassNotFoundException {
        // 通知服务端选择创建账号
        oos.writeObject("2");
        String email = showInputDialog("Enter your email:");
        oos.writeObject(email);
        String password = showInputDialog("Enter your password:");
        oos.writeObject(password);
        String nickname = showInputDialog("Enter your nickname:");
        oos.writeObject(nickname);
        Object[] roles = {"seller", "customer"};
        int role = showOptionDialog("Enter your role:", "Account Creation Section", new Object[]{"seller", "customer"});
        oos.writeObject(roles[role]);
        String createResult = (String) ois.readObject();
        if ("success".equalsIgnoreCase(createResult)) {
            showMessageDialog("Your new account has been successfully created!");
        } else {
            showMessageDialog("There was a problem with creating your account.");
        }
    }

    private static void  login() throws IOException, ClassNotFoundException {
        // 通知服务端选择登录
        oos.writeObject("1");
        String email = showInputDialog("Enter your email:");
        oos.writeObject(email);
        String password = showInputDialog("Enter your password:");
        oos.writeObject(password);
        // 等待登录结果
        String loginResult = (String) ois.readObject();
        if ("success".equalsIgnoreCase(loginResult)) {
            // 登录成功
            showMessageDialog("Login Successful!");
            ArrayList<String> accountData = (ArrayList<String>) ois.readObject();
            // TODO: 2023/4/20
            if (accountData.get(3).equals("customer")) {
                // customer
                customer();
            } else if (accountData.get(3).equals("seller")) {
                // seller
                seller();
            }
        } else {
            // 登录失败
            showMessageDialog("There was an error logging in!");
        }

    }

    private static void seller() throws IOException, ClassNotFoundException {
        // TODO: 2023/4/23 GUI 1. Create a store
        //                     2. Select a store
        //                     3. Display Dashboard
        //                     4. Account Settings
        //                     5. Logout
        int choice = getChoice();
        oos.writeObject(choice);
        if (choice == 1) {
            // TODO: 2023/4/23 GUI Enter the name for the store
            String storeName = null;
            oos.writeObject(storeName);
            // TODO: 2023/4/23 GUI Your new store has been created!
        } else if (choice == 2) {
            // TODO: 2023/4/23 GUI Enter the name for the store
            String storeName = null;
            oos.writeObject(storeName);
            while (true) {
                // TODO: 2023/4/23 GUI 1. Create a product
                //                     2. Edit a product
                //                     3. Delete a product
                //                     4. Choose a different store
                //                     5. Return to Seller Menu
                choice = getChoice();
                oos.writeObject(choice + "");
                if (choice == 1) {
                    // TODO: 2023/4/23 GUI Enter the name of the product:
                    String pName = null;
                    oos.writeObject(pName);
                    // TODO: 2023/4/23 GUI Enter the description of the product:
                    String des = null;
                    oos.writeObject(des);
                    // TODO: 2023/4/23 GUI Enter the quantity of the product:
                    int quantity;
                    oos.writeInt(quantity);
                    // TODO: 2023/4/23 GUI Enter the price of the product:
                    double price;
                    oos.writeDouble(price);
                } else if (choice == 2) {
                    // TODO: 2023/4/23 GUI Enter the name of the product:
                    String pName = null;
                    oos.writeObject(pName);
                    // TODO: 2023/4/23 GUI  What would you like to change about the product?
                    // TODO: 2023/4/23 GUI  1.Name\n2. Description\n3. Quantity\n4. Price
                    choice = getChoice();
                    oos.writeObject(choice + "");
                    if (choice == 1) {
                        // TODO: 2023/4/23 GUI Enter the new name for the product:
                        String newName;
                        oos.writeObject(newName);
                    } else if (choice == 2){
                        // TODO: 2023/4/23 GUI Enter the new description for the product:
                        String newDes;
                        oos.writeObject(newDes);
                    } else if (choice == 3) {
                        // TODO: 2023/4/23 GUI Enter the new quantity for the product:
                        int newQuantity;
                        oos.writeInt(newQuantity);
                    } else if (choice == 4) {
                        // TODO: 2023/4/23 GUI Enter the new price for the product:
                        double newPrice;
                        oos.writeDouble(newPrice);
                    }
                    // TODO: 2023/4/23 GUI The product has been edited.
                } else if (choice == 3) {
                    // TODO: 2023/4/23 GUI Enter the name of the product to be deleted:
                    String pName;
                    oos.writeObject(pName);
                    // TODO: 2023/4/23 GUI The product has been deleted.
                } else if (choice == 4) {
                    // TODO: 2023/4/23 GUI Enter the name of the store:
                    String selectedStore;
                    oos.writeObject(selectedStore);
                } else {
                    break;
                }
            }
        } else if (choice == 3) {
            while (true) {
                String sortBy = (String) ois.readObject();
                sellerDisplayDashboard(sortBy);
                // TODO: 2023/4/23 GUI 1. Sort by Ascending Sales\n2. Sort by Descending Sales\n3. Return to Seller Menu
                choice = getChoice();
                oos.writeObject(choice + "");
                if (choice == 3) {
                    break;
                }
            }
        } else if (choice == 4) {
            while (true) {
                // TODO: 2023/4/23 GUI 1. Delete Account\n2. Edit Account\n3. Return
                choice = getChoice();
                oos.writeObject(choice + "");
                if (choice == 1) {
                    boolean deleteAccount = ois.readBoolean();
                    if (deleteAccount) {
                        // TODO: 2023/4/23 GUI Your account has been deleted!\nLogging out...
                    } else {
                        // TODO: 2023/4/23 GUI Your account couldn't be deleted!
                    }
                } else if (choice == 2) {
                    // TODO: 2023/4/23 GUI What would you like to change?
                    // TODO: 2023/4/23 GUI 1. Email\n2. Password\n3. Nickname
                    choice = getChoice();
                    oos.writeObject(choice + "");
                    if (choice == 1) {
                        // TODO: 2023/4/23 GUI Enter the updated email:
                        String edit;
                        oos.writeObject(edit);
                    } else if (choice == 2) {
                        // TODO: 2023/4/23 GUI Enter the updated password:
                        String edit;
                        oos.writeObject(edit);
                    } else if (choice == 3) {
                        // TODO: 2023/4/23 GUI Enter the new nickname:
                        String edit;
                        oos.writeObject(edit);
                    }
                    boolean editAccount = ois.readBoolean();
                    if (editAccount) {
                        // TODO: 2023/4/23 GUI Your account has been updated!
                    } else {
                        // TODO: 2023/4/23 GUI There was a problem with updating your account.
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

            int choice = 1+ showOptionDialog("", "customer menu", CUSTOMER_MENU);
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
                    // TODO: 2023/4/22 GUI Your account has been deleted!\nLogging out...
                } else {
                    // TODO: 2023/4/22 GUI Your account couldn't be deleted!
                }
            } else if (choice == 2) {
                // TODO: 2023/4/22 GUI What would you like to change?
                // TODO: 2023/4/22 GUI 1. Email\n2. Password\n3. Nickname
                choice = getChoice();
                oos.writeObject(choice + "");
                if (choice == 1) {
                    // TODO: 2023/4/22 GUI Enter the updated email:
                    // TODO: 2023/4/22 getEdit
                    String edit = null;
                    oos.writeObject(edit);
                } else if (choice == 2) {
                    // TODO: 2023/4/22 GUI Enter the updated password:
                    String password = null;
                    oos.writeObject(password);
                } else if (choice == 3) {
                    // TODO: 2023/4/22 GUI Enter the new nickname:
                    String nickname = null;
                    oos.writeObject(nickname);
                }
                boolean editAccount = ois.readBoolean();
                if (editAccount) {
                    // TODO: 2023/4/22 Your account has been updated!
                } else {
                    // TODO: 2023/4/22 There was a problem with updating your account.
                }
            } else {
                break;
            }
        }
    }

    private static void selectProduct() throws IOException {
        // TODO: 2023/4/22 GUI Please enter the name of the product:
        String productName = getProductName();
        oos.writeObject(productName);
        boolean exists = ois.readBoolean();
        if (!exists) {
            // TODO: 2023/4/22 GUI The product does not exist!
            // Returning to the marketplace...

        } else {
            // TODO: 2023/4/22 GUI 1. Purchase
            //2. Return to market
            // TODO: GUI getChoice
            int choice = getChoice();
            oos.writeObject(choice + "");
            if (choice == 1) {
                boolean purchased = ois.readBoolean();
                if (purchased) {
                    // TODO: 2023/4/22 GUI The product has been successfully purchased!\nReturning to the marketplace...
                } else {
                    // TODO: 2023/4/22 GUI The product could not be purchased!\nReturning to the marketplace...
                }
            } else {
                // TODO: 2023/4/22 GUI  Returning to the marketplace...
            }
        }
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
            // TODO: 2023/4/22 GUI  1. Sort by ascending sales 2. Sort by descending sale 3. Return to dashboard menu
            choice = getChoice();
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

    private static final String[] SORT_PRODUCTS_MENU = {"1. Sale filter", "2. Sort by ascending price",
        "3. Sort by descending price", "4. Sort by ascending quantity", "5. Sort by descending quantity",
        "6. Show normal marketplace", "7. Return"};

    private static void sortProducts() throws IOException {
        int choice = 1 + showOptionDialog("", "Sort product menu", SORT_PRODUCTS_MENU);
        oos.writeObject(choice + "");

    }

    private static final String[] CUSTOMER_MENU = new String[] {"1. Sort products",
        "2. Search for a product", "3. Display Dashboard", "4. Select a product", "5. Account Settings", "6. View purchase history", "7. Logout"};

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

}
