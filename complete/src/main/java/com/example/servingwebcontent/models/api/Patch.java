package com.example.servingwebcontent.models.api;

import java.io.IOException;
import java.util.List;

public abstract class Patch {
    public abstract String patchApi(List<String> thingsToPatch, String description) throws IOException, InterruptedException;
}
