package mh.cyb.root.DpiBatchMeetBackend.modules.profile.repository;

import mh.cyb.root.DpiBatchMeetBackend.modules.profile.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findByUserId(Long userId);
}
