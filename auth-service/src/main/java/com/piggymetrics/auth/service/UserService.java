package com.piggymetrics.auth.service;

import com.piggymetrics.auth.domain.User;

public interface UserService {

	void create(User user);
	// UserService.java
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserDTO getUserById(String userId) {
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new UserNotFoundException("User not found with ID: " + userId);
        }
        return convertToDTO(user);
    }

    public UserDTO createUser(UserDTO userDTO) {
        User user = convertToEntity(userDTO);
        user.setId(UUID.randomUUID().toString()); // Generate a unique ID
        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }

    public void updateUser(String userId, UserDTO userDTO) {
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new UserNotFoundException("User not found with ID: " + userId);
        }
        // Update the user properties
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        // ... other user properties

        userRepository.save(user);
    }

    public void deleteUser(String userId) {
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new UserNotFoundException("User not found with ID: " + userId);
        }
        userRepository.delete(user);
    }

    private UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        // ... other user attributes
        return userDTO;
    }

    private User convertToEntity(UserDTO userDTO) {
        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        // ... other user attributes
        return user;
    }
}



}
	