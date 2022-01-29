package com.example.application.data.service;

import com.example.application.data.entity.User;
import com.vaadin.flow.component.Component;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    //public record AuthorizedRoute(String route, String name, Class<? extends Component> view) {}

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void authenticate(String email, String password) throws AuthException {
        User user = userRepository.getByEmail(email);
        System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaa");
        System.out.println(user.getPassword());
        System.out.println(password);
        if (user != null && user.getPassword() == password) {

        } else {
            throw new AuthException();
        }
    }

    public class AuthException extends Exception {

    }
}
