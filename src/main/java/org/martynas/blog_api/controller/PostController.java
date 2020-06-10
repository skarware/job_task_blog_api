package org.martynas.blog_api.controller;

import org.martynas.blog_api.exception.AccessDeniedException;
import org.martynas.blog_api.exception.PostNotFoundException;
import org.martynas.blog_api.model.BlogUser;
import org.martynas.blog_api.model.Post;
import org.martynas.blog_api.service.BlogUserService;
import org.martynas.blog_api.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

@RestController
public class PostController {

    private final PostService postService;
    private final BlogUserService blogUserService;

    @Autowired
    public PostController(PostService postService, BlogUserService blogUserService) {
        this.postService = postService;
        this.blogUserService = blogUserService;
    }

    // Get all blog posts by all user
    @GetMapping("/posts")
    Collection<Post> getAllPosts() {
        return this.postService.getAll();
    }

    // Get all current user blog posts
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/posts/user")
    Collection<Post> getAllOwnPosts(Principal principal) {
        return this.postService.getAllByUser((BlogUser) blogUserService.loadUserByUsername(principal.getName()));
    }

    // Get all blog posts by given user email
    @GetMapping("/posts/user/{email}")
    Collection<Post> getAllPostsByEmail(@PathVariable String email) {
        return this.postService.getAllByUser((BlogUser) blogUserService.loadUserByUsername(email));
    }

    // Get a post by given id
    @GetMapping("/posts/{id}")
    Post getPost(@PathVariable Long id) {
        // Find and return the post by given id or throw exception
        return this.postService.getById(id).orElseThrow(() -> new PostNotFoundException(id));
    }

    // Create new blog post and returns it
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/posts")
    public String createNewPost(@Valid Post post, BindingResult bindingResult, Principal principal) {

        // Just in case authorization fails set user name to anonymous to avoid null exception
        String authUsername = "anonymous";
        if (principal != null) {
            authUsername = principal.getName();
        }
        // Set valid BlogUser from DB into new Post (please note, anonymous is a valid user its user in DB with id: 0)
        post.setUser((BlogUser) this.blogUserService.loadUserByUsername(authUsername));

        // Validate post fields
        if (bindingResult.hasErrors()) {
            String validationErrorMsg = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
            return "Validation Error: " + validationErrorMsg;
        }

        // if no errors then Persist new blog post into database and return it
        return this.postService.save(post).toString();
    }

    // Replaces a post by given id (only if one exists and is modified by same user post was created)
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/posts/{id}")
    public String replacePost(@PathVariable Long id, @Valid Post replacingPost, BindingResult bindingResult, Principal principal) {

        // Try to find blog post by given id
        Optional<Post> optionalPost = this.postService.getById(id);

        // If post by id exist then modify it and return or throw post not found exception
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            // If current authenticated user is owner of the post then modify or throw 403
            if (principal.getName().equals(post.getUser().getUsername())) {

                // Validate replacing post's fields
                if (bindingResult.hasErrors()) {
                    String validationErrorMsg = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
                    return "Validation Error: " + validationErrorMsg;
                }

                // Replace post's title and body
                post.setTitle(replacingPost.getTitle());
                post.setBody(replacingPost.getBody());

                // Persist edited blog post into database and return it
                return this.postService.save(post).toString();

            } else {
                // else access to the requested resource is forbidden (403)
                throw new AccessDeniedException();
            }
        } else {
            throw new PostNotFoundException(id);
        }
    }

    // Delete a post by given id (only if one exists and is deleted by same user post was created)
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/posts/{id}")
    public void deletePost(@PathVariable Long id, Principal principal) {

        // Try to find blog post by given id
        Optional<Post> optionalPost = this.postService.getById(id);

        // If post by id exist then delete it or throw post not found exception
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            // If current authenticated user is owner of the post then delete or throw 403
            if (principal.getName().equals(post.getUser().getUsername())) {

                // Delete blog post into database and return it
                this.postService.delete(post);

            } else {
                // else access to the requested resource is forbidden (403)
                throw new AccessDeniedException();
            }
        } else {
            throw new PostNotFoundException(id);
        }
    }

}
