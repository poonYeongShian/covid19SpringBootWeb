package com.example.servingwebcontent.models.api;

import com.example.servingwebcontent.models.apimodel.TestingSite;

public class TestingSiteFactory implements APIfactory<TestingSite> {
    private String api;

    public TestingSiteFactory(String api) {
        this.api = api;
    }

    @Override
    public Get<TestingSite> createGet() {
        return new TestingSiteGet(this.api);
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
