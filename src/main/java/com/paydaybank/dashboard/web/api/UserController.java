package com.paydaybank.dashboard.web.api;

import com.paydaybank.dashboard.dto.model.UserDTO;
import com.paydaybank.dashboard.enums.UserRoles;
import com.paydaybank.dashboard.service.UserService;
import com.paydaybank.dashboard.web.helper.ResponseHelper;
import com.paydaybank.dashboard.web.model.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
@Api(value = "User Api", tags = {"User"}, produces = "appliation/json", consumes = "application/json")
public class UserController {
    
    @Autowired
    UserService userService;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @ApiOperation(
            value = "All Users [ADMIN ONLY]",
            notes = "This endpoint returns all users for the admin. You must provide an auth token of the admin user."
    )
    @ApiResponses({
            @ApiResponse(code = 401, message = "Unauthorized! You must provide a valid jwt token!", response = Response.class ),
            @ApiResponse(code = 403, message = "Forbidden! You must provide a jwt token of an ADMIN USER!", response = Response.class )
    })
    public ResponseEntity<Response<List<UserDTO>>> getAllUsers(){

        List<UserDTO> users = userService.finAll();

        Response<List<UserDTO>> response = ResponseHelper.ok(users);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN') || @methodAuthorizeService.isAuthorizedUser( #userId, authentication )")
    @ApiOperation(
            value = "Get User By Id",
            notes = "This endpoint returns the user that has given userId. You must provide a token. Admin can access any user info"
    )
    @ApiResponses({
            @ApiResponse(code = 401, message = "Unauthorized! You must provide a valid jwt token!", response = Response.class ),
            @ApiResponse(code = 403, message = "Forbidden! You must provide a jwt token of the user that can access here!", response = Response.class ),
            @ApiResponse(code = 400, message = "Bad Request! Some invalid entries were provided!", response = Response.class ),
            @ApiResponse(code = 404, message = "Not Found! There is no data matches information you gave!", response = Response.class )
    })
    public ResponseEntity<Response<UserDTO>> getUserById( @PathVariable UUID userId ){

        Optional<UserDTO> user = userService.findById(userId);

        Response<UserDTO> response = ResponseHelper.ok(user.orElse(null));
        return ResponseEntity.ok()
                .location(URI.create("/users/"+userId))
                .body(response);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @ApiOperation(
            value = "Create New Admin User",
            notes = "This endpoint create a new user with admin role. You must provide an auth token of the admin user.",
            code = 201
    )
    @ApiResponses({
            @ApiResponse(code = 401, message = "Unauthorized! You must provide a valid jwt token!", response = Response.class ),
            @ApiResponse(code = 403, message = "Forbidden! You must provide a jwt token of an ADMIN USER!", response = Response.class ),
            @ApiResponse(code = 400, message = "Bad Request! Some invalid entries were provided!", response = Response.class )
    })
    public ResponseEntity createNewUser(@RequestBody UserDTO userDTO ){

        userDTO.addRole(UserRoles.ADMIN);
        Optional<UserDTO> createdUser = userService.create( Optional.of(userDTO) );

        Response<UserDTO> response = ResponseHelper.ok(createdUser.orElse(null));
        return ResponseEntity.created( URI.create(createdUser.map(dto -> "/users/" + dto.getId()).orElse("")))
                                .body(response);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN') || (#userDTO != null && @methodAuthorizeService.isAuthorizedUser( #userDTO.getId(), authentication) )")
    @ApiOperation(
            value = "Update User",
            notes = "This endpoint update the user. User object must contain id field to update. Email, password and roles can not be updated." +
                    " You must provide a token. Admin can update any user info!"
    )
    @ApiResponses({
            @ApiResponse(code = 401, message = "Unauthorized! You must provide a valid jwt token!", response = Response.class ),
            @ApiResponse(code = 403, message = "Forbidden! You must provide a jwt token of the user that can access here!", response = Response.class ),
            @ApiResponse(code = 400, message = "Bad Request! Some invalid entries were provided!", response = Response.class ),
            @ApiResponse(code = 404, message = "Not Found! There is no data matches information you gave!", response = Response.class )
    })
    public ResponseEntity<Response<UserDTO>> updateUser(@RequestBody UserDTO userDTO ){

        Optional<UserDTO> updatedUser = userService.update( Optional.of(userDTO) );

        Response<UserDTO> response = ResponseHelper.ok(updatedUser.orElse(null));
        return ResponseEntity.ok()
                .location( URI.create("/users/"+ updatedUser.get().getId() ) )
                .body(response);
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN') || @methodAuthorizeService.isAuthorizedUser( #userId, authentication )")
    @ApiOperation(
            value = "Delete User",
            notes = "This endpoint delete the user has given userId. You must provide a token. Admin can delete any user!"
    )
    @ApiResponses({
            @ApiResponse(code = 401, message = "Unauthorized! You must provide a valid jwt token!", response = Response.class ),
            @ApiResponse(code = 403, message = "Forbidden! You must provide a jwt token of the user that can access here!", response = Response.class ),
            @ApiResponse(code = 400, message = "Bad Request! Some invalid entries were provided!", response = Response.class ),
            @ApiResponse(code = 404, message = "Not Found! There is no data matches information you gave!", response = Response.class )
    })
    public ResponseEntity updateUser(@PathVariable UUID userId ){
        userService.deleteById( userId );
        return ResponseEntity.ok().body(ResponseHelper.ok());
    }
}
