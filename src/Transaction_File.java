import java.io.*;
import java.util.ArrayList;
import java.util.List;

 //Appends transaction data to a specified file
public class Transaction_File {
    public static void appendTransaction(String filePath, String transactionData) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(transactionData);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}