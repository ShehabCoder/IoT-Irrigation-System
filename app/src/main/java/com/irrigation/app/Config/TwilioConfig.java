package com.irrigation.app.Config;


import com.twilio.Twilio;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import jakarta.annotation.PostConstruct;


@Configuration
public class TwilioConfig {

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @PostConstruct
    public void initializeTwilio() {
        Twilio.init(accountSid, authToken);
        System.out.println("Twilio initialized with Account SID: " + accountSid);
    }
}
