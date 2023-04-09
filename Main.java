import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

// the current method is to use a text file to store account information
// another way would be a static global arraylist of accounts classes that
// contains the specific seller or buyer and all of their info in one object
public class Main {

    public static void main(String[] args) throws IOException {
        Scanner scan = new Scanner(System.in);

        String choice = ""; // stores user navigation inputs
        System.out.println("Welcome!");

        boolean logged = false; // status on whether user has logged in successfully
        boolean created = false; // checks if account creation was successful
        boolean on = true; // controls if program is running or not

        while (on) {
            System.out.println("1. Login\n2. Create An Account\n3. Exit"); // right now user is going to enter just email and password
            choice = scan.nextLine();

            if (choice.equals("1")) { // user chooses to login
                System.out.println("Enter your email:");
                String email = scan.nextLine();
                System.out.println("Enter your password:");
                String password = scan.nextLine();
                logged = trylogging(email, password);
                while (logged) {
                    // Primary Interface based on user class
                    System.out.println("1. Delete Account\n2. Edit Account\n3. Logout");
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
                        int checking = -1;
                        System.out.println("What would you like to change?");
                        System.out.println("1. Email\n2. Password");
                        choice = scan.nextLine();
                        if (choice.equals("1")) {
                            System.out.println("Enter the updated email:");
                            edit = scan.nextLine();
                            check = email;
                            checking = 0;
                        } else if (choice.equals("2")) {
                            System.out.println("Enter the updated password:");
                            edit = scan.nextLine();
                            check = password;
                            checking = 1;
                        }
                        if (editAccount(email, check, edit, checking)) {
                            System.out.println("Your account has been updated!");
                        } else {
                            System.out.println("There was a problem with updating your account.");
                        }
                    } else if (choice.equals("3")) {
                        logged = false;
                    }
                }
            } else if (choice.equals("2")) { // Account Creation Section
                System.out.println("Enter your email:");
                String email = scan.nextLine();
                System.out.println("Enter your password:");
                String password = scan.nextLine();
                System.out.println("1. Seller\n2. Customer");
                String type = scan.nextLine();
                if (type.equals("1")) {
                    type = "seller";
                } else {
                    type = "customer";
                }
                created = createAccount(email, password, type);
                if (created) {
                    System.out.println("Your new account has been successfully created!");
                } else {
                    System.out.println("There was a problem with creating yuor account.");
                }
            } else { // EXIT option
                System.out.println("Goodbye!");
                on = false;
            }
        }
    }

    // method that tries logging user in with provided email and password, currently it will iterate through file lines of data and return a boolean if user exists or not
    public static boolean trylogging(String email, String password) {
        try {
            BufferedReader bfr = new BufferedReader(new FileReader("Accounts.txt"));
            String line = "";
            String[] accountData;
            while ((line = bfr.readLine()) != null) {
                accountData = line.split(", ", 3);
                if (accountData[0].equals(email) && accountData[1].equals(password)) {
                    System.out.println("Login Successful!");
                    System.out.printf("Welcome back, %s\n", email);
                    bfr.close();
                    return true;
                }
            }
            System.out.println("Login Failed! User does not exist.");
            bfr.close();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
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
                if(!(line.split(", ", 3)[0].equals(email))) {
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
    public static boolean createAccount(String email, String password, String type) {
        try {
            BufferedWriter bfw = new BufferedWriter(new FileWriter("Accounts.txt", true));
            bfw.write(String.format("%s, %s, %s\n", email, password, type));
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
                accountData = line.split(", ", 3);
                if (accountData[0].equals(email) && accountData[checking].equals(check)) {
                    accountData[checking] = edit;
                    String newline = String.join(", ", accountData);
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
}
