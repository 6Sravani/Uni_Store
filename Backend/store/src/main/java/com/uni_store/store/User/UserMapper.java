package com.uni_store.store.User;

import com.uni_store.store.Admin.AdminUserUpdateDto;
import com.uni_store.store.Security.UserRegistrationDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UserMapper {

    UserResponseDto userToUserResponseDto(User user);

    User userRegistrationDtoToUser(UserRegistrationDto user);

    // This is the new method for updating an existing entity from a DTO
    void updateUserFromDto(UserUpdateDto dto, @MappingTarget User user);

    void updateUserFromAdminDto(AdminUserUpdateDto dto, @MappingTarget User user);
}
