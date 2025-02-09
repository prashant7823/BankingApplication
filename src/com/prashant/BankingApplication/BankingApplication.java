package com.prashant.BankingApplication;

import java.sql.SQLException;
import java.util.Scanner;

public class BankingApplication {
    private static Scanner scanner = new Scanner(System.in);
    private static User currentUser;

    public static void main(String[] args) {
        while (true) {
            System.out.println("1. Login");
            System.out.println("2. Sign Up");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    login();
                    break;
                case 2:
                    signUp();
                    break;
                case 0:
                    System.out.println("Goodbye!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void login() {
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        try {
            currentUser = User.authenticate(username, password);
            if (currentUser != null) {
                System.out.println("Login successful! \n");
                userMenu();
            } else {
                System.out.println("Invalid data.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void signUp() {
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        try {
            if (User.signUp(username, password)) {
                System.out.println("Sign up successful!");
            } else {
                System.out.println("Sign up failed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void userMenu() {
        while (true) {
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer");
            System.out.println("0. Logout");
            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    checkBalance();
                    break;
                case 2:
                    deposit();
                    break;
                case 3:
                    withdraw();
                    break;
                case 4:
                    transfer();
                    break;
                case 0:
                    System.out.println("Logged out.");
                    currentUser = null;
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void checkBalance() {
        System.out.println("Current balance: ₹" + currentUser.getBalance());
    }

    private static void deposit() {
        System.out.print("Enter amount to deposit: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        try {
            currentUser.updateBalance(amount);
            System.out.println("Deposit successful.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void withdraw() {
        System.out.print("Enter amount to withdraw: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        try {
            if (amount > currentUser.getBalance()) {
                System.out.println("Insufficient balance.");
            } else {
                currentUser.updateBalance(-amount);
                System.out.println("Withdrawal successful.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void transfer() {
        System.out.print("Enter recipient username: ");
        String recipientUsername = scanner.nextLine();
        System.out.print("Enter amount to transfer: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        try {
            if (amount > currentUser.getBalance()) {
                System.out.println("Insufficient balance to transfer.");
            } else {
                User recipient = User.getUserByUsername(recipientUsername);
                if (recipient != null) {
                    currentUser.updateBalance(-amount);
                    recipient.updateBalance(amount);
                    System.out.println("Transfer successful.");
                } else {
                    System.out.println("Recipient not found.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}