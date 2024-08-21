package org.example.jwtauth.event;

import lombok.Getter;
import org.example.jwtauth.entity.User;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserRegistrationEvent extends ApplicationEvent {

    private final User user;

    public UserRegistrationEvent(Object source, User user) {
        super(source);
        this.user = user;
    }

}
