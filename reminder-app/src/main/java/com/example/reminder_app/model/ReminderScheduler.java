package com.example.reminder_app.model;

import com.example.reminder_app.repository.ReminderRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class ReminderScheduler {

    private final ReminderRepository repo;
    private final JavaMailSender mailSender;

    public ReminderScheduler(ReminderRepository repo, JavaMailSender mailSender) {
        this.repo = repo;
        this.mailSender = mailSender;
    }

    // Every minute run aagum
    @Scheduled(cron = "0 * * * * *")
    public void checkReminders() {
        List<Reminder> pending = repo.findBySentFalse();
        LocalDateTime now = LocalDateTime.now();

        for (Reminder r : pending) {
            LocalDateTime sendAt = r.getDateTime().minusMinutes(r.getRemindBefore());
            if (sendAt.isAfter(now)) continue;

            try {
                SimpleMailMessage msg = new SimpleMailMessage();
                msg.setTo(r.getEmail());
                msg.setSubject("Reminder: " + r.getTitle());
                msg.setText("Purpose: " + r.getPurpose()
                        + "\nEvent time: " + r.getDateTime()
                        + (r.getRemindBefore() > 0
                        ? "\n(This is your " + r.getRemindBefore() + " minute advance reminder)"
                        : ""));
                mailSender.send(msg);

                r.setSent(true);
                repo.save(r);
                System.out.println("Email sent for: " + r.getTitle());
            } catch (Exception e) {
                System.out.println("Email failed: " + e.getMessage());
            }
        }
    }
    }
