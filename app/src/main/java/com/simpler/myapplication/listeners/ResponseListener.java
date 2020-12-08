package com.simpler.myapplication.listeners;


import com.simpler.myapplication.model.JsonCommentsModel;

import java.util.List;

public abstract class ResponseListener {
    public abstract void onResponse(List<JsonCommentsModel> response);
}
