package com.slober.workflow.service;

import com.slober.workflow.dto.UserRequest;
import com.slober.workflow.model.User;
import com.slober.workflow.repository.UserRepository;
import org.springframework.stereotype.Service;
import com.slober.workflow.exception.ResourceNotFoundException;
import com.slober.workflow.exception.EmailAlreadyExistsException;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User createUser(UserRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException(
                    "User with email " + request.getEmail() + " already exists"
            );
        }

        User user = new User();

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());

        return userRepository.save(user);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User with id " + id + " not found"));
    }

    public User updateUser(Long id, UserRequest request) {

        User existingUser = getUserById(id);

        if (userRepository.existsByEmailAndIdNot(request.getEmail(), id)) {
            throw new EmailAlreadyExistsException(
                    "User with email " + request.getEmail() + " already exists"
            );
        }

        existingUser.setFirstName(request.getFirstName());
        existingUser.setLastName(request.getLastName());
        existingUser.setEmail(request.getEmail());

        return userRepository.save(existingUser);
    }

    public void deleteUser(Long id){
        User user = getUserById(id);
        userRepository.delete(user);
    }
}
