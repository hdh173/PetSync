package com.coursework.petsync.project.DTO.entity;

import lombok.Data;

/**
 * CREATE TABLE comment(
 * 	comment_id	INT		PRIMARY KEY		AUTO_INCREMENT,
 * 	post_id 	INT		NOT NULL,
 * 	user_id		INT		NOT NULL,
 * 	content		TEXT	NOT NULL,
 * 	created_at	TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
 * 	updated_at	TIMESTAMP	DEFAULT CURRENT_TIMESTAMP		ON UPDATE CURRENT_TIMESTAMP,
 * 	FOREIGN KEY	post_id	REFERENCES	post(post_id)
 * 		ON DELETE CASCADE,
 * 	FOREIGN KEY user_id	REFERENCES	user(id)
 * 		ON DELETE CASCADE
 * 	);
 */
@Data
public class Comment {
    private int commentId;
    private int postId;
    private int userId;
    private String content;
    private String createdAt;
    private String updatedAt;

    public Comment(int commentId, int postId, int userId, String content) {
        this.commentId = commentId;
        this.postId = postId;
        this.userId = userId;
        this.content = content;
    }
}
