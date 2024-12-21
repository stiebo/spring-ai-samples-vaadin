package dev.stiebo.app.data;

import dev.stiebo.app.configuration.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Boolean existsByName(RoleName roleName);
    Optional<Role> findByName(RoleName roleName);
}
