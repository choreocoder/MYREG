package controllers;

import models.AttendanceRecord;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    // Our temporary in-memory database table for check-ins
    private static final List<AttendanceRecord> mockAttendanceDatabase = new ArrayList<>();

    static {

        // (attendanceId, eventId, personId, status, checkInTime)
        mockAttendanceDatabase.add(new AttendanceRecord(1, 101, 1, "PRESENT", LocalDateTime.now()));
        mockAttendanceDatabase.add(new AttendanceRecord(2, 101, 2, "ABSENT", LocalDateTime.now()));
    }

    // 1. CREATE: Mark someone present/absent (Submit a check-in)
    @PostMapping
    public ResponseEntity<AttendanceRecord> logAttendance(@RequestBody AttendanceRecord record) {
        if (record == null) {
            return ResponseEntity.badRequest().build();
        }

        // Fixed: Using your actual setter name 'setAttendanceId'
        int nextId = mockAttendanceDatabase.size() + 1;
        record.setAttendanceId(nextId);

        // Fallback: If no timestamp was passed by the phone, drop 'now' in right here
        if (record.getCheckInTime() == null) {
            record.setCheckInTime(LocalDateTime.now());
        }

        mockAttendanceDatabase.add(record);
        return ResponseEntity.status(HttpStatus.CREATED).body(record);
    }

    // 2. READ ALL: Get every single attendance log ever taken
    @GetMapping
    public ResponseEntity<List<AttendanceRecord>> getAllRecords() {
        return ResponseEntity.ok(mockAttendanceDatabase);
    }

    // 3. READ BY EVENT: Fetch the roster grid for one specific event
    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<AttendanceRecord>> getAttendanceByEvent(@PathVariable int eventId) {
        List<AttendanceRecord> eventRoster = new ArrayList<>();

        for (AttendanceRecord record : mockAttendanceDatabase) {
            if (record.getEventId() == eventId) {
                eventRoster.add(record);
            }
        }

        return ResponseEntity.ok(eventRoster);
    }
}