package com.group03.loveit.models.post;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author Nhat
 */
public interface IPostDAO {

    CompletableFuture<PostDTO> getPostById(long id);
    CompletableFuture<List<PostDTO>> getAllPosts();
    CompletableFuture<List<PostDTO>> getFilteredPosts(Integer pageSize, Integer page, PostFilter filter, List<Long> fetchedPostIds);
    CompletableFuture<Void> insertPost(PostDTO post);
    CompletableFuture<Void> updatePost(PostDTO post);
    CompletableFuture<Void> flagPost(long id, boolean isActive);
    CompletableFuture<Void> deletePost(long id);
}
