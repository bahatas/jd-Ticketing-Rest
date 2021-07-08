package com.ticketing.service;

import com.ticketing.entity.ConfirmationToken;
import com.ticketing.exception.TicketingProjectException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;


public interface ConfirmationTokenService {

    ConfirmationToken save(ConfirmationToken confirmationToken);
    void sendEmail(SimpleMailMessage simpleMailMessage);
    ConfirmationToken readByToken(String token) throws TicketingProjectException;
    void delete(ConfirmationToken confirmationToken);



}
