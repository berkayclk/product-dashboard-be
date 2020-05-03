package com.paydaybank.dashboard.web.api;

import com.paydaybank.dashboard.dto.model.UserDTO;
import com.paydaybank.dashboard.enums.UserRoles;
import com.paydaybank.dashboard.service.UserService;
import com.paydaybank.dashboard.web.helper.ResponseHelper;
import com.paydaybank.dashboard.web.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {
    
    @Autowired
    UserService userService;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity getAllUsers(){

        List<UserDTO> users = userService.finAll();

        Response<List<UserDTO>> response = ResponseHelper.ok(users);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN') || @methodAuthorizeService.isAuthorizedUser( #userId, authentication )")
    public ResponseEntity getUserById( @PathVariable UUID userId ){

        Optional<UserDTO> user = userService.findById(userId);

        Response<UserDTO> response = ResponseHelper.ok(user.orElse(null));
        return ResponseEntity.ok()
                .location(URI.create("/users/"+userId))
                .body(response);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity createNewUser(@RequestBody UserDTO userDTO ){

        userDTO.addRole(UserRoles.ADMIN);
        Optional<UserDTO> createdUser = userService.create( Optional.of(userDTO) );

        Response<UserDTO> response = ResponseHelper.ok(createdUser.orElse(null));
        return ResponseEntity.created( URI.create(createdUser.map(dto -> "/users/" + dto.getId()).orElse("")))
                                .body(response);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN') || (#userDTO != null && @methodAuthorizeService.isAuthorizedUser( #userDTO.getId(), authentication) )")
    public ResponseEntity updateUser(@RequestBody UserDTO userDTO ){

        Optional<UserDTO> updatedUser = userService.update( Optional.of(userDTO) );

        Response<UserDTO> response = ResponseHelper.ok(updatedUser.orElse(null));
        return ResponseEntity.ok()
                .location( URI.create("/users/"+ updatedUser.get().getId() ) )
                .body(response);
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN') || @methodAuthorizeService.isAuthorizedUser( #userId, authentication )")
    public ResponseEntity updateUser(@PathVariable UUID userId ){
        userService.deleteById( userId );
        return ResponseEntity.ok().body(ResponseHelper.ok());
    }
}
