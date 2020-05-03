package com.paydaybank.dashboard.service;

import com.paydaybank.dashboard.dto.mapper.UserMapper;
import com.paydaybank.dashboard.dto.model.UserDTO;
import com.paydaybank.dashboard.exception.PaydayException;
import com.paydaybank.dashboard.model.User;
import com.paydaybank.dashboard.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(SpringExtension.class)
@DisplayName("UserService should")
@SpringBootTest
public class UserServiceTest {

    @MockBean
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Test
    @DisplayName("returns correctly existing data - findById")
    public void findByIdSuccessTest(){
        User mockUser = new User(UUID.randomUUID(),"fullName", "email", "password", "title", null );
        doReturn(Optional.of(mockUser)).when(userRepository).findById( mockUser.getId() );

        Optional<UserDTO> responseUser = userService.findById( mockUser.getId() );

        Assertions.assertTrue(responseUser.isPresent(), "Data should return with id " + mockUser.getId());

        UserDTO responseUserDTO = responseUser.get();
        Assertions.assertEquals(mockUser.getFullName(), responseUserDTO.getFullName());
        Assertions.assertEquals(mockUser.getEmail(), responseUserDTO.getEmail());
        Assertions.assertEquals(mockUser.getPassword(), responseUserDTO.getPassword());
        Assertions.assertEquals(mockUser.getTitle(), responseUserDTO.getTitle());
        Assertions.assertEquals(Collections.emptyList(), responseUserDTO.getRoles());
    }

    @Test
    @DisplayName("returns empty data for not existing data - findById")
    public void findByIdNotFoundTest(){
        UUID id = UUID.randomUUID();
        doReturn(Optional.empty()).when(userRepository).findById(id);
        Optional<UserDTO> responseUser = userService.findById(id);
        Assertions.assertFalse(responseUser.isPresent(), "Data should not return with id " + id);
    }

    @Test
    @DisplayName("returns all of existing data - findAll")
    public void findAllSuccessTest(){
        User user1 = new User(UUID.randomUUID(),"user fullname 1", "email1", "password", "title1", null );
        User user2 = new User(UUID.randomUUID(),"user fullname 2", "email2", "password", "title2", null );
        doReturn(Arrays.asList(user1,user2)).when(userRepository).findAll();

        List<UserDTO> responseUsers = userService.finAll();

        Assertions.assertEquals(2, responseUsers.size());
    }

    @Test
    @DisplayName("creates new user successfully")
    public void createUserSuccessfullyTest(){
        User mockUser = new User(UUID.randomUUID(),"fullName", "email", "password", "title", null );
        doReturn(mockUser).when(userRepository).save(any());

        UserDTO userToSave = UserMapper.INSTANCE.userToUserDTO(mockUser);
        Optional<UserDTO> responseUser = userService.create( Optional.of(userToSave) );

        Assertions.assertTrue(responseUser.isPresent());

        UserDTO savedUser = responseUser.get();
        Assertions.assertEquals(mockUser.getFullName(), savedUser.getFullName());
        Assertions.assertEquals(mockUser.getEmail(), savedUser.getEmail());
        Assertions.assertEquals(mockUser.getPassword(), savedUser.getPassword());
        Assertions.assertEquals(mockUser.getTitle(), savedUser.getTitle());
        Assertions.assertEquals(Collections.emptyList(), savedUser.getRoles());
    }

    @Test
    @DisplayName("gets fail while updating user does not exists")
    public void updateNotExistingUserTest(){
        UserDTO mockUser = new UserDTO(UUID.randomUUID(),"fullName", "email", "password", "title", null );

        doReturn(Optional.empty()).when(userRepository).findById(mockUser.getId());

        Assertions.assertThrows(PaydayException.EntityNotFoundException.class, () -> {
            userService.update( Optional.of(mockUser) );
        });
    }

