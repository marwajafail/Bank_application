
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class Customer extends UserAccount {
    private String firstName;
    private String lastName;
    private List<Account> accounts;
    private String transactionFilePath;
    private DebitCard debitCard;
    // Constructor
    public Customer(String username, String password, String firstName, String lastName) {
        super(username, password, "C");
        this.firstName = firstName;
        this.lastName = lastName;
        this.accounts = new ArrayList<>();
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void addAccount(Account account) {
        this.accounts.add(account);
        this.transactionFilePath = ReadFile.getTransactionFilePath(getUsername());
    }

    public boolean hasAccountType(String accountType) {
        for (Account account : accounts) {
            if (account.getAccountType().equalsIgnoreCase(accountType)) {
                return true;
            }
        }
        return false;
    }
    public Account getAccountByType(String accountType) {
        for (Account account : accounts) {

            if (account.getAccountType().equalsIgnoreCase(accountType)) {
                return account;
            }
        }
        return null;
    }

    public void withdraw(String accountType, double amount, String pin) {
        Account account = getAccountByType(accountType);
        if (account != null) {
            if (accountType.equalsIgnoreCase("checking")) {
                if (account.getDebitCard() != null && account.getDebitCard().verifyPin(pin)) {
                    performWithdraw(account, amount);
                } else {
                    System.out.println("Invalid PIN.");
                }
            } else if (accountType.equalsIgnoreCase("saving")) {
                if (account.getDebitCard() != null && account.getDebitCard().verifySavingPin(pin)) {
                    performWithdraw(account, amount);
                } else {
                    System.out.println("Invalid PIN.");
                }
            }
        } else {
            System.out.println("Account Type not found");
        }
    }

    public void Deposit(String accountType, double amount,String pin) {
        Account account = getAccountByType(accountType);
        if (account != null) {
            if (accountType.equalsIgnoreCase("checking")) {
                if (account.getDebitCard() != null && account.getDebitCard().verifyPin(pin)) {
                    performDeposit(account, amount);
                } else {
                    System.out.println("Invalid PIN.");
                }
            } else if (accountType.equalsIgnoreCase("saving")) {
                if (account.getDebitCard() != null && account.getDebitCard().verifySavingPin(pin)) {
                    performDeposit(account, amount);
                } else {
                    System.out.println("Invalid PIN.");
                }
            }
        } else {
            System.out.println("Account Type not found");
        }
    }

    public void transfer(String fromAccountType, String toAccountType, double amount,String pin) {

        Account fromAccount = getAccountByType(fromAccountType);
        Account toAccount = getAccountByType(toAccountType);

        if (fromAccount != null && toAccount != null) {
            if (fromAccountType.equalsIgnoreCase("checking")) {
                if (fromAccount.getDebitCard() != null && fromAccount.getDebitCard().verifyPin(pin)) {
                    performTransfer(fromAccount, toAccount, amount);
                } else {
                    System.out.println("Invalid PIN for checking account.");
                }
            } else if (fromAccountType.equalsIgnoreCase("saving")) {
                if (fromAccount.getDebitCard() != null && fromAccount.getDebitCard().verifySavingPin(pin)) {
                    performTransfer(fromAccount, toAccount, amount);
                } else {
                    System.out.println("Invalid PIN for saving account.");
                }
            }
        } else {
            System.out.println("One or both accounts not found");
        }
    }

    private void performWithdraw(Account account, double amount) {
        if (account.isActive()) {
            account.withdraw(amount);
            recordTransaction(account.getAccountType(),"Withdrawal", amount, account.getBalance());
        } else {
            System.out.println("Withdrawal failed: account deactivated");
        }
    }

    private void performDeposit(Account account, double amount) {
        account.deposit(amount);
        recordTransaction(account.getAccountType(),"Deposit", amount, account.getBalance());
    }

    private void performTransfer(Account fromAccount, Account toAccount, double amount) {
        fromAccount.withdraw(amount);
        if (fromAccount.isActive()) {
            toAccount.deposit(amount);
            recordTransaction(fromAccount.getAccountType(),"Transfer", amount, toAccount.getBalance());
        } else {
            System.out.println("Transfer failed: account deactivated");
            fromAccount.deposit(amount); // revert the withdrawal
        }
    }


    // ADD Transaction record for each customer specific file
    private void recordTransaction(String accountType,String type, double amount, double postBalance) {
        String transactionData = String.format("%s,%s,%s,%.2f,%.2f",accountType ,new Date(), type, amount, postBalance);
        String transactionFilePath = ReadFile.getTransactionFilePath(getUsername());
        Transaction_File.appendTransaction(transactionFilePath, transactionData);
    }

    //read customer Transaction File
    public void TransactionHistory() {
        try {
            String transactionFilePath = ReadFile.getTransactionFilePath(getUsername());
            List<String> transactions = ReadFile.readTransactionFile(transactionFilePath);
            if (transactions.isEmpty()) {
                System.out.println("No transactions found");
            } else {
                System.out.println("Transaction History for " + getUsername() + ":");
                for (String transaction : transactions) {
                    System.out.println(transaction);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public void transactionHistory(String filter) {
        try {
            String transactionFilePath = ReadFile.getTransactionFilePath(getUsername());
            List<String> transactions = ReadFile.readTransactionFile(transactionFilePath);

            if (transactions.isEmpty()) {
                System.out.println("No transactions found");
            } else {
                System.out.println("Transaction History for " + getUsername() + ":");

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Calendar calendar = Calendar.getInstance();

                switch (filter.toLowerCase()) {
                    case "today":
                        System.out.println("Filtering transactions for today...");
                        filterTransactions(transactions, dateFormat.format(calendar.getTime()), dateFormat);
                        break;
                    case "yesterday":
                        System.out.println("Filtering transactions for yesterday...");
                        calendar.add(Calendar.DATE, -1);
                        filterTransactions(transactions, dateFormat.format(calendar.getTime()), dateFormat);
                        break;
                    case "last 7 days":
                    case "last week":
                        System.out.println("Filtering transactions for last 7 days...");
                        calendar.add(Calendar.DATE, -7);
                        filterTransactions(transactions, dateFormat.format(calendar.getTime()), dateFormat);
                        break;
                    case "last 30 days":
                    case "last month":
                        System.out.println("Filtering transactions for last 30 days...");
                        calendar.add(Calendar.MONTH, -1);
                        filterTransactions(transactions, dateFormat.format(calendar.getTime()), dateFormat);
                        break;
                    default:
                        System.out.println("Invalid filter option");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // filter customer Transactions by compare the date
    private void filterTransactions(List<String> transactions, String filterDate, SimpleDateFormat dateFormat) {
        boolean found = false;
        SimpleDateFormat transactionDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);

        for (String transaction : transactions) {
            String[] transactionData = transaction.split(",");
            if (transactionData.length >= 5) {
                String dateString = transactionData[1].trim();
                try {
                    Date transactionDate = transactionDateFormat.parse(dateString);
                    String formattedTransactionDate = dateFormat.format(transactionDate);

                    if (formattedTransactionDate.equals(filterDate)) {
                        System.out.println(transaction);
                        found = true;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        if (!found) {
            System.out.println("No transactions found for the selected filter");
        }
    }


}


