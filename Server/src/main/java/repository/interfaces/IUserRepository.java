package repository.interfaces;

import model.User;

public interface IUserRepository extends IRepository<User, Integer> {
    User login(User user);
}
