package org.martynas.blog_api.controller;

import org.martynas.blog_api.model.BlogUser;
import org.martynas.blog_api.service.BlogUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.RoleNotFoundException;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Objects;

@RestController
public class UserController {

    private final BlogUserService blogUserService;

    @Autowired
    public UserController(BlogUserService blogUserService) {
        this.blogUserService = blogUserService;
    }

    @GetMapping("/login")
    public String login(Principal principal) {
        // Check if web services user already logged in if so show its name
        if (principal != null) {
            return "You logged in as: " + principal.getName();
        } else {
            return "Services use Basic Auth for users Authorization";
        }
    }

    @PostMapping("/register")
    public String registerNewUser(@Valid BlogUser blogUser, BindingResult bindingResult) throws RoleNotFoundException {

        // Check if username is available
        if (blogUserService.findByUsername(blogUser.getUsername()).isPresent()) {
            return "Username is already registered try other one or go away";
        }

        // Validate users fields
        if (bindingResult.hasErrors()) {
            String validationErrorMsg = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
            return "New user did not validate; " + validationErrorMsg;
        }

        // if pass validation, no any errors then Persist new blog user into database
        this.blogUserService.saveNewBlogUser(blogUser);

        return "new blog user successfully created";
    }
}
