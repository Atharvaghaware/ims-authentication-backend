package com.ims.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ims.dto.ForgotPasswordRequest;
import com.ims.dto.ResetPasswordRequest;
import com.ims.service.PasswordService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {
	    "http://localhost:5173",
	    "https://ims-authentication-frontend.vercel.app"
	})
public class PasswordController {

    @Autowired
    private PasswordService passwordService;

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestBody ForgotPasswordRequest request) {

        return passwordService.forgotPassword(request.getEmail());

    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestBody ResetPasswordRequest request) {

        return passwordService.resetPassword(
                request.getToken(),
                request.getPassword()
        );

    }
}