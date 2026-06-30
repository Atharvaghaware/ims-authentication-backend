package com.ims.service;

public interface PasswordService {

    String forgotPassword(String email);

    String resetPassword(String token, String newPassword);

}	