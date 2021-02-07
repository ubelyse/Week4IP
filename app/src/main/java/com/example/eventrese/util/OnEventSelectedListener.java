package com.example.eventrese.util;


import com.example.eventrese.models.Event;

import java.util.List;

public interface OnEventSelectedListener {
    public void onEventSelected(Integer position, List<Event> events, String source);
}