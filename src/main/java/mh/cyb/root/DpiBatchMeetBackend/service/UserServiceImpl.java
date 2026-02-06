package mh.cyb.root.DpiBatchMeetBackend.service;

import mh.cyb.root.DpiBatchMeetBackend.domain.User;
import mh.cyb.root.DpiBatchMeetBackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder; // Placeholder import if needed

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApprovalService approvalService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
            ApprovalService approvalService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.approvalService = approvalService;
    }

    @Override
    public User registerUser(User user) {
        // Encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Set default values
        user.setEnabled(false);
        user.setAccountNonLocked(true);

        // Set default role
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.getRoles().add(mh.cyb.root.DpiBatchMeetBackend.domain.Role.MEMBER);
        }

        User savedUser = userRepository.save(user); // Save first to get ID

        // Create approval request
        approvalService.createRequest(savedUser);

        return savedUser;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
