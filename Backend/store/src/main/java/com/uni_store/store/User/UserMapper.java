package com.uni_store.store.User;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponseDto userToUserResponceDto(User user);

    User userRegistrationDtoToUser(UserRegistrationDto user);

    // This is the new method for updating an existing entity from a DTO
    void updateUserFromDto(UserUpdateDto dto, @MappingTarget User user);
}
