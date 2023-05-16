package com.example.app.service.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Node {
    private String nodeId;
    private String pid;
    private String text;
    private String email;
}
