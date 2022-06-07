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


@WebFilter(urlPatterns = {"/movies/*", "/actors/*", "/comment/*"})
public class JWTFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        boolean statusIsSet = false;
        try {
            String authorization = httpRequest.getHeader("Authorization");
            if (authorization == null || authorization == "") {
                httpResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
                statusIsSet = true;
            } else {
                AuthenticationHelper authenticator = new AuthenticationHelper();
                Map<String, String> payload = authenticator.validateJWT(authorization);
                User user = UserRepository.getInstance().getByEmail(payload.get("email"));
                if (user == null) {
                    throw new Exception();
                }
                request.setAttribute("user", user);
            }
        } catch (JwtException e) {
            e.printStackTrace();
            httpResponse.setStatus(HttpStatus.FORBIDDEN.value());
            statusIsSet = true;
        } catch (Exception e) {
            httpResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            statusIsSet = true;
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

