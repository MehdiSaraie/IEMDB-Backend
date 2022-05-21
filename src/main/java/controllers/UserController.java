package controllers;

import classes.IEMDB;
import classes.User;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@RestController
public class UserController {
  @RequestMapping(value = "users/logout", method = RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE)
  public void Logout() {
    IEMDB.getInstance().setLoggedInUser(null);
  }

  @RequestMapping(value = "users/signup", method = RequestMethod.POST,
    produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> Signup(
    @RequestParam (value = "name") String name,
    @RequestParam (value = "nickname") String nickname,
    @RequestParam (value = "birthDate") String birthDate,
    @RequestParam (value = "email") String email,
    @RequestParam (value = "password") String password
  ) throws InvalidKeyException, NoSuchAlgorithmException {
    AuthenticationHelper authenticator = new AuthenticationHelper();
    IEMDB iemdb = IEMDB.getInstance();
    User user = iemdb.getUserByEmail(email); //TODO check in database instead
    if (user != null) {
      return new ResponseEntity<>("User Exists", HttpStatus.UNAUTHORIZED);
    } else {
      String passwordHash = authenticator.hashPassword(password);
      //TODO add user in database
      String JWT = authenticator.generateJWTForUser(email);
      return new ResponseEntity<>(JWT, HttpStatus.OK);
    }
  }

  @RequestMapping(value = "users/login", method = RequestMethod.POST,
          produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> Login(
          @RequestParam (value = "email") String email,
          @RequestParam (value = "password") String password) throws InvalidKeyException, NoSuchAlgorithmException {
    AuthenticationHelper authenticator = new AuthenticationHelper();
    IEMDB iemdb = IEMDB.getInstance();
    User user = iemdb.getUserByEmail(email); //TODO check in database instead
    if (user == null) {
      return new ResponseEntity<>("User Not Found", HttpStatus.UNAUTHORIZED);
    } else if (!authenticator.passwordMatches(password, user.getPassword())) {
      return new ResponseEntity<>("Wrong Password", HttpStatus.UNAUTHORIZED);
    } else {
      iemdb.setLoggedInUser(user);
      String JWT = authenticator.generateJWTForUser(email);
      return new ResponseEntity<>(JWT, HttpStatus.OK);
    }
  }

  @RequestMapping(value = "users/auth_callback", method = RequestMethod.GET,
          produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<HashMap> AuthCallback(@RequestParam (value = "code") String code) throws InvalidKeyException, NoSuchAlgorithmException {
    RestTemplate template = new RestTemplate();

    RequestEntity authRequest = RequestEntity.post(URI.create("https://github.com/login/oauth/access_token?client_id=" + "17736afbe983a0754dd1" +
            "&client_secret=" + "d69606cc6507816471a8c5b79d879ab36e96d837" +
            "&code=" + code)).accept(MediaType.APPLICATION_JSON).build();
    ResponseEntity<HashMap> authresponse = template.exchange(authRequest, HashMap.class);

    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "token " + authresponse.getBody().get("access_token"));
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    RequestEntity userInfoRequest = new RequestEntity(headers, HttpMethod.GET, URI.create("https://api.github.com/user"));
    ResponseEntity<HashMap> userDataResponse = template.exchange(userInfoRequest, HashMap.class);
    HashMap<String, String> userData = userDataResponse.getBody();
    System.out.println(userData);
    //TODO add or update user in database
    String JWT = new AuthenticationHelper().generateJWTForUser(userData.get("email"));
    System.out.println(JWT);
    HashMap response = new HashMap();
    response.put("JWT", JWT);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
