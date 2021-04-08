package pl.iwa.ciezkamonika.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.iwa.ciezkamonika.model.Role;
import pl.iwa.ciezkamonika.model.RoleName;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName roleName);
}
