package org.example.second.sms;

import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class SmsService {
    private static final Logger logger = LoggerFactory.getLogger(SmsService.class);

    @Value("${coolsms.api.key}")
    private String apiKey;

    @Value("${coolsms.api.secret}")
    private String apiSecret;

    public void sendSms(String to, String from, String text) {
        Message coolsms = new Message(apiKey, apiSecret);

        HashMap<String, String> params = new HashMap<>();
        params.put("to", to);
        params.put("from", from);
        params.put("type", "SMS");
        params.put("text", text);

        try {
            logger.info("Attempting to send SMS: " + params);
            JSONObject obj = coolsms.send(params);
            logger.info("SMS sent successfully. Response: " + obj.toString());
        } catch (CoolsmsException e) {
            logger.error("Failed to send SMS. Error code: " + e.getCode() + ", Error message: " + e.getMessage());
        }
    }
}