package com.udacity.jwdnd.course1.cloudstorage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LogoutController {
    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        // Perform any additional logout logic if needed

        // Invalidate the session and redirect to the login page
        request.getSession().invalidate();
        return "redirect:/login";
    }
}
