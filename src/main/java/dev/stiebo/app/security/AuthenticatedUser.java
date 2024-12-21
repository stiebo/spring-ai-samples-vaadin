package dev.stiebo.app.security;

import com.vaadin.flow.spring.security.AuthenticationContext;
import dev.stiebo.app.data.User;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class AuthenticatedUser {

    private final AuthenticationContext authenticationContext;

    public AuthenticatedUser(AuthenticationContext authenticationContext) {
        this.authenticationContext = authenticationContext;
    }

    public Optional<User> get() {
        return authenticationContext.getAuthenticatedUser(User.class);
    }

    public void logout() {
        authenticationContext.logout();
    }

}
