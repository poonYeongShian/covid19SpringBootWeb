package com.example.servingwebcontent.models.apimodel;

import java.util.Collection;

import com.example.servingwebcontent.models.api.Get;
import com.example.servingwebcontent.models.api.APIfactory;
import com.example.servingwebcontent.models.api.UserFactory;

public class AuthenticateSingleton {

    private static AuthenticateSingleton instance;
    private boolean isUserAuthenticated = false;
    private User user = null;

    private AuthenticateSingleton() {
    };

    public static AuthenticateSingleton getInstance() {
        // Lazy Initialisation
        if (instance == null) {
            instance = new AuthenticateSingleton();
        }
        return instance;
    }

    public boolean getIsUserAuthenticated() {
        return isUserAuthenticated;
    }

    public void authenticate(String username) {
        isUserAuthenticated = true;

        APIfactory<User> userFactory = new UserFactory(System.getenv("API_KEY"));

        Get<User> userGet = userFactory.createGet();
        try {
            // User Collection
            Collection<User> userCollection = userGet.getApi();

            for (User user2 : userCollection) {
                if (user2.getUserName().toLowerCase().equals(username.toLowerCase())) {
                    user = user2;
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void deauthenicate() {
        isUserAuthenticated = false;
        user = null;
    }

    public User getUser() {
        return user;
    }
}
