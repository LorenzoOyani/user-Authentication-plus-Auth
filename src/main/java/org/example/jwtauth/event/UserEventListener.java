package org.example.jwtauth.event;

import org.example.jwtauth.entity.User;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class UserEventListener implements ApplicationListener<UserRegistrationEvent> {

    private JavaMailSender javaMailSender;

    @Override
    public void onApplicationEvent(UserRegistrationEvent event) {
        sendEmailTo(event.getUser());
    }

    @Async
    protected void sendEmailTo(User user) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(user.getEmail());
        simpleMailMessage.setSubject("welcome  "+user.getUsername());
        simpleMailMessage.setText("Welcome user with  username  " + user.getUsername());

        javaMailSender.send(simpleMailMessage);
    }
}
