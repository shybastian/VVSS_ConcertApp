package repository.repositories;

import model.User;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Unit tests for the UserRepository.
 *
 * @author Sonya
 * @since 28.03.2020
 * */
public class UserRepositoryTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test(expected = Exception.class)
    public void addInvalidUsernameEmpty() throws Exception {
        UserRepository userRepository = new UserRepository();
        User user = new User("", "Passwor5d");
        userRepository.add(user);
        expectedException.expectMessage("Username cannot be empty!");
    }

    @Test(expected = Exception.class)
    public void addInvalidUsernameNotUnique() throws Exception {
        UserRepository userRepository = new UserRepository();
        User user = new User("admin", "Passwor7d");
        userRepository.add(user);
        expectedException.expectMessage("This username already exists!");
    }

    @Test(expected = Exception.class)
    public void addInvalidPasswordEmpty() throws Exception {
        UserRepository userRepository = new UserRepository();
        User user = new User("newUser", "");
        userRepository.add(user);
        expectedException.expectMessage("Password cannot be empty");
    }

    @Test(expected = Exception.class)
    public void addInvalidPasswordTooShort() throws Exception {
        UserRepository userRepository = new UserRepository();
        User user = new User("newUser", "12a");
        userRepository.add(user);
        expectedException.expectMessage("Password must have at least 4 characters!");
    }

    @Test(expected = Exception.class)
    public void addInvalidPasswordNoDigit() throws Exception {
        UserRepository userRepository = new UserRepository();
        User user = new User("newUser", "aaaa");
        userRepository.add(user);
        expectedException.expectMessage("Password should contain at least one digit and only alphanumeric characters!");
    }

    @Test
    public void addUserValid() throws Exception {
        UserRepository userRepository = new UserRepository();
        User user = new User("newUser1","MyFancy5Password");
        userRepository.add(user);
        User insertedUser = userRepository.findOne("newUser1");
        assertThat(insertedUser.getUsername(), is("newUser1"));
    }
}
