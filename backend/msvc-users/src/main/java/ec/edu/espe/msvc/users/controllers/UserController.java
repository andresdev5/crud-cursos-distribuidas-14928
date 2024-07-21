package ec.edu.espe.msvc.users.controllers;

import ec.edu.espe.msvc.users.entity.User;
import ec.edu.espe.msvc.users.services.UserService;
import ec.edu.espe.msvc.users.dto.UserDto;
import org.springframework.http.ResponseEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAll(){
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id){
        Optional<User> userOptional = userService.getById(id);

        if(userOptional.isPresent()){
            return ResponseEntity.ok().body(userOptional.get());
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        Optional<User> optionalUser = userService.getById(id);

        if(optionalUser.isPresent()){
            userService.delete(id);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> save(@Validated(UserDto.Create.class) @RequestBody UserDto user){
        return ResponseEntity.ok().body(userService.save(user));
    }

    @PutMapping
    public ResponseEntity<?> update(@Validated(UserDto.Update.class) @RequestBody UserDto user){
        return ResponseEntity.ok().body(userService.update(user));
    }

    @PostMapping("/fetch-ids")
    public List<UserDto> fetchUsers(@RequestParam List<Long> ids){
        return userService.fetchUsersByIds(ids);
    }
}
