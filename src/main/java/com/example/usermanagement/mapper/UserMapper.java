package com.example.usermanagement.mapper;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.example.usermanagement.dtos.auth.RegisterReqDto;
import com.example.usermanagement.dtos.user.UserReqDto;
import com.example.usermanagement.dtos.user.UserResDto;
import com.example.usermanagement.dtos.user.UserUpdateReqDto;
import com.example.usermanagement.entity.User;

@Mapper(config = BaseMapperConfig.class)
public interface UserMapper {

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "expired", ignore = true)
    @Mapping(target = "locked", ignore = true)
    @Mapping(target = "credentialsExpired", ignore = true)
    User toEntity(RegisterReqDto req);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "expired", ignore = true)
    @Mapping(target = "locked", ignore = true)
    @Mapping(target = "credentialsExpired", ignore = true)
    User toEntity(UserReqDto req);

    UserResDto toDto(User user);

    List<UserResDto> toDtoList(List<User> users);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "expired", ignore = true)
    @Mapping(target = "locked", ignore = true)
    @Mapping(target = "credentialsExpired", ignore = true)
    void updateEntityFromDto(UserUpdateReqDto req, @MappingTarget User user);
}
