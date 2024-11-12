import java.util.Random;

public class DebitCard {
    private String cardType;
    private String cardNumber;
    private String pin;
    private double withdrawLimit;
    private double transferLimit;
    private double transferLimitOwnAccount;
    private double depositLimit;
    private double depositLimitOwnAccount;
    // Constructor
    public DebitCard(String cardType, String pin) {
        this.cardType = cardType;
        this.cardNumber = generateCardNumber();
        this.pin = pin;
        setLimits();
    }


    private String generateCardNumber() {
        Random random = new Random();
        StringBuilder cardNumber = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            cardNumber.append(random.nextInt(10));
        }
        return cardNumber.toString();
    }
    //setter and getter
    public void setLimits() {
        switch (cardType) {
            case "Mastercard Platinum":
                this.withdrawLimit = 20000;
                this.transferLimit = 40000;
                this.transferLimitOwnAccount = 80000;
                this.depositLimit = 100000;
                this.depositLimitOwnAccount = 200000;
                break;
            case "Mastercard Titanium":
                this.withdrawLimit = 10000;
                this.transferLimit = 20000;
                this.transferLimitOwnAccount = 40000;
                this.depositLimit = 100000;
                this.depositLimitOwnAccount = 200000;
                break;
            case "Mastercard":
                this.withdrawLimit = 5000;
                this.transferLimit = 10000;
                this.transferLimitOwnAccount = 20000;
                this.depositLimit = 100000;
                this.depositLimitOwnAccount = 200000;
                break;
        }
    }

    public void setWithdrawLimit(double withdrawLimit) {
        this.withdrawLimit = withdrawLimit;
    }

    public void setTransferLimit(double transferLimit) {
        this.transferLimit = transferLimit;
    }

    public void setTransferLimitOwnAccount(double transferLimitOwnAccount) {
        this.transferLimitOwnAccount = transferLimitOwnAccount;
    }

    public void setDepositLimit(double depositLimit) {
        this.depositLimit = depositLimit;
    }

    public void setDepositLimitOwnAccount(double depositLimitOwnAccount) {
        this.depositLimitOwnAccount = depositLimitOwnAccount;
    }

    public String getCardType() {
        return cardType;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getPin() {
        return pin;
    }

    public double getWithdrawLimit() {
        return withdrawLimit;
    }

    public double getTransferLimit() {
        return transferLimit;
    }

    public double getTransferLimitOwnAccount() {
        return transferLimitOwnAccount;
    }

    public double getDepositLimit() {
        return depositLimit;
    }

    public double getDepositLimitOwnAccount() {
        return depositLimitOwnAccount;
    }

    public boolean verifyPin(String pin) {
        return this.pin.equals(pin);
    }

    public boolean verifySavingPin(String pin) {
        return this.pin.equals(pin);
    }
}