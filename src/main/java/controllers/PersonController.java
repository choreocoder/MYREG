package controllers;

import models.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/people")
public class PersonController {

    private static final List<Person> mockPeopleDatabase = new ArrayList<>();

    // Static block to pre-populate fake database with initial tracking data
    static {
        mockPeopleDatabase.add(new Leader(1, "Otlile", "Lebelo", "otlile@example.com", "0123456789", "Cape Town", "LEADER", LeaderTier.TRAINEE));
        mockPeopleDatabase.add(new Visitor(2, "John", "Doe", "john@example.com", "0987654321", "Claremont", "VISITOR", LocalDate.now(), 1, FollowUpStatus.PENDING));
    }

    // READ: GET A SINGLE PERSON BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Person> getPersonById(@PathVariable int id){
        for (Person p:mockPeopleDatabase){
            if (p.getId() == id) {
                return ResponseEntity.ok(p);
            }
        }
        return ResponseEntity.notFound().build(); // 404 NOT FOUND

    }

    // READ: GET THE ENTIRE CHURCH ROSTER
    @GetMapping
    public ResponseEntity<List<Person>> getAllPeople(){
        return ResponseEntity.ok(mockPeopleDatabase);
    }


    //CREATE: REGISTER A NEW PERSON
    @PostMapping
    public ResponseEntity<Person> addPerson(@RequestBody Person newPerson){
        if (newPerson == null){
            return ResponseEntity.badRequest().build(); // 400 BAD REQUEST ERROR
        }

        // SIMULATE THE AUTOINCREMENT FROM THE DATABASE
        int nextId = mockPeopleDatabase.size() + 1;
        newPerson.setId(nextId);

        // SAVE TO OUR TEMP DATABASE
        mockPeopleDatabase.add(newPerson);

        // REST STANDARD PRACTICE
        return ResponseEntity.status(HttpStatus.CREATED).body(newPerson);
    }

}