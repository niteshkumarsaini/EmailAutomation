package com.mail.sender.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Email {

    private String receiverEmail;
    private String companyName;
    private boolean simpleEmail;
    private String resumeType;
}