package com.example.eventrese.util;


import java.util.List;

public interface OnEventSelectedListener {
    public void onEventSelected(Integer position, List<Event> events, String source);
}