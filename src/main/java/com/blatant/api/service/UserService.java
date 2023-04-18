package com.blatant.api.service;

import com.blatant.api.dto.AdminUserResponse;
import com.blatant.api.dto.RegisterRequest;
import com.blatant.api.dto.UserResponse;
import com.blatant.api.dto.UserSubscriptionResponse;
import com.blatant.api.entity.Subscription;
import com.blatant.api.entity.User;
import com.blatant.api.entity.UserRole;
import com.blatant.api.entity.UserStatus;
import com.blatant.api.exception.RegistrationException;
import com.blatant.api.exception.UserSubscriptionNotFound;
import com.blatant.api.repository.SubscriptionRepository;
import com.blatant.api.repository.UserRepository;
import com.blatant.api.security.user.UserSecurityService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

    private final ModelMapper mapper;

    public UserService(UserRepository userRepository, PasswordEncoder encoder, ModelMapper mapper) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.mapper = mapper;
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

    public UserResponse getCurrnetUser(){
        return mapper.map(getUserSecurity().user(),UserResponse.class);
    }

    public List<Subscription> getCurrentUserSubs() {

        User user = userRepository.findById(getUserSecurity().user().getId())
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));

        return user.getUserSubscription();
    }

    public List<AdminUserResponse> getAllUsers(){
        List<User> allUsers = userRepository.findAll();
        return allUsers.stream().map(elem -> mapper.map(elem,AdminUserResponse.class)).toList();

    }
    private UserSecurityService getUserSecurity(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UserSecurityService) authentication.getPrincipal();
    }

}

