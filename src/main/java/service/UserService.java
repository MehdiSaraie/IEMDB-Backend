package service;

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
public class UserService {
  @RequestMapping(value = "users/logout", method = RequestMethod.GET,
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

  @RequestMapping(value = "users/auth_callback", method = RequestMethod.GET,
          produces = MediaType.APPLICATION_JSON_VALUE)
  public void AuthCallback(@RequestParam (value = "code") String code) throws InvalidKeyException, NoSuchAlgorithmException {
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
    //add user
    System.out.println(userData);
    HashMap header = new HashMap();
    header.put("alg", "HS256");
    header.put("typ", "JWT");
    HashMap payload = new HashMap();
    payload.put("iss", "iemdb");
    payload.put("iat", new Date().getTime());
    payload.put("exp", new Date().getTime()+86400000);
    payload.put("userEmail", userData.get("email"));
    System.out.println(header.toString());
    String encodedHeader = Base64.getUrlEncoder().encodeToString(header.toString().getBytes());
    String encodedPayload = Base64.getUrlEncoder().encodeToString(payload.toString().getBytes());
    String valueToDigest = encodedHeader + "." + encodedPayload;
    String key = "iemdb1401";

    String signature = new HMACSHA256().generateHmacSha256(valueToDigest, key);
    String JWT = encodedHeader + "." + encodedPayload + "." + signature;
    System.out.println(JWT);
  }
}
