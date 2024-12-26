package dev.stiebo.app.configuration;

import dev.stiebo.app.data.Role;
import dev.stiebo.app.data.RoleRepository;
import dev.stiebo.app.data.User;
import dev.stiebo.app.data.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

@Component
public class AppStartup {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AppStartup(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        // create roles
        for (RoleName roleName : RoleName.values()) {
            if (!roleRepository.existsByName(roleName)) {
                Role role = new Role();
                role.setName(roleName);
                roleRepository.save(role);
            }
        }

        // create at least one User
        if (userRepository.count() == 0) {
            User user = new User()
                    .setName("User")
                    .setUsername("user")
                    .setPassword(passwordEncoder.encode("user")); // Demo only!
            Optional<Role> userRoleOpt = roleRepository.findByName(RoleName.USER);
            if (userRoleOpt.isPresent()) {
                user.setRoles(Set.of(userRoleOpt.get()));
            } else {
                throw new RuntimeException("Role USER not found.");
            }
            userRepository.save(user);
        }
    }
}
