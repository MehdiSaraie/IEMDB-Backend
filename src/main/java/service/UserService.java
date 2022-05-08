package service;

import domain.IEMDB;
import domain.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserService {
  @RequestMapping(value = "users/logout", method = RequestMethod.POST,
    produces = MediaType.APPLICATION_JSON_VALUE)
  public void Logout() {
    IEMDB.getInstance().setLoggedInUser(null);
  }

  @RequestMapping(value = "users/login", method = RequestMethod.POST,
    produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> Login(
    @RequestParam (value = "email") String email,
    @RequestParam (value = "password") String password) {
    IEMDB iemdb = IEMDB.getInstance();
    User user = iemdb.getUserByEmail(email);
    if (user == null) {
      return new ResponseEntity<>("User Not Found", HttpStatus.BAD_REQUEST);
    } else if (!user.getPassword().equals(password)) {
      return new ResponseEntity<>("Wrong Password", HttpStatus.BAD_REQUEST);
    } else {
      iemdb.setLoggedInUser(user);
      return new ResponseEntity<>(HttpStatus.OK);
    }
  }
}
