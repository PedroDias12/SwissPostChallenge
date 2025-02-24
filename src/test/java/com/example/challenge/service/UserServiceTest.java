package com.example.challenge.service;

import com.example.challenge.exception.ServiceException;
import com.example.challenge.exception.UserNotFoundException;
import com.example.challenge.mapper.UserMapper;
import com.example.challenge.model.User;
import com.example.challenge.repository.UserRepository;
import com.example.challenge.resource.UserResource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static com.example.challenge.TestContants.USER_EMAIL;
import static com.example.challenge.TestContants.USER_NAME;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
        @Mock
        private UserRepository userRepository;
        @Mock
        private UserMapper userMapper;
        @InjectMocks
        private UserService userService;
        private final UserResource userResource = UserResource.builder().email(USER_EMAIL).name(USER_NAME).build();
        private final User user = User.builder().id(1L).email(USER_EMAIL).name(USER_NAME).build();

        @Test
        void testCreateUserSuccess() throws ServiceException {
            when(userMapper.resourceToUser(any())).thenReturn(user);
            when(userRepository.save(any())).thenReturn(user);
            when(userRepository.findByEmail(userResource.getEmail())).thenReturn(Optional.empty());

            long userId = userService.createUser(userResource);

            assertEquals(1L, userId);
        }

        @Test
        void testCreateUserInvalidEmail() {
            UserResource resource = userResource.toBuilder().email("newEmail").build();
            ServiceException exception = assertThrows(ServiceException.class, () -> userService.createUser(resource));

            assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
            assertEquals("User with invalid email " + resource.getEmail(), exception.getMessage());
        }

        @Test
        void testCreateUserEmailAlreadyExists() {
            when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));

            ServiceException exception = assertThrows(ServiceException.class, () -> userService.createUser(userResource));

            assertEquals(HttpStatus.CONFLICT, exception.getHttpStatus());
            assertEquals("User with email " + userResource.getEmail() + " already exists", exception.getMessage());
        }

        @Test
        void testGetUserSuccess() throws UserNotFoundException {
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            when(userMapper.userToResource(any())).thenReturn(userResource);

            UserResource result = userService.getUser(1L);

            assertNotNull(result);
            assertEquals(userResource, result);
        }

        @Test
        void testGetUserUserNotFound() {
            when(userRepository.findById(1L)).thenReturn(Optional.empty());

            UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.getUser(1L));

            assertEquals("User with id 1 not found", exception.getMessage());
        }
}
