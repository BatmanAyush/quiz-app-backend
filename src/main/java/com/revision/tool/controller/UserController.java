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
            return "Working Successfully!";
    }

    @PostMapping("/sign")
    public boolean signin(@RequestBody Client cli ){
      Client client = userService.save(cli);
      return true;
    }

    @CrossOrigin("*")
    @PostMapping("/login")
    public ClientToken login(@RequestBody Client loginRequest) { // Renamed method and parameter
        // This line handles authentication. It will throw an exception on failure.
        System.out.println("Enter Login Controller");
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getName(), loginRequest.getPassword())
        );

        // If we reach here, authentication was successful.
        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
        Client authenticatedUser = userPrinciple.getUser(); // This is the Client object from the DB with the correct ID
        String token = jwtService.generateToken(authenticatedUser);

        return new ClientToken(token);
    }
    @CrossOrigin("*")
    @PostMapping("/checkToken")
    public boolean  getToken(@RequestBody ClientToken clientToken){
        return true;
    }
}
