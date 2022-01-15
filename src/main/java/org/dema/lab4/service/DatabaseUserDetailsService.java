package org.dema.lab4.service;

import org.dema.lab4.entity.User;
import org.dema.lab4.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class DatabaseUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return user;
    }

    public void registerUser(User user) throws UserExistsException {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UserExistsException();
        }
        User target = new User();
        target.setUsername(user.getUsername());
        final PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        target.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(target);
    }
}
