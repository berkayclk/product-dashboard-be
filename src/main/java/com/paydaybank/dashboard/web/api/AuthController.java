package com.paydaybank.dashboard.web.api;

import com.paydaybank.dashboard.dto.model.UserDTO;
import com.paydaybank.dashboard.service.UserService;
import com.paydaybank.dashboard.helper.ResponseHelper;
import com.paydaybank.dashboard.dto.response.Response;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@Api(value = "Auth Api", tags = {"Auth"}, produces = "appliation/json", consumes = "application/json")
public class AuthController implements AuthInterface {

    @Autowired
    UserService userService;

    @GetMapping(value = "/whoami", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(
            value = "Who Am I",
            notes = "This endpoint returns information of the authenticated user. You must provide an auth token."
    )
    @ApiResponses({
            @ApiResponse(code = 401, message = "Unauthorized", response = Response.class ),
            @ApiResponse(code = 404, message = "Not Found! There is no active user matches token you gave!", response = Response.class )
    })
    public ResponseEntity<Response<UserDTO>> whoAmI() {
        String authUserId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<UserDTO> foundUser = userService.findById( UUID.fromString(authUserId) );

        Response response = ResponseHelper.ok(foundUser.orElse(null));
        return ResponseEntity.ok().body(response);
    }


    @GetMapping("/{userId}/roles")
    @PreAuthorize("hasAuthority('ADMIN') || @methodAuthorizeService.isAuthorizedUser( #userId, authentication )")
    @ApiOperation(
            value = "Get Assigned Roles By userId",
            notes = "This endpoint returns role information of the user has given userId. You must provide an auth token. " +
                    "! Admin User can access to all userId. !"
    )
    @ApiResponses({
            @ApiResponse(code = 401, message = "Unauthorized! You must provide a valid jwt token!", response = Response.class ),
            @ApiResponse(code = 403, message = "Forbidden! You can not access this area with your token!", response = Response.class ),
            @ApiResponse(code = 400, message = "Bad Request! Some invalid entries were provided!", response = Response.class ),
            @ApiResponse(code = 404, message = "Not Found! There is no data matches information you gave!", response = Response.class )
    })
    public ResponseEntity<Response<Set<String>>> getRolesByUserId(@PathVariable UUID userId) {
        Optional<UserDTO> foundUser = userService.findById(userId);

        Set<String> roles = new HashSet<>();
        if( foundUser.isPresent() && foundUser.get().getRoles() != null ) {
            roles = foundUser.get().getRoles().stream()
                    .map(s -> s.getRole().toString())
                    .collect(Collectors.toSet());

        }

        Response response = ResponseHelper.ok(roles);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/signup")
    @ApiOperation(
            value = "Create New User",
            notes = "This endpoint returns role information of the user has given userId. You must provide an auth token."
    )
    @ApiResponses({
            @ApiResponse(code = 400, message = "Bad Request! Some invalid entries were provided!", response = Response.class )
    })
    public ResponseEntity<Response<UserDTO>> signup(@RequestBody UserDTO user) {
        Optional<UserDTO> createdUser = userService.create(Optional.ofNullable(user));

        Response response = ResponseHelper.ok(createdUser.orElse(null));
        return ResponseEntity.ok().body(response);
    }
}
