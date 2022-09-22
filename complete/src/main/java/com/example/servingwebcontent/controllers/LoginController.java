package com.example.servingwebcontent.controllers;

import com.example.servingwebcontent.domain.UserLogin;
import com.example.servingwebcontent.models.apimodel.AuthenticateSingleton;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Controller
public class LoginController {

    AuthenticateSingleton authenticateInstance = AuthenticateSingleton.getInstance();

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("UserLogin", new UserLogin());
        return "login";
    }

    // Check for Credentials
    @PostMapping("/login")
    public String postlogin(@ModelAttribute("userLogin") UserLogin login, Model model)
            throws IOException, InterruptedException {
        String username = login.getUserName();
        String password = login.getPassword();

        // Check the login form using the API
        if (checkLoginFromAPI(username, password)) {

            authenticateInstance.authenticate(username);

            model.addAttribute("userName", username);
            model.addAttribute("password", password);

            return "redirect:/";
        }

        model.addAttribute("error", "Incorrect Username & Password");
        return "login";
    }

    @GetMapping("/logout")
    public String logout(Model model) {

        authenticateInstance.deauthenicate();

        model.addAttribute("UserLogin", new UserLogin());
        return "login";
    }

    public boolean checkLoginFromAPI(String userName, String password) throws IOException, InterruptedException {
        boolean flag = false;
        String rootUrl = "https://fit3077.com/api/v2";
        String usersUrl = rootUrl + "/user";
        String usersLoginUrl = usersUrl + "/login";

        String jsonString = "{" +
                "\"userName\":\"" + userName + "\"," +
                "\"password\":\"" + password + "\"" +
                "}";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create(usersLoginUrl + "?jwt=true")) // Return a JWT so we can
                                                                                              // use it in Part 5 later.
                .setHeader("Authorization", System.getenv("API_KEY"))
                .header("Content-Type", "application/json") // This header needs to be set when sending a JSON request
                                                            // body.
                .POST(HttpRequest.BodyPublishers.ofString(jsonString))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            flag = true;
        }

        return flag;
    }
}