package repository.interfaces;

import model.User;

public interface IUserRepository extends IRepository<User, String> {
    User login(User user);
}
