package com.project.rest.webservices.restfulwebservices.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

@RestController
public class UserResource {
    @Autowired
    private UserDaoService service;

    @GetMapping("/users")
    public List<User> retrieveAllUsers() {
        return service.findAll();
    }

    @GetMapping("/users/{id}")
    public Resource<User> retrieveUser(@PathVariable int id) {
        User user = service.findOne(id);
        if(user==null)
            throw new UserNotFoundException("id-"+ id);

        Resource<User> resource = new Resource<User>(user);
        ControllerLinkBuilder linkTo =
                linkTo(methodOn(this.getClass()).retrieveAllUsers());

        resource.add(linkTo.withRel("all-users"));

        return resource;
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser (@PathVariable int id) {
        User user = service.deleteById(id);
        if(user==null)
            throw new UserNotFoundException("id-"+ id);
        return;
    }
    //
    // input - details of user
    // output - CREATED & Return the created URI
    @PostMapping("/users")
    public ResponseEntity createUser(@Valid @RequestBody User user) {
        User savedUser = service.save(user);
        //created
        // /users/ savedUser.getId
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedUser.getId()).toUri();

        return ResponseEntity.created(location).build();
    }
}
