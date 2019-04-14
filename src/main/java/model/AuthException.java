package model;

public class AuthException extends Throwable {
    public AuthException(String s) {
        System.err.println(s);
    }
}
