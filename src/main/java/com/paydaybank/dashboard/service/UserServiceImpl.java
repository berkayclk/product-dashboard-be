package com.paydaybank.dashboard.service;

import com.paydaybank.dashboard.dto.mapper.UserMapper;
import com.paydaybank.dashboard.dto.model.UserDTO;
import com.paydaybank.dashboard.enums.UserRoles;
import com.paydaybank.dashboard.exception.EntityType;
import com.paydaybank.dashboard.exception.ExceptionType;
import com.paydaybank.dashboard.exception.PaydayException;
import com.paydaybank.dashboard.model.User;
import com.paydaybank.dashboard.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    UserRepository userRepository;

    @Override
    public Optional<UserDTO> findById(UUID id) {
        Optional<User> foundUser = userRepository.findById(id);
        return foundUser.map(UserMapper.INSTANCE::userToUserDTO);
    }

    @Override
    public List<UserDTO> finAll() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(UserMapper.INSTANCE::userToUserDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserDTO> create(Optional<UserDTO> userDTO) {
        
        if( !userDTO.isPresent() ) {
            logger.warn("Attempted to create new user with empty parameter!");
            return Optional.empty();
        }

        User user = userDTO.map(UserMapper.INSTANCE::userDtoToUser).get();
        logger.info("A new user creation process begun with {} user email", user.getEmail());

        user.addRole(UserRoles.USER);

        User savedUser = userRepository.save(user);
        UserDTO savedUserDTO = UserMapper.INSTANCE.userToUserDTO(savedUser);

        return Optional.ofNullable(savedUserDTO);
    }

    @Override
    public Optional<UserDTO> update(Optional<UserDTO> userDTO) {
        if( !userDTO.isPresent() || userDTO.get().getId() == null ) {
            logger.warn("Attempted to update user with missing identity info.");
            throw PaydayException.throwException(EntityType.USER, ExceptionType.MISSING_PARAMETER);
        }

        Optional<User> user = userRepository.findById(userDTO.get().getId());
        if( !user.isPresent() ) {
            logger.warn("Attempted to update user that does not exists. Provided Id: {}", userDTO.get().getId());
            throw PaydayException.throwException(EntityType.USER, ExceptionType.NOT_FOUND);
        }

        User foundUser = user.get();
        User updatedUser = userDTO.map( u -> {
            foundUser.setFullName(u.getFullName());
            foundUser.setTitle(u.getTitle());
            return foundUser;
        }).get();

        userRepository.save(updatedUser);

        UserDTO savedUserDTO = UserMapper.INSTANCE.userToUserDTO(updatedUser);
        return Optional.ofNullable(savedUserDTO);
    }

    @Override
    public Boolean deleteById(UUID id) {
        Optional<User> foundUser = userRepository.findById(id);
        if( !foundUser.isPresent() ) {
            logger.warn("Attempted to delete the user that does not exists");
            throw PaydayException.throwException(EntityType.USER, ExceptionType.NOT_FOUND);
        }

        userRepository.deleteById(id);
        return true;
    }

    @Override
    public Boolean delete(Optional<UserDTO> userDTO) {
        if( !userDTO.isPresent() || userDTO.get().getId() == null ) {
            logger.warn("Attempted to delete user with missing identity info.");
            throw PaydayException.throwException(EntityType.USER, ExceptionType.MISSING_PARAMETER);
        }

        Optional<User> user = userRepository.findById(userDTO.get().getId());
        if( !user.isPresent() ) {
            logger.warn("Attempted to delete user that does not exists. Provided Id: {}", userDTO.get().getId());
            throw PaydayException.throwException(EntityType.USER, ExceptionType.NOT_FOUND);
        }

        userRepository.deleteById(user.get().getId());
        return true;
    }
}
