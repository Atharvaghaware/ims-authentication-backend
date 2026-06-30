	package com.ims.impl;
	
	import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ims.dto.LoginRequest;

import com.ims.dto.RegisterRequest;
import com.ims.entity.User;
import com.ims.dto.LoginResponse;
import com.ims.jwt.JwtService;
import com.ims.repository.UserRepository;
import com.ims.service.UserService;
	@Service
	public class UserServiceImpl implements UserService {
	
	    @Autowired
	    private UserRepository userRepository;
	
	    @Autowired
	    private PasswordEncoder passwordEncoder;
	    
	    @Autowired
	    private JwtService jwtService;
	    
	   
	    @Override
	    public String register(RegisterRequest request) {
	
	        if (userRepository.existsByEmail(request.getEmail())) {
	            return "Email already exists";
	        }
	        
	
	        User user = new User();
	
	        user.setFullName(request.getFullName());
	        user.setEmail(request.getEmail());
	        user.setPassword(passwordEncoder.encode(request.getPassword()));
	        user.setRole(request.getRole());
	        user.setStatus(true);
	
	        userRepository.save(user);
	
	        return "User Registered Successfully";
	    }
	
		
		
	    @Override
	    public LoginResponse login(LoginRequest request) {

	        User user = userRepository.findByEmail(request.getEmail())
	                .orElseThrow(() -> new RuntimeException("Invalid Email"));

	        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
	            throw new RuntimeException("Invalid Password");
	        }

	        String token = jwtService.generateToken(user.getEmail());

	        return new LoginResponse(
	                token,
	                user.getFullName(),
	                user.getRole().name()
	        );
	    }
	}