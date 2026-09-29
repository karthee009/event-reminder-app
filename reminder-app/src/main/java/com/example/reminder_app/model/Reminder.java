package com.example.reminder_app.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Reminder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String purpose;
    private LocalDateTime dateTime;
    private String email;
    private boolean sent = false;
    private int remindBefore = 0;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }

    public LocalDateTime getDateTime() { return dateTime; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public boolean isSent() { return sent; }
    public void setSent(boolean sent) { this.sent = sent; }

    public int getRemindBefore() { return remindBefore; }
    public void setRemindBefore(int remindBefore) { this.remindBefore = remindBefore; }
}