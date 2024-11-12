

import org.mindrot.jbcrypt.BCrypt;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class Login {
    private List<UserAccount> users;
    // Constructor
    public Login(List<UserAccount> users) {
        this.users = users;
    }
    //setter and getter
    public List<UserAccount> getUsers() {
        return users;
    }

    public void setUsers(List<UserAccount> users) {
        this.users = users;
    }

    public Login() {
        this.users = ReadFile.readUserFile();
    }
    // use login file to verify password
    private boolean verifyPasswordFromLoginFile(String username, String inputPassword) {
        try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\marooy990\\jdi\\projects\\Project1-master\\login.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] loginInfo = line.split(",");
                if (loginInfo.length == 2 && loginInfo[0].equals(username)) {
                    return BCrypt.checkpw(inputPassword, loginInfo[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    //Validate the credentials (username and password) and determine whether the user can log in successfully or not
    public UserAccount authenticate(String username, String password) {
        Optional<UserAccount> userOptional = users.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();

        if (userOptional.isPresent()) {
            UserAccount user = userOptional.get();
            if (!user.isAccountLocked() && verifyPasswordFromLoginFile(username, password)) {
                user.resetFailedLoginAttempts();
                return user;
            } else {
                user.incrementFailedLoginAttempts();// increment the number of failed attempts upon failure
                //Verify that his account has not been locked
                if (user.getFailedLoginAttempts() >= UserAccount.getMaxFailedAttempts()) {
                    user.setLastFailedLoginTime(LocalDateTime.now());
                }
            }
        }
        return null;
    }



}
