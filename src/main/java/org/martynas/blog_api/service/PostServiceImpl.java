package org.martynas.blog_api.service;

import org.martynas.blog_api.model.BlogUser;
import org.martynas.blog_api.model.Post;
import org.martynas.blog_api.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public Optional<Post> getById(Long id) {
        return postRepository.findById(id);
    }

    @Override
    public Collection<Post> getAll() {
        return postRepository.findAllByOrderByCreationDateDesc();
    }

    @Override
    public Collection<Post> getAllByUser(BlogUser blogUser) {
        return postRepository.findAllByUserOrderByCreationDate(blogUser);
    }

    @Override
    public Post save(Post post) {
        return postRepository.saveAndFlush(post);
    }

    @Override
    public void delete(Post post) {
        postRepository.delete(post);
    }
}
