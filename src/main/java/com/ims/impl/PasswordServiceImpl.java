package com.ims.impl;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ims.entity.PasswordResetToken;
import com.ims.entity.User;
import com.ims.repository.PasswordResetTokenRepository;
import com.ims.repository.UserRepository;
import com.ims.service.EmailService;
import com.ims.service.PasswordService;

@Service
public class PasswordServiceImpl implements PasswordService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    

    @Override
    public String forgotPassword(String email) {

        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty()) {
            return "Email not found";
        }

        User user = optionalUser.get();

        String token = UUID.randomUUID().toString();

        PasswordResetToken resetToken = new PasswordResetToken();

        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(15));

        tokenRepository.save(resetToken);

        String resetLink =
                "https://ims-authentication-frontend.vercel.app/reset-password?token=" + token;

        String subject = "Reset Your Password";

        String body =
                "Hello " + user.getFullName() + ",\n\n"
                        + "Click the link below to reset your password:\n\n"
                        + resetLink
                        + "\n\nThis link will expire in 15 minutes.";

        emailService.sendEmail(
                user.getEmail(),
                subject,
                body
        );

        return "Password reset link has been sent to your email.";

    }

    @Override
    public String resetPassword(String token, String newPassword) {

        Optional<PasswordResetToken> optionalToken =
                tokenRepository.findByToken(token);

        if (optionalToken.isEmpty()) {
            return "Invalid reset token";
        }

        PasswordResetToken resetToken = optionalToken.get();

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {

            tokenRepository.delete(resetToken);

            return "Reset token has expired";

        }

        User user = resetToken.getUser();

        user.setPassword(passwordEncoder.encode(newPassword));

        userRepository.save(user);

        tokenRepository.delete(resetToken);

        return "Password updated successfully";

    }

}