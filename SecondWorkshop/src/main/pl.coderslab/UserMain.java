import entity.User;

import java.sql.SQLException;

public class UserMain {
    public static void main(String[] args) {
        UserDao userDao = new UserDao();
        User[] users = userDao.findAll();
        for (User u:
             users) {
            userDao.printUser(u);
        }
    }

}

