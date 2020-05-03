package com.paydaybank.dashboard.dto.mapper;

import com.paydaybank.dashboard.dto.model.UserDTO;
import com.paydaybank.dashboard.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper( UserMapper.class );

    UserDTO userToUserDTO(User user);

    User userDtoToUser(UserDTO userDTO);
}
