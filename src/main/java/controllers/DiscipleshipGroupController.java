package controllers;

import models.DiscipleshipGroup;
import models.GroupType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/groups") //Changed from /api/people to avoid path collisions
public class DiscipleshipGroupController {


    private static final List<DiscipleshipGroup> mockGroupDatabase = new ArrayList<>();

    static {

        mockGroupDatabase.add(new DiscipleshipGroup(1, "YA_FORLYF", "Athlone", 1));
        mockGroupDatabase.add(new DiscipleshipGroup(2, "Otlile's Cell", "Rondebosch", 2));
    }

    // READ: GET A SINGLE GROUP BY ID
    @GetMapping("/{id}")
    public ResponseEntity<DiscipleshipGroup> getGroupById(@PathVariable int id){
        for (DiscipleshipGroup g : mockGroupDatabase){
            if (g.getGroupId() == id) {
                return ResponseEntity.ok(g);
            }
        }
        return ResponseEntity.notFound().build();
    }

    // READ: GET ALL GROUPS
    @GetMapping
    public ResponseEntity<List<DiscipleshipGroup>> getAllGroups(){
        return ResponseEntity.ok(mockGroupDatabase);
    }

    // CREATE: REGISTER A NEW GROUP
    @PostMapping
    public ResponseEntity<DiscipleshipGroup> addGroup(@RequestBody DiscipleshipGroup newGroup){
        if (newGroup == null){
            return ResponseEntity.badRequest().build();
        }

        mockGroupDatabase.add(newGroup);

        return ResponseEntity.status(HttpStatus.CREATED).body(newGroup);
    }
}