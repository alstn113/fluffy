package com.fluffy.external.email;


public record SendEmailRequest(String to, String subject, String body) {
}
