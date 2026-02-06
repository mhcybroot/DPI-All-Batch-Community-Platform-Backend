package mh.cyb.root.DpiBatchMeetBackend.service;

import mh.cyb.root.DpiBatchMeetBackend.domain.User;
import java.util.Optional;

public interface UserService {
    User registerUser(User user);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
