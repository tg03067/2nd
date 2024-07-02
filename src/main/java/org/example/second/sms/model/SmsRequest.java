package org.example.second.sms.model;

import lombok.Data;
import lombok.Setter;

@Data
public class SmsRequest {
    private String from;
    @Setter
    private String to;
    @Setter
    private String message;

    // Getters and setters
    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public String getMessage() {
        return message;
    }

}