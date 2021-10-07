package com.example.demo1.Controllers;

import org.springframework.web.bind.annotation.GetMapping;

public class Controller {

    @GetMapping("/home")
    public String getPageInfo() {
        return "This has just began";
    }

}
