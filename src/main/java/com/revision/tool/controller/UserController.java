package com.revision.tool.controller;

import com.revision.tool.dao.ClientRepo;
import com.revision.tool.model.Client;
import com.revision.tool.model.ClientToken;
import com.revision.tool.model.UserPrinciple;
import com.revision.tool.service.JwtService;
import com.revision.tool.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@CrossOrigin("*")
public class UserController {

    @Autowired
    private UserService userService;

   @Autowired
   private AuthenticationManager authenticationManager;
    @Autowired
    private ClientRepo repo;
    @Autowired
    private JwtService jwtService;


    @CrossOrigin("*")
    @GetMapping("/test")
    public String test(){
            return "Actions Reflected Sucessfully";
    }

    @PostMapping("/sign")
    public boolean signin(@RequestBody Client cli ){
      Client client = userService.save(cli);
      return true;
    }

    @CrossOrigin("*")
    @PostMapping("/login")
    public ClientToken login(@RequestBody Client loginRequest) {
        System.out.println("Enter Login Controller");
        System.out.println("Attempting login for user: " + loginRequest.getName());

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getName(), loginRequest.getPassword())
            );

            System.out.println("✅ Authentication successful!");
            UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
            Client authenticatedUser = userPrinciple.getUser();
            String token = jwtService.generateToken(authenticatedUser);

            System.out.println("✅ Token generated: " + token);
            return new ClientToken(token);

        } catch (Exception e) {
            System.out.println("❌ Authentication failed!");
            System.out.println("❌ Error type: " + e.getClass().getName());
            System.out.println("❌ Error message: " + e.getMessage());
            e.printStackTrace();
            throw e; // Re-throw to let Spring Security handle it
        }
    }
    @CrossOrigin("*")
    @PostMapping("/checkToken")
    public boolean  getToken(@RequestBody ClientToken clientToken){
        return true;
    }
}
