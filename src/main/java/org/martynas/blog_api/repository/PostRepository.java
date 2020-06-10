package org.martynas.blog_api.repository;

import org.martynas.blog_api.model.BlogUser;
import org.martynas.blog_api.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Collection<Post> findAllByOrderByCreationDateDesc();

    Collection<Post> findAllByUserOrderByCreationDate(@NotNull BlogUser blogUser);

    Optional<Post> findById(Long id);
}
