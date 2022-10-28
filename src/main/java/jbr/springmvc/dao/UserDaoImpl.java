package jbr.springmvc.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;



import jbr.springmvc.service.JsonConverter;

import org.springframework.jdbc.core.RowMapper;

import jbr.springmvc.model.Login;
import jbr.springmvc.model.User;

public class UserDaoImpl implements UserDao {



  private String databaseName= "lzwUsers";

  public int register(User user){
    redisDB db = new redisDB();
    if (db.isExist(databaseName, user.getUsername())){
      db.disconnect();
      return 0;

    }
    else{
      db.setMap(databaseName,user.getUsername() ,user.toString());
      db.disconnect();
      return 1;
    }

  }

  public User validateUser(Login login){
    redisDB db =new redisDB();
    if (db.isExist(databaseName,login.getUsername())){
      String userString= db.getMap(databaseName,login.getUsername());
      db.disconnect();

      JsonConverter con = new JsonConverter();
      User enteredUser= (User)con.jsonToObject(userString, User.class);

      if(login.getPassword().equals(enteredUser.getPassword())) return enteredUser;
      else return null;

    }
    else{
      db.disconnect();
      return null;
    }

  }

/*
  public int register(User user) {
    String sql = "insert into users values(?,?,?,?,?,?,?)";

    return jdbcTemplate.update(sql, new Object[] { user.getUsername(), user.getPassword(), user.getFirstname(),
        user.getLastname(), user.getEmail(), user.getAddress(), user.getPhone() });
  }

  public User validateUser(Login login) {
    String sql = "select * from users where username='" + login.getUsername() + "' and password='" + login.getPassword()
        + "'";
    List<User> users = jdbcTemplate.query(sql, new UserMapper());

    return users.size() > 0 ? users.get(0) : null;
  }*/

}

class UserMapper implements RowMapper<User> {

  public User mapRow(ResultSet rs, int arg1) throws SQLException {
    User user = new User();

    user.setUsername(rs.getString("username"));
    user.setPassword(rs.getString("password"));
    user.setFirstname(rs.getString("firstname"));
    user.setLastname(rs.getString("lastname"));
    user.setEmail(rs.getString("email"));
    user.setAddress(rs.getString("address"));
    user.setPhone(rs.getInt("phone"));

    return user;
  }
}