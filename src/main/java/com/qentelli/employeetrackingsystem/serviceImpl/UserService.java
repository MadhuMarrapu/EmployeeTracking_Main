package com.qentelli.employeetrackingsystem.serviceImpl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.qentelli.employeetrackingsystem.config.JwtUtil;
import com.qentelli.employeetrackingsystem.entity.Person;
import com.qentelli.employeetrackingsystem.models.client.request.LoginUserRequest;
import com.qentelli.employeetrackingsystem.models.client.response.LoginUserResponse;

@Service
public class UserService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PersonService personService;

    @Autowired
    private Map<String, Map<String, String>> adminMetadata;

    public LoginUserResponse loginByEmail(LoginUserRequest loginUser) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginUser.getUserName(), loginUser.getPassword()
            )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String username = userDetails.getUsername();
        String token = jwtUtil.generateToken(username);

        LoginUserResponse response = new LoginUserResponse();
        response.setUserName(username);
        response.setAcessToken(token);

        String rawRole = userDetails.getAuthorities().iterator().next().getAuthority();
        String role = rawRole.startsWith("ROLE_") ? rawRole.substring(5) : rawRole;
        response.setRole(role);

        // ✅ First try admin metadata
        if (adminMetadata.containsKey(username)) {
            Map<String, String> meta = adminMetadata.get(username);
            response.setFirstName(meta.get("firstName"));
            response.setLastName(meta.get("lastName"));
        } else {
            // 🔁 Fallback to DB lookup
            Person person = personService.getPersonEntity(username);
            if (person != null) {
                response.setFirstName(person.getFirstName());
                response.setLastName(person.getLastName());
            } else {
                // Optional: handle missing person data explicitly
                response.setFirstName("Unknown");
                response.setLastName("User");
            }
        }

        return response;
    }
}