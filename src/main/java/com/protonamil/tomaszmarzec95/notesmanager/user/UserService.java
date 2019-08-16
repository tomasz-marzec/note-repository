package com.protonamil.tomaszmarzec95.notesmanager.user;

import com.protonamil.tomaszmarzec95.notesmanager.security.JwtTokenProvider;
import com.protonamil.tomaszmarzec95.notesmanager.security.Role;
import com.protonamil.tomaszmarzec95.notesmanager.security.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private JwtTokenProvider tokenProvider;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepo.findByUsername(username);
        if(user != null) {
            List<GrantedAuthority> authorities = getUserAuthority(user.getRoleSet());
            return buildUserForAuthentication(user, authorities);
        } else {
            throw new UsernameNotFoundException("username not found");
        }
    }

    private List<GrantedAuthority> getUserAuthority(Set<Role> userRoles) {
        Set<GrantedAuthority> roles = new HashSet<>();
        userRoles.forEach((role) -> {
            roles.add(new SimpleGrantedAuthority(role.getRole()));
        });

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>(roles);
        return grantedAuthorities;
    }

    public List<GrantedAuthority> loadUserAuthority(String username) {
        User user = userRepo.findByUsername(username);
        return  getUserAuthority(user.getRoleSet());
    }

    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role userRole = roleRepo.findByRole("USER");
        user.setRoleSet(new HashSet<>(Arrays.asList(userRole)));
        userRepo.save(user);
    }

    private UserDetails buildUserForAuthentication(User user, List<GrantedAuthority> authorities) {
        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(), authorities);
    }

    public User getUserByTokenHeader(String bearerToken) {
        bearerToken = bearerToken.substring(7, bearerToken.length());
        String username = tokenProvider.getUsername(bearerToken);
        return userRepo.findByUsername(username);
    }

}
