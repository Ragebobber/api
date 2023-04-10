package com.blatant.api.service;

import com.blatant.api.dto.RegisterRequest;
import com.blatant.api.entity.User;
import com.blatant.api.entity.UserRole;
import com.blatant.api.entity.UserStatus;
import com.blatant.api.exception.RegistrationException;
import com.blatant.api.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public UserService(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Transactional
    public User registerUser(@NonNull RegisterRequest request) throws RegistrationException {
        User user =  userRepository.findByLogin(request.getLogin()).orElse(null);
        if(user != null)
            throw new RegistrationException("User already exists" + user);

        user = new User();
        user.setLogin(request.getLogin());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setRole(UserRole.USER);
        user.setStatus(UserStatus.ACTIVE);
        return userRepository.save(user);
    }

}

