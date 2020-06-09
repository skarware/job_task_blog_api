package org.martynas.blog_api.service;

import org.martynas.blog_api.model.Authority;
import org.martynas.blog_api.model.BlogUser;
import org.martynas.blog_api.repository.AuthorityRepository;
import org.martynas.blog_api.repository.BlogUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Service
public class BlogUserServiceImpl implements BlogUserService {

    private static final String DEFAULT_ROLE = "ROLE_USER";
    private final BCryptPasswordEncoder bcryptEncoder;
    private final BlogUserRepository blogUserRepository;
    private final AuthorityRepository authorityRepository;

    @Autowired
    public BlogUserServiceImpl(BCryptPasswordEncoder bcryptEncoder, BlogUserRepository blogUserRepository, AuthorityRepository authorityRepository) {
        this.bcryptEncoder = bcryptEncoder;
        this.blogUserRepository = blogUserRepository;
        this.authorityRepository = authorityRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<BlogUser> blogUser = blogUserRepository.findByUsername(username);
        return blogUser.orElseThrow(() -> new UsernameNotFoundException("No user found with username " + username));
    }

    @Override
    public Optional<BlogUser> findByUsername(String username) {
        return blogUserRepository.findByUsername(username);
    }

    @Override
    public BlogUser saveNewBlogUser(BlogUser blogUser) throws RoleNotFoundException {

        // Encode plaintext password with password encoder
        blogUser.setPassword(this.bcryptEncoder.encode(blogUser.getPassword())); // explicit bcryptEncoder is safer approach ?

        // set new blog user account to enabled by default
        blogUser.setEnabled(true);

        // Set default Authority/Role to new blog user
        Optional<Authority> optionalAuthority = this.authorityRepository.findByAuthority(DEFAULT_ROLE);
        if (optionalAuthority.isPresent()) {
            Authority authority = optionalAuthority.get();
            Collection<Authority> authorities = Collections.singletonList(authority);
            blogUser.setAuthorities(authorities);
            return this.blogUserRepository.saveAndFlush(blogUser);
        } else {
            throw new RoleNotFoundException("Default role not found for blog user with username " + blogUser.getUsername());
        }
    }
}
