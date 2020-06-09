package org.martynas.blog_api.service;

import org.martynas.blog_api.model.BlogUser;
import org.martynas.blog_api.model.Post;

import java.util.Collection;
import java.util.Optional;

public interface PostService {

    Optional<Post> getById(Long id);

    Collection<Post> getAll();

    Collection<Post> findAllByUser(BlogUser blogUser);

    Collection<Post> findByUser(BlogUser blogUser);

    Post save(Post post);

    void delete(Post post);
}

