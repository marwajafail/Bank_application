

import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Banker extends UserAccount {
    private String firstName;
    private String lastName;
    private List<Account> accounts;
    // Constructor
    public Banker(String username, String password, String firstName, String lastName) {
        super(username, password, "B");
        this.firstName = firstName;
        this.lastName = lastName;
        this.accounts = new ArrayList<>();
    }
    //setter and getter
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

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public void addAccount(Account account) {
        this.accounts.add(account);
    }
    // Add new Customer by Banker and add the customer info in users file
    public void addCustomer(List<UserAccount> users, String firstName, String lastName, String password, double balance) {
        int newId = ReadFile.getMaxUserId() + 1;
        String username = firstName;
        String Password = password;
        Customer newCustomer = new Customer(username, Password, firstName, lastName);
        newCustomer.addAccount(new Account("saving", balance));
        users.add(newCustomer);

        try {
            String userData = String.format("%d,%s,%s,%s,%.2f,C", newId, username, lastName, Password, balance);
            ReadFile.appendToFile(ReadFile.FILE_PATH, userData);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Customer added: " + username);
    }
}
