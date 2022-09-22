package com.example.servingwebcontent.models.api;

import org.json.simple.parser.ParseException;

import java.io.IOException;

public abstract class Delete {
    public abstract void deleteApi(String id) throws IOException, InterruptedException, ParseException;
}
