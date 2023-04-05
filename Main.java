package com.company;

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
                    // here user will have more options and will be expanded upon based on their status as seller or customer
                    System.out.println("1. Delete Account\n2. Edit Account\n3. Logout");
                    choice = scan.nextLine();
                    if (choice.equals("3")) ;
                    logged = false;
                }
            } else if (choice.equals("2")) { // user chooses to create a new account
                System.out.println("Enter your email:");
                String email = scan.nextLine();
                System.out.println("Enter your password:");
                String password = scan.nextLine();
                created = createAccount(email, password);
                if (created) {
                    System.out.println("Your new account has been successfully created!");
                } else {
                    System.out.println("There was a problem with creating yuor account.");
                }
            } else { // option 3 to exit program
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
                accountData = line.split(", ", 2);
                System.out.println(accountData[0]);
                System.out.println(accountData[1]);
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

    // method for account creation
    public static boolean createAccount(String email, String password) {
        try {
            BufferedWriter bfw = new BufferedWriter(new FileWriter("Accounts.txt", true));
            bfw.write(String.format("%s, %s\n", email, password));
            bfw.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}