import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ReadFile {
    public static final String FILE_PATH = "C:\\Users\\marooy990\\jdi\\projects\\Project1-master\\users.txt";
    public static final String TRANSACTION_PATH = "C:\\Users\\marooy990\\jdi\\projects\\Project1-master\\transactions\\";
    // Read users file
    public static ArrayList<UserAccount> readUserFile() {
        ArrayList<UserAccount> users = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] userInfo = line.split(",");
                if (userInfo.length == 6) {
                    int id = Integer.parseInt(userInfo[0]);
                    String username = userInfo[1];
                    String firstName = userInfo[1];
                    String lastName = userInfo[2];
                    String password = userInfo[3];
                    double balance = Double.parseDouble(userInfo[4]);
                    String userType = userInfo[5];
                    if ("C".equalsIgnoreCase(userType)) {
                        Customer customer = new Customer(username, password, firstName, lastName);
                        Account account = new Account("saving", balance);
                        customer.addAccount(account);
                        users.add(customer);
                    } else if ("B".equalsIgnoreCase(userType)) {
                        Banker banker = new Banker(username, password, firstName, lastName);
                        Account account = new Account("saving", balance);
                        banker.addAccount(account);
                        users.add(banker);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        saveEncryptedPasswords(users); // to save encrypted passwords in login file

        return users;
    }
    //Read users and save encrypted passwords in login file
    public static void saveEncryptedPasswords(List<UserAccount> users) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\marooy990\\jdi\\projects\\Project1-master\\login.txt"))) {
            for (UserAccount user : users) {
                writer.write(user.getUsername() + "," + user.getPassword());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // read to get max user id in users file
    public static int getMaxUserId() {
        int maxId = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] userInfo = line.split(",");
                if (userInfo.length > 0 && !userInfo[0].isEmpty()) {
                    try {
                        int id = Integer.parseInt(userInfo[0]);
                        if (id > maxId) {
                            maxId = id;
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid ID format: " + userInfo[0]);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return maxId;
    }

    public static void appendToFile(String filePath, String data) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(data);
            writer.newLine();
        }
    }
    // get Transaction File Path
    public static String getTransactionFilePath(String username) {
        return TRANSACTION_PATH + username + "_transactions.txt";
    }
    // Read Transaction File
    public static List<String> readTransactionFile(String filePath) throws IOException {
        List<String> transactions = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                transactions.add(line);
            }
        }
        return transactions;
    }


}
