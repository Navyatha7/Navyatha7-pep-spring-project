package com.example.service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepo;

    public Optional<Account> getAccountById(int id) {
        return accountRepo.findById(id);
    }

    public Account register(Account account) {
        String username = account.getUsername();

        if (username == null || username.trim().isEmpty()) {
            return null;
        }

        boolean userExists = accountRepo.findAll()
                .stream()
                .anyMatch(existing -> username.equals(existing.getUsername()));

        if (userExists) {
            return null;
        }

        return accountRepo.save(account);
    }

    public Account login(Account account) {
        return accountRepo.findAll()
                .stream()
                .filter(existing -> existing.getUsername().equals(account.getUsername())
                        && existing.getPassword().equals(account.getPassword()))
                .findFirst()
                .orElse(null);
    }
}
