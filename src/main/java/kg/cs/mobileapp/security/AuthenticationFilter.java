package kg.cs.mobileapp.security;

import com.fasterxml.jackson.databind.ObjectMapper;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import kg.cs.mobileapp.SpringApplicationContext;
import kg.cs.mobileapp.service.UserService;
import kg.cs.mobileapp.shared.dto.UserDto;
import kg.cs.mobileapp.ui.model.request.UserLoginRequestModel;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    private String contentType;

    public AuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException {
        try {
            contentType = req.getHeader("Accept");

            UserLoginRequestModel creds = new ObjectMapper().readValue(req.getInputStream(), UserLoginRequestModel.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getPassword(), new ArrayList<>())
            ); // if it's successful then the below method will be called.

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // below method is called, only when successful authentication is conducted. Otherwise it's called.
    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {

        String userName = ((User) auth.getPrincipal()).getUsername();

        // Jwts - Json Web Token: dependency from build.gradle
        // This dependency generates JSon web token, and will be send back inside Server response. Will be used during
        // future requests that requires authenticated permissions. Server will use this data to identify the user as
        // already authenticated.
        String token = Jwts.builder()
                .setSubject(userName)
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.getTokenSecret())
                .compact();

        // Because userService implements UserDetailsService from Spring Security, it returns UserDetails object
        // which doesn't have "userId" field. So we have to get to the userService Bean, from new Context, and
        // call new "getUser" method which returns us object of type "UserDto"
        UserService userService = (UserService) SpringApplicationContext.getBean("userServiceImpl");
        UserDto userDto = userService.getUser(userName);

        // appending a "Authorization" Header to the Server response on Authentication event.
        res.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token);
        // appending a "User ID" Header to the Server response on Authentication event.
        res.addHeader("User ID", userDto.getUserId());
    }
}
