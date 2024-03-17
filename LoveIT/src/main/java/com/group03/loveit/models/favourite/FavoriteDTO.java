package com.group03.loveit.models.favourite;

import com.group03.loveit.models.post.PostDTO;
import com.group03.loveit.models.user.UserDTO;

import java.time.LocalDateTime;

/**
 * @author Nhat
 */
public class FavoriteDTO {
    // Fields
    private PostDTO post;
    private UserDTO user;
    private LocalDateTime createdAt;

    public PostDTO getPost() {
        return post;
    }

    public void setPost(PostDTO post) {
        this.post = post;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public FavoriteDTO(PostDTO post, UserDTO user, LocalDateTime createdAt) {
        this.post = post;
        this.user = user;
        this.createdAt = createdAt;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((post == null) ? 0 : post.hashCode());
        result = prime * result + ((user == null) ? 0 : user.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FavoriteDTO other = (FavoriteDTO) obj;
        if (post == null) {
            if (other.post != null) {
                return false;
            }
        } else if (!post.equals(other.post)) {
            return false;
        }
        if (user == null) {
            if (other.user != null) {
                return false;
            }
        } else if (!user.equals(other.user)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "FavouriteDTO{" + "post=" + post + ", user=" + user + '}';
    }
}