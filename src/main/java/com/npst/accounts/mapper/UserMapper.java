package com.npst.accounts.mapper;

import com.npst.accounts.dao.UserDto;
import com.npst.accounts.entity.AppUser;

public class UserMapper {

    public static UserDto mapTOUserDto(AppUser appUser, UserDto userDto) {
        userDto.setUsername(appUser.getUsername());
        userDto.setPassword(appUser.getPassword());
        return userDto;
    }

    public static AppUser mapToUser(UserDto userDto, AppUser appUser) {
        appUser.setUsername(userDto.getUsername());
        appUser.setPassword(userDto.getPassword());
        return appUser;
    }
}
