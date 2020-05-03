package com.paydaybank.dashboard.web.api;

import com.paydaybank.dashboard.dto.model.UserDTO;
import com.paydaybank.dashboard.exception.PaydayException;
import com.paydaybank.dashboard.model.UserRole;
import com.paydaybank.dashboard.service.UserService;
import com.paydaybank.dashboard.web.helper.ResponseHelper;
import com.paydaybank.dashboard.web.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    UserService userService;

    @GetMapping("/{userId}/roles")
    public ResponseEntity getRolesByUserId(@PathVariable UUID userId) {
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
    public ResponseEntity signup(@RequestBody UserDTO user) {
        Optional<UserDTO> createdUser = userService.create(Optional.ofNullable(user));

        Response response = ResponseHelper.ok(createdUser.orElse(null));
        return ResponseEntity.ok().body(response);
    }
}
