package com.revature.controllers;

import com.revature.daos.RoleDAO;
import com.revature.daos.UserDAO;
import com.revature.dto.AuthDTO;
import com.revature.dto.LoginDTO;
import com.revature.dto.RegisterDTO;
import com.revature.models.User;
import com.revature.security.JwtGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthController {
    private final AuthenticationManager authManager;
    private final UserDAO userDAO;
    private final RoleDAO roleDAO;
    private final PasswordEncoder passEncoder;
    private final JwtGenerator jwtGenerator;

    @Autowired
    public AuthController(AuthenticationManager authManager, UserDAO userDAO,
                          RoleDAO roleDAO, PasswordEncoder passEncoder,
                          JwtGenerator jwtGenerator) {
        this.authManager = authManager;
        this.userDAO = userDAO;
        this.roleDAO = roleDAO;
        this.passEncoder = passEncoder;
        this.jwtGenerator = jwtGenerator;
    }

    @PostMapping("register")
    public ResponseEntity<String> register(@RequestBody RegisterDTO registerDTO) {
        if (userDAO.existsByUsername(registerDTO.getUsername())) {
            return new ResponseEntity<>("That username is already in use.",
                    HttpStatus.BAD_REQUEST);
        }

        User user = new User(
                registerDTO.getFirstName(),
                registerDTO.getLastName(),
                registerDTO.getUsername(),
                passEncoder.encode(registerDTO.getPassword())
        );

        user.setRole(roleDAO.findRoleByTitle("Employee"));

        userDAO.save(user);

        return new ResponseEntity<>("User created successfully.",
                HttpStatus.CREATED);
    }

    @PostMapping("login")
    public ResponseEntity<AuthDTO> login(@RequestBody LoginDTO loginDTO) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getUsername(),
                        loginDTO.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return new ResponseEntity<>(
                new AuthDTO(jwtGenerator.generateToken(authentication)),
                HttpStatus.OK
        );
    }
}