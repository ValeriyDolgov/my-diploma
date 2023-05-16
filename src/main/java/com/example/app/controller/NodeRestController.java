package com.example.app.controller;

import com.example.app.service.UserService;
import com.example.app.service.dto.Node;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("nodes")
@RequiredArgsConstructor
public class NodeRestController {
    private final UserService userService;

    @GetMapping
    public List<Node> nodes() {
        return userService.getListOfNodesForWorkerTree();
    }
}
