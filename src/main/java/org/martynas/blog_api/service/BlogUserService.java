package org.martynas.blog_api.service;

import org.martynas.blog_api.model.BlogUser;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.management.relation.RoleNotFoundException;
import java.util.Optional;

public interface BlogUserService extends UserDetailsService {

    Optional<BlogUser> findByEmail(String email);

    BlogUser saveNewBlogUser(BlogUser blogUser) throws RoleNotFoundException;

}
