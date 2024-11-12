import org.mindrot.jbcrypt.BCrypt;

import java.time.Duration;
import java.time.LocalDateTime;

public class UserAccount {
    private String username;
    private String password;
    private String userType;
    private int failedLoginAttempts;
    private LocalDateTime lastFailedLoginTime;
    private static final int MAX_FAILED_ATTEMPTS = 3;
    private static final Duration LOCK_DURATION = Duration.ofMinutes(1);
    // Constructor
    public UserAccount(String username, String password, String userType) {
        this.username = username;
        this.password = hashPassword(password);
        this.userType = userType;
        this.failedLoginAttempts = 0;
        this.lastFailedLoginTime = null;
    }
    //setter and getter
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = hashPassword(password);
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public boolean isCustomer() {
        return "C".equalsIgnoreCase(this.userType);
    }

    public boolean isBanker() {
        return "B".equalsIgnoreCase(this.userType);
    }

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public boolean verifyPassword(String inputPassword) {
        return BCrypt.checkpw(inputPassword, this.password);
    }

    public boolean isAccountLocked() {
        if (failedLoginAttempts >= MAX_FAILED_ATTEMPTS && lastFailedLoginTime != null) {
            LocalDateTime now = LocalDateTime.now();
            return lastFailedLoginTime.plus(LOCK_DURATION).isAfter(now);
        }
        return false;
    }

    public void incrementFailedLoginAttempts() {
        failedLoginAttempts++;
        lastFailedLoginTime = LocalDateTime.now();
    }

    public void resetFailedLoginAttempts() {
        failedLoginAttempts = 0;
        lastFailedLoginTime = null;
    }

    public int getFailedLoginAttempts() {
        return failedLoginAttempts;
    }

    public void setLastFailedLoginTime(LocalDateTime lastFailedLoginTime) {
        this.lastFailedLoginTime = lastFailedLoginTime;
    }

    public static int getMaxFailedAttempts() {
        return MAX_FAILED_ATTEMPTS;
    }
}
