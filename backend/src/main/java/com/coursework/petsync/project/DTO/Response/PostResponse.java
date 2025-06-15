package com.coursework.petsync.project.DTO.Response;

import com.coursework.petsync.project.DTO.entity.Post;
import com.coursework.petsync.project.DTO.entity.User;
import lombok.Data;

import java.util.List;

@Data
public class PostResponse extends Post {
    private String userName;
    private String userAvatar;
    private Boolean isConsultant;
    private Boolean follow;
    private Boolean like;
    private Boolean coin;
    private List<CommentResponse> comments;
    
    public void fillUserInfo(User user) {
        this.userName = user.getNickname();
        this.userAvatar = user.getAvatarUrl();
        this.isConsultant = user.getRole().equals("CONSULTANT");
    }
}