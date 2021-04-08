package pl.iwa.ciezkamonika.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.iwa.ciezkamonika.model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findById(long id);
    //User findByUsername(String username);
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
}
