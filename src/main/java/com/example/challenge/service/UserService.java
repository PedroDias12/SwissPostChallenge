package com.example.challenge.service;

import com.example.challenge.exception.ServiceException;
import com.example.challenge.exception.UserNotFoundException;
import com.example.challenge.mapper.UserMapper;
import com.example.challenge.model.User;
import com.example.challenge.repository.UserRepository;
import com.example.challenge.resource.UserResource;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;

@AllArgsConstructor
@Service
public class UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public long createUser(final UserResource resource) throws ServiceException {
        validateUser(resource);
        User user = userMapper.resourceToUser(resource);
        user = userRepository.save(user);
        return user.getId();
    }

    private void validateUser(UserResource resource) throws ServiceException {
        if (!isEmailValid(resource.getEmail())){
            throw new ServiceException("User with invalid email " + resource.getEmail(), HttpStatus.BAD_REQUEST);
        }
        if (getUserByEmail(resource.getEmail()).isPresent()) {
            throw new ServiceException("User with email " + resource.getEmail() + " already exists", HttpStatus.CONFLICT);
        }
    }

    private boolean isEmailValid(String email) {
        String regexRFC5322 = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        return Strings.isNotBlank(email) &&
                Pattern.compile(regexRFC5322).matcher(email).matches();
    }

    public UserResource getUser(Long id) throws UserNotFoundException {
        return userMapper.userToResource(
                userRepository.findById(id)
                        .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"))
        );
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
