package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/signup")
public class SignupController {

    private final UserService userService;

    public SignupController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String signupView(){ //Any name would work here since the GetMapping Component
        return "signup";
    }

    @PostMapping()
    public String signupUser(@ModelAttribute User user, Model model){
        String signupError = null;

        if(!userService.isUsernameAvailable(user.getUsername())){
            signupError = "The Username already exist";
        }

        if(signupError==null){
            int rowsAdded = userService.creaseUser(user);
            if (rowsAdded<0){
                signupError = "There was an error signing you up. Please try again.";
            }
        }

        // Add to the model, so that the frontend can receive and use those variables
        if(signupError==null){
            model.addAttribute("signupSuccess", true);
        } else {
            model.addAttribute("signupError",signupError);
        }

        //rendering signup html page
        return "signup";
    }


}
