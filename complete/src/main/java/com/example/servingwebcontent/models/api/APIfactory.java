package com.example.servingwebcontent.models.api;

public interface APIfactory<E> {

    public Get<E> createGet();

    public Post createPost();

    // ---DELETE AND PATCH CAN BE IMPLEMENT IN FUTURE---

    public Delete createDelete();

    public Patch createPatch();
}
