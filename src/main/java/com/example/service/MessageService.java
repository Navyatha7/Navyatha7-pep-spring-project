package com.example.service;

import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MessageService {

    @Autowired
    private MessageRepository msgRepo;

    @Autowired
    private AccountRepository accRepo;

    public Message createMessage(Message msg) {
        String text = msg.getMessageText();

        if (text == null || text.trim().isEmpty() || text.length() > 255) {
            return null;
        }

        if (!accRepo.existsById(msg.getPostedBy())) {
            return null;
        }

        return msgRepo.save(msg);
    }

    public Optional<Message> getMessageById(int messageId) {
        return msgRepo.findById(messageId);
    }

    public List<Message> getAllMessages() {
        return msgRepo.findAll()
                .stream()
                .sorted((a, b) -> Integer.compare(a.getMessageId(), b.getMessageId()))
                .collect(Collectors.toList());
    }

    public List<Message> getMessagesByAccountId(int accountId) {
        return msgRepo.findByPostedBy(accountId);
    }

    public int deleteMessage(int messageId) {
        if (!msgRepo.existsById(messageId)) {
            return 0;
        }

        msgRepo.deleteById(messageId);
        return 1;
    }

    public int updateMessage(int messageId, Message updatedMsg) {
        Optional<Message> existingMsg = msgRepo.findById(messageId);

        if (existingMsg.isEmpty()) {
            return -1;
        }

        String newText = updatedMsg.getMessageText();

        if (newText == null || newText.trim().isEmpty() || newText.length() > 255) {
            return -2;
        }

        Message original = existingMsg.get();
        original.setMessageText(newText);

        msgRepo.save(original);
        return 1;
    }
}