    @Test
    @DisplayName("updates existing user successfully")
    public void updatesUserSuccessfullyTest(){
        User mockUser = new User(UUID.randomUUID(),"fullName", "email", "password", "title", null );
        doReturn(Optional.of(mockUser)).when(userRepository).findById(mockUser.getId());

        User mockUserUpdated = new User(mockUser.getId(),"fullName Updated", "email", "password", "titleUpdated", null );
        doReturn(mockUserUpdated).when(userRepository).save(any());

        UserDTO mockUserUpdatedDTO = UserMapper.INSTANCE.userToUserDTO(mockUserUpdated);
        Optional<UserDTO> savedUserDTO = userService.update(Optional.of(mockUserUpdatedDTO));

        Assertions.assertTrue(savedUserDTO.isPresent());

        UserDTO savedUser = savedUserDTO.get();
        Assertions.assertEquals(mockUserUpdated.getFullName(), savedUser.getFullName());
        Assertions.assertEquals(mockUserUpdated.getTitle(), savedUser.getTitle());
    }

    @Test
    @DisplayName("cant update email and password of the user")
    public void dontUpdateEmailAndPasswordTest(){
        User mockUser = new User(UUID.randomUUID(),"fullName", "email", "password", "title", null );
        doReturn(Optional.of(mockUser)).when(userRepository).findById(mockUser.getId());
        doReturn(mockUser).when(userRepository).save(any());

        UserDTO mockUserUpdatedDTO = new UserDTO(mockUser.getId(),"fullName", "emailUpdated", "passwordUpdated", "title", null );
        Optional<UserDTO> savedUserDTO = userService.update(Optional.of(mockUserUpdatedDTO));

        Assertions.assertTrue(savedUserDTO.isPresent());

        UserDTO savedUser = savedUserDTO.get();
        Assertions.assertEquals(mockUser.getEmail(), savedUser.getEmail());
        Assertions.assertEquals(mockUser.getPassword(), savedUser.getPassword());
    }

    @Test
    @DisplayName("gets fail while deleting the user does not exists by id")
    public void deletesNotExistingUserByIdTest(){
        UUID id = UUID.randomUUID();
        doReturn(Optional.empty()).when(userRepository).findById(id);

        Assertions.assertThrows(PaydayException.EntityNotFoundException.class, () -> {
            userService.deleteById(id);
        });

        verify(userRepository, times(1)).findById(id);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("deletes successfully existing user by id")
    public void deletesExistingUserSuccessfullyById(){
        User mockUser = new User(UUID.randomUUID(),"fullName", "email", "password", "title", null );
        doReturn(Optional.of(mockUser)).when(userRepository).findById(mockUser.getId());

        boolean result = userService.deleteById(mockUser.getId());

        verify(userRepository, times(1)).findById(mockUser.getId());
        verify(userRepository, times(1)).deleteById(mockUser.getId());
        verifyNoMoreInteractions(userRepository);

        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("gets fail while deleting the user does not exists by user")
    public void deletesNotExistingUserByUserTest(){

        UserDTO mockUser = new UserDTO(UUID.randomUUID(),"fullName", "email", "password", "title", null );
        doReturn(Optional.empty()).when(userRepository).findById( mockUser.getId() );

        Assertions.assertThrows(PaydayException.EntityNotFoundException.class, () -> {
            userService.delete( Optional.of(mockUser));
        });

        verify(userRepository, times(1)).findById( mockUser.getId() );
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("deletes successfully existing user by user")
    public void deletesExistingUserSuccessfullyByUser(){
        User mockUser = new User(UUID.randomUUID(),"fullName", "email", "password", "title", null );
        doReturn(Optional.of(mockUser)).when(userRepository).findById( mockUser.getId() );

        UserDTO mockUserDTO = UserMapper.INSTANCE.userToUserDTO(mockUser);
        boolean result = userService.delete( Optional.of(mockUserDTO) );

        verify(userRepository, times(1)).findById( mockUser.getId() );
        verify(userRepository, times(1)).deleteById( mockUser.getId() );
        verifyNoMoreInteractions(userRepository);

        Assertions.assertTrue(result);
    }
}
