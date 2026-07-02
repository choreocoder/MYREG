package controllers;

import models.Event;
import models.EventType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private static final List<Event> mockEventDatabase = new ArrayList<>();

    static {

        mockEventDatabase.add(new Event(1, EventType.SUNDAY_SERVICE, "Main Service", LocalDateTime.now(), 0));
        mockEventDatabase.add(new Event(2, EventType.YOUTH, "JOINT SERVICE", LocalDateTime.now(), 1));
    }

    // 1. READ: GET A SINGLE EVENT BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable int id) {
        for (Event e : mockEventDatabase) {
            if (e.getEventId() == id) {
                return ResponseEntity.ok(e);
            }
        }
        return ResponseEntity.notFound().build();
    }

    // 2. READ ALL: GET ALL EVENTS
    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        return ResponseEntity.ok(mockEventDatabase);
    }

    // 3. CREATE: LOG A NEW EVENT (With automatic default date handling)
    @PostMapping
    public ResponseEntity<Event> addEvent(@RequestBody Event newEvent) { // Fixed return type signature
        if (newEvent == null) {
            return ResponseEntity.badRequest().build();
        }

        // Challenge Solved: If the frontend didn't pass a specific timestamp, default it to right now!
        if (newEvent.getEventDate() == null) {
            newEvent.setEventDate(LocalDateTime.now());
        }

        mockEventDatabase.add(newEvent);

        return ResponseEntity.status(HttpStatus.CREATED).body(newEvent);
    }
}