package com.example.challenge.mapper;

import com.example.challenge.model.User;
import com.example.challenge.resource.UserResource;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResource userToResource(User user);
    User resourceToUser(UserResource userResource);
}
