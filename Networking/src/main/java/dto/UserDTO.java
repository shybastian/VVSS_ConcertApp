package dto;

import java.io.Serializable;

public class UserDTO implements Serializable {
    String username, password;

    public UserDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
