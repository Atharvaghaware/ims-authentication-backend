package com.ims.service;

import com.ims.dto.LoginRequest;
import com.ims.dto.LoginResponse;
import com.ims.dto.RegisterRequest;

public interface UserService {

    String register(RegisterRequest request);

    LoginResponse login(LoginRequest request);

}