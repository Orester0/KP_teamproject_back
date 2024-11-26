package org.example.securitysystem.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class MessageResponse implements Serializable {
    private String message;
}
