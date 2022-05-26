package Filters;

import java.io.IOException;
import java.util.Map;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.AuthenticationHelper;
import entities.User;
import org.springframework.http.HttpStatus;

import io.jsonwebtoken.JwtException;
import repository.UserRepository;


@WebFilter(urlPatterns = "/*")
public class JWTFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        System.out.println("JWT FILTER");

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        System.out.println("print 1");
        boolean statusIsSet = false;
        try {
            System.out.println("print 2");
            String authorization = httpRequest.getHeader("Authorization");
            if (authorization == null || authorization == "") {
                System.out.println("print 3");
                httpResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
                statusIsSet = true;
                System.out.println("JWT FILTER 401");
            } else {
                System.out.println("print 4");
                AuthenticationHelper authenticator = new AuthenticationHelper();
                Map<String, String> payload = authenticator.validateJWT(authorization);
                User user = UserRepository.getInstance().getByEmail(payload.get("email"));
                if (user == null) {
                    throw new Exception();
                }
                request.setAttribute("user", user);
            }
        } catch (JwtException e) {
            System.out.println("print 5");
            e.printStackTrace();
            httpResponse.setStatus(HttpStatus.FORBIDDEN.value());
            statusIsSet = true;
            System.out.println("JWT FILTER 403");
        } catch (Exception e) {
            System.out.println("print 6");
            httpResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            statusIsSet = true;
            System.out.println("JWT FILTER 401");
        }

        if (statusIsSet) {
            return;
        }
        System.out.println("doing filter");
        chain.doFilter(request, response);

        System.out.println("JWT FILTER after");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // ...
    }

    @Override
    public void destroy() {
        // ...
    }
}

