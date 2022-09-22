package com.example.servingwebcontent.controllers;

import com.example.servingwebcontent.models.apimodel.AuthenticateSingleton;
import com.example.servingwebcontent.models.apimodel.User;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    AuthenticateSingleton authenticateInstance = AuthenticateSingleton.getInstance();

    @GetMapping("/")
    public String index(Model model) {

        if (!authenticateInstance.getIsUserAuthenticated()) {
            return "redirect:/login";
        }

        User user = authenticateInstance.getUser();

        String userName = user.getUserName();
        model.addAttribute("userName", userName);

        if (user.isCustomer()) {
            model.addAttribute("role", "Customer");
        } else if (user.isReceptionist()) {
            model.addAttribute("role", "Administrator");
        } else if (user.isHealthcareWorker()) {
            model.addAttribute("role", "Health Care Worker");
        } else {
            model.addAttribute("role", "No Role");
        }

        return "homePage";
    }
}
