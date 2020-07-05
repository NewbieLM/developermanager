package net.maksym.developermanager.messanger;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SmsMessageSender implements MessageSender {

    @Value("${twilio.sid}")
    private String SID;

    @Value("${twilio.token}")
    private String TOKEN;

    @Value("${twilio.phonenumber}")
    private String formPhoneNumber;

    @Override
    public void sendMessage(String toPhoneNumber, String message) {
        Twilio.init(SID, TOKEN);
        Message.creator(new PhoneNumber(toPhoneNumber),
                new PhoneNumber(formPhoneNumber),
                message)
                .create();
    }
}
