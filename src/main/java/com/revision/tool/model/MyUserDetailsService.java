package com.revision.tool.model;

import com.revision.tool.dao.ClientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private ClientRepo repo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Client user = repo.findByName(username);
        if(user==null){
           throw new UsernameNotFoundException("NotFound Username");
        }
        return new UserPrinciple(user);
    }
}
