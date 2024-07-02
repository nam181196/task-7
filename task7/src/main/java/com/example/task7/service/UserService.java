package com.example.task7.service;

import com.example.task7.model.User;
import com.example.task7.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Page<User> findUsersStartingWithH(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        return userRepository.findByFirstNameStartingWith("H", pageable);
    }

    public Page<User> findUsersContainingH(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        return userRepository.findByFirstNameContaining("H", pageable);
    }
    public Page<User> findUsersByName(String firstName, String middleName, String lastName, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        return userRepository.findByFirstNameAndMiddleNameAndLastName(firstName, middleName, lastName, pageable);
    }

    public Page<User> findUsersByIds(List<Long> ids, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        return userRepository.findByIdIn(ids, pageable);
    }

    //Phương thức chuyển tiền từ A -> B
    public User addMoney(Long id, Long amount) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setMoney(user.getMoney() + amount);
        return userRepository.save(user);
    }

    @Transactional
    public void transferMoney(Long fromUserId, Long toUserId, Long amount) {
        User fromUser = userRepository.findById(fromUserId)
                .orElseThrow(() -> new RuntimeException("User not found: " + fromUserId));
        User toUser = userRepository.findById(toUserId)
                .orElseThrow(() -> new RuntimeException("User not found: " + toUserId));

        if (fromUser.getMoney() < amount) {
            throw new RuntimeException("Insufficient funds for user " + fromUserId);
        }

        fromUser.setMoney(fromUser.getMoney() - amount);
        toUser.setMoney(toUser.getMoney() + amount);

        userRepository.save(fromUser);
        userRepository.save(toUser);
    }
}