package com.example.lab5.service.impl;


//import com.example.lab5.entity.PasswordResetToken;
import com.example.lab5.entity.Users;
//import com.example.lab5.repository.PasswordResetTokenRepository;
import com.example.lab5.repository.UsersRepository;
import com.example.lab5.service.UsersService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public class UsersServiceImpl implements UsersService, UserDetailsService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    // CONSTRUCTOR
    public UsersServiceImpl(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();

    }


    // GET LIST OF USERS
    @Override
    public List<Users> getAllUsers() {
        return usersRepository.findAllUsersWithTasks();
    }


    // GET USER BY ID
    @Override
    public Users getUserById(Long id) {
        return usersRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found for id " + id));
    }


    // DELETE USER
    @Override
    public void deleteUserById(Long id) {
        this.usersRepository.deleteById(id);
    }


    // SAVE USER
    @Override
    public Users save(Users user) {
        if (user.getPassword() != null && !user.getPassword().startsWith("$2a$")) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return usersRepository.save(user);
    }


    // FIND USER BY USERNAME
    @Override
    public Users findByUsername(String username) {
        return usersRepository.findByUsername(username);
    }


    // CHECK IF USER EXISTS
    @Override
    public boolean existsByUsername(String username) {
        return usersRepository.findByUsername(username) != null;
    }



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = usersRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            user.getPassword(),
            List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }

    @Override
    public void updateRole(Long userId, String role) {
        Users user = usersRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setRole(role);
        usersRepository.save(user);
    }

    @Override
    public Users findByEmail(String email) {
        return usersRepository.findByEmail(email);
    }

    @Override
    public String generateResetToken(Long userId) {
        Users user = usersRepository.findByUserId(userId);
        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        usersRepository.save(user);
        return token;
    }


    @Override
    public Users findByResetToken(String resetToken) {
        return usersRepository.findByResetToken(resetToken);
    }

    @Override
    public void resetPassword(Long userId, String newPassword) {
        Users user = usersRepository.findByUserId(userId);
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        usersRepository.save(user);
    }

    @Override
    public Users findByUserId(Long Id) {
        return usersRepository.findById(Id).orElseThrow(() -> new RuntimeException("User not found"));
    }

}
