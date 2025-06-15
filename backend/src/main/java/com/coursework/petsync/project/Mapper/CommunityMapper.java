package com.coursework.petsync.project.Mapper;

import com.coursework.petsync.project.DTO.Response.CommentResponse;
import com.coursework.petsync.project.DTO.Response.PostResponse;
import com.coursework.petsync.project.DTO.entity.Comment;
import com.coursework.petsync.project.DTO.entity.CommunityProfile;
import com.coursework.petsync.project.DTO.entity.Post;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Mapper
public interface CommunityMapper {
    // 帖子相关
    List<PostResponse> getPostRecommendations();
    int publishPost(Post post);
    int updatePost(Post post);
    int deletePost(@Param("userId") Long userId, @Param("postId") Long postId);
    PostResponse getPostById(Long postId);
    List<PostResponse> filterPosts(@Param("keywords") String keywords, @Param("tag") String tag);
    List<PostResponse> getFollowingPosts(@Param("userId") Long userId, @Param("isExpert") boolean isExpert, @Param("sort") String sort);
    
    // 用户关系
    int isFollow(@Param("following") Long following, @Param("followed") Long followed);
    int isLike(@Param("userId") Long userId, @Param("postId") Long postId);
    int isCoin(@Param("userId") Long userId, @Param("postId") Long postId);
    
    // 评论相关
    List<CommentResponse> getPostDetails(Long postId);
    int comment(Comment comment);
    int updateComment(Comment comment);
    int deleteComment(@Param("userId") Long userId, @Param("commentId") Long commentId);
    
    // 浏览记录
    int havePostView(@Param("userId") Long userId, @Param("postId") Long postId);
    int insertPostViewRecord(@Param("userId") Long userId, @Param("postId") Long postId);
    int updatePostViewTime(@Param("userId") Long userId, @Param("postId") Long postId);
    
    // 互动操作
    int likePost(@Param("userId") Long userId, @Param("postId") Long postId);
    int unlikePost(@Param("userId") Long userId, @Param("postId") Long postId);
    int insertCoin(@Param("userId") Long userId, @Param("postId") Long postId);
    
    // 关注操作
    int followUser(@Param("userId") Long userId, @Param("follow") Long follow);
    int unfollowUser(@Param("userId") Long userId, @Param("follow") Long follow);
    
    // 用户资料
    CommunityProfile getCommunityProfile(Long userId);
    List<PostResponse> getUserPosts(Long userId);
    List<CommunityProfile> getFollowingUsers(Long userId);
    List<CommunityProfile> getFollowedUsers(Long userId);
    List<PostResponse> getUserViewRecords(Long userId);
    List<PostResponse> getUserLikes(Long userId);
    List<PostResponse> getUserInserts(Long userId);
}