
import java.util.ArrayList;
import java.util.List;

public class Account {
    private String accountType;
    private double balance;
    private String password;
    private int overdraftCount;
    private boolean isActive;
    private DebitCard debitCard;
    private static final double OverDraftFee = 35.0;
    private static final double OverDraftLimit = -100.0;
    private static final int MaxOverDraft = 2;

    // Constructor
    public Account(String accountType, double balance) {
        this.accountType = accountType;
        this.balance = balance;
        this.password = "";
        this.overdraftCount = 0;
        this.isActive = true;

    }
    //setter and getter
    public void setDebitCard(DebitCard debitCard) {
        this.debitCard = debitCard;
    }

    public DebitCard getDebitCard() {
        return debitCard;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Transaction methods : first check if the account is active the implement
    public void withdraw(double amount) {

        if (!isActive) {
            System.out.println("The account is deactivated as a result of two overdrafts");
            return;
        }
// check negative balance
        if (balance - amount < OverDraftLimit) {
            System.out.println("Cannot withdraw more than $100 if your account balance is negative");
            return;

        }
// check card limit
        if (amount > debitCard.getWithdrawLimit()) {
            System.out.println("Withdraw amount exceeds daily limit");
            return;
        }

        balance -= amount;
// check Overdraft
        if (balance < 0) {
            overdraftCount++;
            balance -= OverDraftFee;
            System.out.println("Overdraft fee of $35 charged. Your balance is: " + balance);

            if (overdraftCount >= MaxOverDraft) {
                isActive = false;
                System.out.println("The account is deactivated as a result of two overdrafts");
            }
        } else {
            System.out.println("Your balance is: " + balance);
        }

    }
    public void deposit(double amount) {
// check card limit
        if (amount > debitCard.getDepositLimit()) {
            System.out.println("Deposit amount exceeds daily limit");
            return;
        }
        balance += amount;
        System.out.println("Your balance is : "+ balance);

        if (balance >= 0 && overdraftCount > 0) {
            overdraftCount = 0;
            isActive = true;
            System.out.println("Account reactivated");
        }
    }



    public boolean isActive() {
        return isActive;
    }

    public int getOverdraftCount() {
        return overdraftCount;
    }

    public void deactivate() {
        this.isActive = false;
    }

    public void activate() {
        this.isActive = true;
    }
    // to add debit card for Account
    public void addDebitCard(String cardType, String pin) {
        this.debitCard = new DebitCard(cardType, pin);
        this.debitCard.setLimits();
    }
}
