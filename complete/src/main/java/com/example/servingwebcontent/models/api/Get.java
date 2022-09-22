package com.example.servingwebcontent.models.api;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.Collection;

public abstract class Get<E>{
    public abstract Collection<E> getApi() throws IOException, InterruptedException, ParseException;
}
