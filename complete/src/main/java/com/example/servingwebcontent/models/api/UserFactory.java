package com.example.servingwebcontent.models.api;

import com.example.servingwebcontent.models.apimodel.User;

public class UserFactory implements APIfactory<User> {
    private String api;

    public UserFactory(String api) {
        this.api = api;
    }

    @Override
    public Get<User> createGet() {
        return new UserGet(this.api);
    }

    @Override
    public Post createPost() {
        return null;
    }

    @Override
    public Delete createDelete() {
        return null;
    }

    @Override
    public Patch createPatch() {
        return null;
    }
}
