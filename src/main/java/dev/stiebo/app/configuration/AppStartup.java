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

        // create at least one Admin
        if (userRepository.count() == 0) {
            User admin = new User()
                    .setName("Admin")
                    .setUsername("admin")
                    .setPassword(passwordEncoder.encode("admin")); // Demo only!
            Optional<Role> adminRoleOpt = roleRepository.findByName(RoleName.ADMIN);
            if (adminRoleOpt.isPresent()) {
                admin.setRoles(Set.of(adminRoleOpt.get()));
            } else {
                throw new RuntimeException("Role ADMIN not found.");
            }
            userRepository.save(admin);
        }
    }
}
