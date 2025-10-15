package com.uni_store.store.Admin;


import com.uni_store.store.User.UserRole;

public record AdminUserUpdateDto(

        UserRole role,
        Boolean isActive
) {
}
