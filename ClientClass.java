import javax.swing.*;

public class ClientClass {
  
  public static void main(String[] args) {
    
    
    
  }
  
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

    public static String emailLoginInputDialog() {
        String choice;
        choice = JOptionPane.showInputDialog(null, "Please enter your email",
                "Marketplace Login", JOptionPane.QUESTION_MESSAGE);
        return choice;
    }

    public static String passwordLoginInputDialog() {
        String choice;
        choice = JOptionPane.showInputDialog(null, "Please enter your password",
                "Marketplace Login", JOptionPane.QUESTION_MESSAGE);
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

  ///////////////////////////////

    public static String customerMainInputDialog() {
        String choice;
        String[] customerMenu = {"1. Sort products", "2. Search for a product", "3. Display Dashboard",
                "4. Select a product", "5. Account Settings", "6. View purchase history", "Exit"};
        choice = (String) JOptionPane.showInputDialog(null, "What would you like to do?",
                "Customer Menu", JOptionPane.QUESTION_MESSAGE, null, customerMenu, customerMenu[0]);
        return choice;
    }
  
  
  
}
