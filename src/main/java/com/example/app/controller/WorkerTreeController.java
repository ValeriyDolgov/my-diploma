package com.example.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WorkerTreeController {
    @GetMapping("/workerTree")
    public String workerTree(){
        return "workerTree";
    }
}
