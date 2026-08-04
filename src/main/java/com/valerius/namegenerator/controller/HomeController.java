package com.valerius.namegenerator.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Sends the root URL to the character design wizard.
 */
@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "redirect:/design";
    }
}
