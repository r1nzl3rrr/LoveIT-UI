package com.group03.loveit.models.favourite;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author Nhat
 */
public interface IFavoriteDAO {
    CompletableFuture<FavoriteDTO> getFavoriteById(long postId, long userId);
    CompletableFuture<List<FavoriteDTO>> getFavoritesByUser(long userId);
    CompletableFuture<List<FavoriteDTO>> getFavoritesByPost(long postId);
    CompletableFuture<Void> insertFavorite(FavoriteDTO favorite);
    CompletableFuture<Void> deleteFavorite(long postId, long userId);
}
