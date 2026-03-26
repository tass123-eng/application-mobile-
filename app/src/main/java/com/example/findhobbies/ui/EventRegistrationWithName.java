package com.example.findhobbies.ui;

import com.example.findhobbies.entity.EventRegistration;

public class EventRegistrationWithName {
    public EventRegistration registration;
    public String eventName;

    public EventRegistrationWithName(EventRegistration registration, String eventName) {
        this.registration = registration;
        this.eventName = eventName;
    }
}
