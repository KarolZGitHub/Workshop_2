

import entity.User;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.Arrays;

public class UserDao {


    public User createUser(User user) {
        final String CREATE_USER_QUERY = "INSERT INTO users (userName, email, password) VALUES (?, ?, ?)";
        try (Connection con = DbUtil.getConnection()) {
            PreparedStatement prep = con.prepareStatement(CREATE_USER_QUERY, Statement.RETURN_GENERATED_KEYS);
            prep.setString(1, user.getName());
            prep.setString(2, user.getEmail());
            prep.setString(3, hashPassword(user.getPassword()));
            prep.executeUpdate();
            ResultSet resultSet = prep.getGeneratedKeys();
            if (resultSet.next()) {
                user.setId(resultSet.getInt(1));
            }
            return user;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String hashPassword(String password) {
        String first = BCrypt.hashpw(password, BCrypt.gensalt());
        return first;
    }

    public User read(int userId) {
        final String READ_USER_QUERY = "SELECT * FROM users WHERE id=?";
        try (Connection con = DbUtil.getConnection()) {
            PreparedStatement preparedStatement = con.prepareStatement(READ_USER_QUERY);
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt(1));
                user.setName(resultSet.getString(2));
                user.setEmail(resultSet.getString(3));
                user.setPassword(resultSet.getString(4));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return null;
    }

    public User update(User user) {
        final String UPDATE_USER_QUERY = "UPDATE users SET username = ? , email = ?,  password = ? WHERE id = ?";
        try (Connection connection = DbUtil.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USER_QUERY);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, hashPassword(user.getPassword()));
            preparedStatement.setInt(4, user.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void printUser(User user) {
        int id = user.getId();
        String name = user.getName();
        String email = user.getEmail();
        String read = "id= " + id + " name= " + name + " email= " + email;
        System.out.println("User {" + read + "}");
    }

    public void delete(int userId) {
        try (Connection con = DbUtil.getConnection()) {
            final String DELETE_USER_QUERY = "DELETE FROM users WHERE id = ?";
            PreparedStatement preparedStatement = con.prepareStatement(DELETE_USER_QUERY);
            preparedStatement.setInt(1, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private User[] addToArray(User u, User[] users) {

        User[] tmpUsers = Arrays.copyOf(users, users.length + 1); // Tworzymy kopię tablicy powiększoną o 1.

        tmpUsers[users.length] = u; // Dodajemy obiekt na ostatniej pozycji.

        return tmpUsers; // Zwracamy nową tablicę.
    }
    public User[] findAll(){
        final String SELECT_USERS_QUERY = "SELECT * FROM users";
        try (Connection con = DbUtil.getConnection()){
            User[] users = new User[0];
            PreparedStatement preparedStatement = con.prepareStatement(SELECT_USERS_QUERY);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                User user = new User();
                user.setId(resultSet.getInt(1));
                user.setName(resultSet.getString(2));
                user.setEmail(resultSet.getString(3));
                user.setPassword(user.getPassword());
                users=addToArray(user,users);
            }return users;

        } catch (SQLException e) {
            e.printStackTrace();
        }return null;
    }
}

