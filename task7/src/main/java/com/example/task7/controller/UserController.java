package com.example.task7.controller;

import com.example.task7.model.User;
import com.example.task7.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users-starting-with-h")
    public Page<User> getUsersStartingWithH(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return userService.findUsersStartingWithH(page, size);
    }
    @GetMapping("/users-containing-h")
    public Page<User> getUsersContainingH(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return userService.findUsersContainingH(page, size);
    }
    @GetMapping("/fullname")
    public Page<User> getUsersByName(
            @RequestParam String firstName,
            @RequestParam String middleName,
            @RequestParam String lastName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return userService.findUsersByName(firstName, middleName, lastName, page, size);
    }

    @GetMapping("/by-ids")
    public Page<User> getUsersByIds(
            @RequestParam List<Long> ids,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return userService.findUsersByIds(ids, page, size);
    }

    //Endpoint chuyển tiền
    @PostMapping("/{id}/add-money")
    public ResponseEntity<String> addMoney(@PathVariable Long id, @RequestParam Long amount) {
        try {
            userService.addMoney(id, amount);
            return ResponseEntity.ok("Money added successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/transfer-money")
    public ResponseEntity<String> transferMoney(@RequestParam Long fromUserId, @RequestParam Long toUserId, @RequestParam Long amount) {
        try {
            userService.transferMoney(fromUserId, toUserId, amount);
            return ResponseEntity.ok("Transfer successful");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
