package controller;

import domain.IEMDB;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserService {
  @RequestMapping(
    value = "users/logout",
    method = RequestMethod.POST,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public void Logout() {
    IEMDB.getInstance().logout();
  }

  @RequestMapping(
    value = "users/login",
    method = RequestMethod.POST,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<String> Login(
    @RequestParam (value = "email") String email,
    @RequestParam (value = "password") String password
  ) {
    try {
      IEMDB.getInstance().login(email, password);
      return new ResponseEntity<>(HttpStatus.OK);
    } catch(Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
}
