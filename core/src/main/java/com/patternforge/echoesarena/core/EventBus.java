package com.patternforge.echoesarena.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class EventBus {

    private final Map<Class<?>, List<Consumer<Object>>> listeners;

    public EventBus() {
        this.listeners = new HashMap<>();
    }

    @SuppressWarnings("unchecked")
    public <T> void subscribe(Class<T> eventType, Consumer<T> listener) {
        listeners
            .computeIfAbsent(eventType, k -> new ArrayList<>())
            .add((Consumer<Object>) listener);
    }

    public <T> void unsubscribe(Class<T> eventType, Consumer<T> listener) {
        List<Consumer<Object>> handlers = listeners.get(eventType);
        if (handlers != null) {
            handlers.remove(listener);
        }
    }

    public <T> void emit(T event) {
        List<Consumer<Object>> handlers = listeners.get(event.getClass());
        if (handlers != null) {
            for (Consumer<Object> handler : new ArrayList<>(handlers)) {
                handler.accept(event);
            }
        }
    }
}
