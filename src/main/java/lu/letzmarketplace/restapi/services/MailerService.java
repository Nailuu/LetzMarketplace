package lu.letzmarketplace.restapi.services;

import lu.letzmarketplace.restapi.models.User;
import org.springframework.stereotype.Service;

@Service
public class MailerService {

    public void sendUserVerificationEmail(User user) {
        // TODO: Cancel user creation if failed to send email
    }
}
