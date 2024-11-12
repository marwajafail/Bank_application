
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Duration LOCK_DURATION = Duration.ofMinutes(1);

    public static void main(String[] args) throws IOException {

        Scanner scanner = new Scanner(System.in);
        Login login = new Login();
        boolean loginSuccessful = false;

        while (!loginSuccessful) {
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            UserAccount user = login.authenticate(username, password);
            if (user != null) {
                System.out.println("Login successful!");
                loginSuccessful = true;
                if (user.isBanker()) {
                    System.out.println("Welcome Banker " + user.getUsername());
                    Banker banker = (Banker) user;

                    boolean exit = false;
                    while (!exit) {
                        System.out.println("Choose an option:");
                        System.out.println("1- Add new customer");
                        System.out.println("2- Exit");

                        int choice = scanner.nextInt();
                        scanner.nextLine();

                        switch (choice) {
                            case 1:
                                System.out.print("Enter customer first name: ");
                                String customerFirstName = scanner.nextLine();
                                System.out.print("Enter customer last name: ");
                                String customerLastName = scanner.nextLine();
                                System.out.print("Enter customer password: ");
                                String customerPassword = scanner.nextLine();
                                System.out.print("Enter balance: ");
                                double balance = scanner.nextDouble();
                                scanner.nextLine();

                                banker.addCustomer(login.getUsers(), customerFirstName, customerLastName, customerPassword, balance);
                                System.out.println("New customer added successfully.");
                                break;
                            case 2:
                                exit = true;
                                break;
                            default:
                                System.out.println("Please try again.");
                        }
                    }
                } else if (user.isCustomer()) {
                    System.out.println("Welcome Customer " + user.getUsername());
                    Customer customer = (Customer) user;

                    boolean exit = false;
                    while (!exit) {
                        System.out.println("Choose an option:");
                        System.out.println("1- Create a new account");
                        System.out.println("2- Withdraw money");
                        System.out.println("3- Deposit money");
                        System.out.println("4- Transfer money");
                        System.out.println("5- Display Filtering transactions history");
                        System.out.println("6- Detailed Account Statement");
                        System.out.println("7- Exit");

                        int choice = scanner.nextInt();
                        scanner.nextLine();

                        switch (choice) {
                            case 1:
                                String accountType;
                                boolean hasChecking = customer.getAccounts().stream()
                                        .anyMatch(account -> account.getAccountType().equalsIgnoreCase("checking"));
                                boolean hasSaving = customer.getAccounts().stream()
                                        .anyMatch(account -> account.getAccountType().equalsIgnoreCase("saving"));

                                if (hasChecking && hasSaving) {
                                    System.out.println("You already have both checking and saving accounts.");
                                } else if (!hasChecking || !hasSaving) {
                                    System.out.print("Enter account type (checking/saving): ");
                                    accountType = scanner.nextLine();
                                    if (("checking".equalsIgnoreCase(accountType) && hasChecking) ||
                                            ("saving".equalsIgnoreCase(accountType) && hasSaving)) {
                                        System.out.println("You already have a " + accountType + " account.");
                                        addCardToAccount(customer, scanner, accountType);
                                    } else {
                                        System.out.print("Enter balance: ");
                                        double balance = scanner.nextDouble();
                                        scanner.nextLine();

                                        customer.addAccount(new Account(accountType, balance));
                                        System.out.println(accountType + " account created with balance " + balance);
                                        if ("checking".equalsIgnoreCase(accountType)) {
                                            addCardToAccount(customer, scanner, accountType);
                                        }
                                    }
                                }
                                break;
                            case 2:
                                System.out.print("Enter account type (checking/saving): ");
                                accountType = scanner.nextLine();
                                System.out.print("Enter amount to withdraw: ");
                                double withdrawAmount = scanner.nextDouble();
                                scanner.nextLine();
                                System.out.print("Enter PIN: ");
                                String withdrawPin = scanner.nextLine();

                                customer.withdraw(accountType, withdrawAmount, withdrawPin);
                                break;
                            case 3:
                                System.out.print("Enter account type (checking/saving): ");
                                accountType = scanner.nextLine();
                                System.out.print("Enter amount to deposit: ");
                                double depositAmount = scanner.nextDouble();
                                scanner.nextLine();
                                System.out.print("Enter PIN: ");
                                String depositPin = scanner.nextLine();

                                customer.Deposit(accountType, depositAmount, depositPin);
                                break;
                            case 4:
                                System.out.print("Enter from account type (checking/saving): ");
                                String fromAccountType = scanner.nextLine();
                                System.out.print("Enter to account type (checking/saving): ");
                                String toAccountType = scanner.nextLine();
                                System.out.print("Enter amount to transfer: ");
                                double transferAmount = scanner.nextDouble();
                                scanner.nextLine();
                                System.out.print("Enter PIN: ");
                                String transferPin = scanner.nextLine();

                                customer.transfer(fromAccountType, toAccountType, transferAmount, transferPin);
                                break;
                            case 5:
                                displayTransactionHistory(customer, scanner);
                                break;
                            case 6:
                                displayDetailedAccountStatement(customer);
                                break;

                            case 7:
                                exit = true;
                                break;
                            default:
                                System.out.println("Please try again.");
                        }
                    }
                }
            } else {
                System.out.println("Invalid username or password");
                // Check if account is locked due to failed attempts
                UserAccount lockedAccount = login.getUsers().stream()
                        .filter(u -> u.getUsername().equals(username))
                        .findFirst().orElse(null);

                if (lockedAccount != null && lockedAccount.isAccountLocked()) {
                    System.out.println("Account locked due to multiple failed attempts. Try again later.");
                    try {
                        Thread.sleep(LOCK_DURATION.toMillis());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    private static void addCardToAccount(Customer customer, Scanner scanner, String accountType) {
        Account account = customer.getAccountByType(accountType);
        if (account != null) {
            if (accountType.equalsIgnoreCase("checking")) {
                System.out.println("Select checking card type:");
                System.out.println("1- Mastercard");
                System.out.println("2- Mastercard Titanium");
                System.out.println("3- Mastercard Platinum");

                int cardChoice = scanner.nextInt();
                scanner.nextLine();

                String cardType = "";
                switch (cardChoice) {
                    case 1:
                        cardType = "Mastercard";
                        break;
                    case 2:
                        cardType = "Mastercard Titanium";
                        break;
                    case 3:
                        cardType = "Mastercard Platinum";
                        break;
                    default:
                        System.out.println("Invalid card type selected.");
                        return;
                }

                System.out.print("Enter PIN: ");
                String pin = scanner.nextLine();

                account.addDebitCard(cardType, pin);
                System.out.println(cardType + " card added with number: " + account.getDebitCard().getCardNumber());
            } else if (accountType.equalsIgnoreCase("saving")) {
                System.out.println("Select saving card type:");
                System.out.println("1-  Mastercard");
                System.out.println("2-  Mastercard Titanium");
                System.out.println("3-  Mastercard Platinum");

                int cardChoice = scanner.nextInt();
                scanner.nextLine();

                String cardType = "";
                switch (cardChoice) {
                    case 1:
                        cardType = "Mastercard";
                        break;
                    case 2:
                        cardType = "Mastercard Titanium";
                        break;
                    case 3:
                        cardType = "Mastercard Platinum";
                        break;
                    default:
                        System.out.println("Invalid card type selected.");
                        return;
                }

                System.out.print("Enter Saving PIN: ");
                String pin = scanner.nextLine();

                account.addDebitCard(cardType, pin);
                System.out.println(cardType + " card added with number: " + account.getDebitCard().getCardNumber());
            } else {
                System.out.println("Invalid account type.");
            }
        } else {
            System.out.println("Account Type not found or cannot add card to saving account.");
        }
    }

    private static void displayTransactionHistory(Customer customer) throws IOException {
        customer.TransactionHistory();
    }

    private static void displayTransactionHistory(Customer customer, Scanner scanner) throws IOException {
        System.out.println("Choose filter option:");
        System.out.println("1- Today");
        System.out.println("2- Yesterday");
        System.out.println("3- Last 7 days");
        System.out.println("4- Last 30 days");

        int choice = scanner.nextInt();
        scanner.nextLine();

        String filter;
        switch (choice) {
            case 1:
                filter = "today";
                break;
            case 2:
                filter = "yesterday";
                break;
            case 3:
                filter = "last 7 days";
                break;
            case 4:
                filter = "last 30 days";
                break;
            default:
                System.out.println("Invalid choice");
                return;
        }

        customer.transactionHistory(filter);
    }



    // display Detailed Account Statement for customer
    private static void displayDetailedAccountStatement(Customer customer) {
        double totalBalance = 0;
        System.out.println("Detailed Account Statement for " + customer.getUsername());
        System.out.println("------------------------------------------------------");
        for (Account account : customer.getAccounts()) {
            System.out.println("Account Type: " + account.getAccountType());
            System.out.println("Current Balance: $" + account.getBalance());
            System.out.println("Transactions:");
            try {
                List<String> transactions = ReadFile.readTransactionFile(ReadFile.getTransactionFilePath(customer.getUsername()));
                double amount = account.getBalance();
                for (String transaction : transactions) {
                    String[] transactionData = transaction.split(",");
                    if (transactionData.length >= 4 && transactionData[0].equalsIgnoreCase(account.getAccountType())) {
                        totalBalance += amount;
                        System.out.println("- Date: " + transactionData[1] + ", Type: " + transactionData[2] + ", Amount: $" + amount);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("------------------------------------------------------");
        }
        System.out.println("Total Balance Across All Accounts: $" + totalBalance);
    }





}