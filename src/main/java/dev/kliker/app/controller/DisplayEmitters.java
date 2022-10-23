package dev.kliker.app.controller;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public
class DisplayEmitters {

    private final Map<UUID, Set<SseEmitter>> emitters = new ConcurrentHashMap<>();

    SseEmitter add(SseEmitter emitter, UUID displayId) {
        if (!emitters.containsKey(displayId)) {
            emitters.put(displayId, Collections.synchronizedSet(new HashSet<>()));
        }

        emitters.get(displayId).add(emitter);

        emitter.onCompletion(() -> {
            this.emitters.get(displayId).remove(emitter);
        });
        emitter.onTimeout(() -> {
            emitter.complete();
            this.emitters.get(displayId).remove(emitter);
        });
        return emitter;
    }


    private final ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

    private void send(Object obj, UUID displayId) {
        List<SseEmitter> failedEmitters = new ArrayList<>();
        this.emitters.get(displayId).forEach(emitter -> {
            cachedThreadPool.execute(() -> {
                try {
                    emitter.send(obj);
                } catch (Exception e) {
                    emitter.completeWithError(e);
                    failedEmitters.add(emitter);
                }
            });
        });
        this.emitters.get(displayId).removeAll(failedEmitters);
    }

    public void sendSetSlide(UUID displayId, Integer i ) {
        String event = "{\"event\": \"set-slide\", \"payload\": { \"index\": " + i + " } }";
        this.send(event, displayId);
    }

}
