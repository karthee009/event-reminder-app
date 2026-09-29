package com.example.reminder_app.controller;

import com.example.reminder_app.model.Reminder;
import com.example.reminder_app.repository.ReminderRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reminders")
@CrossOrigin(origins = "*")
public class ReminderController {

    private final ReminderRepository repo;

    public ReminderController(ReminderRepository repo){
        this.repo = repo;
    }


    @GetMapping
    public List<Reminder> findAll(){
       return this.repo.findAll();
    }

    @PostMapping
    public Reminder createReminder(@RequestBody Reminder r){
        return this.repo.save(r);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id){
         this.repo.deleteById(id);
    }
}
