package com.example.reminder_app.repository;

import com.example.reminder_app.model.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReminderRepository extends JpaRepository<Reminder,Long> {

    List<Reminder> findBySentFalseAndDateTimeLessThanEqual(LocalDateTime now);
    List<Reminder> findBySentFalse();
}
