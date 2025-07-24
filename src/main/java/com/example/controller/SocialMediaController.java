package com.example.controller;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SocialMediaController {
    @Autowired
    private MessageService messageService;
    @Autowired
    private AccountService accountService;

    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        Message created = messageService.createMessage(message);
        if (created == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(created);
    }

    @GetMapping("/messages/{id}")
    public ResponseEntity<?> getMessageById(@PathVariable int id) {
        return messageService.getMessageById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.ok().body(""));
    }

    @GetMapping("/messages")
    public List<Message> getAllMessages() {
        return messageService.getAllMessages();
    }

    @GetMapping("/accounts/{accountId}/messages")
    public List<Message> getMessagesByAccount(@PathVariable int accountId) {
        return messageService.getMessagesByAccountId(accountId);
    }

    @DeleteMapping("/messages/{id}")
    public ResponseEntity<Integer> deleteMessage(@PathVariable int id) {
        int count = messageService.deleteMessage(id);
        return ResponseEntity.ok(count);
    }

    @PatchMapping("/messages/{id}")
    public ResponseEntity<?> updateMessage(@PathVariable int id, @RequestBody Message msg) {
        int result = messageService.updateMessage(id, msg);
        if (result == 1) return ResponseEntity.ok(result);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PostMapping("/register")
    public ResponseEntity<Account> register(@RequestBody Account account) {
        Account created = accountService.register(account);
        if (created == null) return ResponseEntity.status(HttpStatus.CONFLICT).build();
        return ResponseEntity.ok(created);
    }

    @PostMapping("/login")
    public ResponseEntity<Account> login(@RequestBody Account account) {
        Account found = accountService.login(account);
        if (found == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        return ResponseEntity.ok(found);
    }
}
