package com.example.eventrese.util;


import com.example.eventrese.models.Event;

import java.util.ArrayList;

public interface OnEventSelectedListener {
    public void onEventSelected(Integer position, ArrayList<Event> restaurants, String source);
}