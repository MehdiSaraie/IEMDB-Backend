package controller;

import domain.IEMDB;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

@RestController
public class UserController {
  @RequestMapping(
    value = "users/logout",
    method = RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public void Logout() {
    IEMDB.getInstance().logout();
  }

  @RequestMapping(
    value = "users/signup",
    method = RequestMethod.POST,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<String> Signup(
    @RequestParam (value = "name") String name,
    @RequestParam (value = "nickname") String nickname,
    @RequestParam (value = "birthDate") String birthDate,
    @RequestParam (value = "email") String email,
    @RequestParam (value = "password") String password
  ) throws NoSuchAlgorithmException, InvalidKeyException {
    AuthenticationHelper authenticator = new AuthenticationHelper();
    IEMDB iemdb = IEMDB.getInstance();
    if (iemdb.doesUserExist(email)) {
      return new ResponseEntity<>("User Exists", HttpStatus.UNAUTHORIZED);
    } else {
      String passwordHash = authenticator.hashPassword(password);
      iemdb.signup(name, nickname, birthDate, email, passwordHash);
      String JWT = authenticator.generateJWTForUser(email);
      return new ResponseEntity<>(JWT, HttpStatus.OK);
    }
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
      AuthenticationHelper authenticator = new AuthenticationHelper();
      IEMDB.getInstance().login(email, password);
      String JWT = authenticator.generateJWTForUser(email);
      return new ResponseEntity<>(JWT, HttpStatus.OK);
    } catch(Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }
  }

  @RequestMapping(
    value = "users/auth_callback",
    method = RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<HashMap> AuthCallback(
    @RequestParam (value = "code") String code
  ) throws InvalidKeyException, NoSuchAlgorithmException, ParseException {
    RestTemplate template = new RestTemplate();
    RequestEntity authRequest = RequestEntity.post(URI.create(
            "https://github.com/login/oauth/access_token?client_id=" + "17736afbe983a0754dd1" +
            "&client_secret=" + "d69606cc6507816471a8c5b79d879ab36e96d837" +
            "&code=" + code
    )).accept(MediaType.APPLICATION_JSON).build();
    ResponseEntity<HashMap> authResponse = template.exchange(authRequest, HashMap.class);

    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "token " + authResponse.getBody().get("access_token"));
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    RequestEntity userInfoRequest = new RequestEntity(headers, HttpMethod.GET, URI.create(
            "https://api.github.com/user"
    ));
    ResponseEntity<HashMap> userDataResponse = template.exchange(userInfoRequest, HashMap.class);
    HashMap<String, String> userData = userDataResponse.getBody();
    System.out.println(userData);
    SimpleDateFormat ff = new SimpleDateFormat("yyyy-MM-ddTHH:mm:ssZ");
    SimpleDateFormat ff2 = new SimpleDateFormat("yyyy-MM-dd");
    String date = ff2.format(ff.parse(userData.get("created_at")));
    IEMDB.getInstance().signup(userData.get("name"), userData.get("login"), date, userData.get("email"), "");
    String JWT = new AuthenticationHelper().generateJWTForUser(userData.get("email"));
    System.out.println(JWT);
    HashMap response = new HashMap();
    response.put("JWT", JWT);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
