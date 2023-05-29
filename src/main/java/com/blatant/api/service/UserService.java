package com.blatant.api.service;

import com.blatant.api.dto.AdminUserResponse;
import com.blatant.api.dto.RegisterRequest;
import com.blatant.api.dto.UserRequest;
import com.blatant.api.dto.UserResponse;
import com.blatant.api.entity.Subscription;
import com.blatant.api.entity.User;
import com.blatant.api.entity.UserRole;
import com.blatant.api.entity.UserStatus;
import com.blatant.api.exception.RegistrationException;
import com.blatant.api.repository.UserRepository;
import com.blatant.api.security.user.UserSecurityService;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Caching;
import org.springframework.transaction.annotation.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional(readOnly = true)
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
    public UserResponse registerUser(@NonNull RegisterRequest request) throws RegistrationException {
        User user =  userRepository.findByLogin(request.getLogin()).orElse(null);
        if(user != null)
            throw new RegistrationException("User already exists" + user);

        user = new User();
        user.setLogin(request.getLogin());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setRole(UserRole.USER);
        user.setStatus(UserStatus.ACTIVE);

        userRepository.save(user);
        return  mapper.map(user,UserResponse.class);
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
    @Transactional
    @Caching(put = {
            @CachePut(value = "UserService::loadUserByUsername",key = "#request.login")
    })
    public AdminUserResponse blockUser( @NonNull UserRequest request){
        User user = userRepository.findByLogin(request.getLogin()).orElseThrow(()-> new UsernameNotFoundException("User not found!"));
        user.setStatus(user.getStatus().equals(UserStatus.ACTIVE) ? UserStatus.BLOCKED : UserStatus.ACTIVE);
        userRepository.save(user);
        return mapper.map(user, AdminUserResponse.class);
    }

    private UserSecurityService getUserSecurity(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UserSecurityService) authentication.getPrincipal();
    }

}

