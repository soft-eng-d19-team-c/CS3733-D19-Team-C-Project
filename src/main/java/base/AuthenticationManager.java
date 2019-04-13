package base;

import org.jasypt.util.password.BasicPasswordEncryptor;

/**
 * This class is meant to be used as a sort of "Facade" to manage
 * password authentication and make it easy to change the encryption method without
 * introducing a large amount of technical debt.
 * @author Ryan LaMarche
 */
public final class AuthenticationManager {
    private BasicPasswordEncryptor passwordEncryptor;

    /**
     * Creates a new instanec of an AuthenticationManager
     * @Author Ryan LaMarche
     */
    public AuthenticationManager() {
        this.passwordEncryptor = new BasicPasswordEncryptor();
    }

    /**
     * Encrypt a password.
     * @param textPassword the plain text password to encrypt
     * @return the encrypted password.
     */
    public String encryptPassword(String textPassword) {
        return this.passwordEncryptor.encryptPassword(textPassword);
    }

    public boolean checkPassword(String textPassword, String encryptedPassword) {
        return this.passwordEncryptor.checkPassword(textPassword, encryptedPassword);
    }
}
