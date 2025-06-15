package com.coursework.petsync.project.Controller;

import com.coursework.petsync.project.DTO.Response.PetsyncResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/community")
@Tag(name = "社区与事件簿模块")
public class CommunityController {
    @Autowired
    private CommunityService communityService;

    //查看最近10佳热门帖
    @GetMapping("/post/recommendations")
    public PetsyncResponse<?> getPostRecommendations(@RequestParam int userId) {
        List<PostResponse> response =  communityService.getPostRecommendations(userId);
        return PetsyncResponse.success(response);
    }

    //用户发布帖子
    @PostMapping("/post/publish")
    public PetsyncResponse<?> publishPost(@ModelAttribute PostDTO postDTO) {
        if (postDTO.getTitle() != null && postDTO.getContent() != null) {
            int response = communityService.publishPost(postDTO);
            return PetsyncResponse.success(response);
        }
        else return PetsyncResponse.failure(0, "非法参数");

    }

    //用户修改自己的帖子内容
    @PostMapping("/post/alter")
    public PetsyncResponse<?> alterPost(@ModelAttribute PostDTO postDTO) {
        if (postDTO.getTitle() == null
                && postDTO.getContent() == null
                && postDTO.getImages() == null
                && postDTO.getTags() == null)
            return PetsyncResponse.failure(0, "非法参数");
        int response = communityService.alterPost(postDTO);
        return PetsyncResponse.success(response);
    }

    //用户删除自己的帖子
    @PostMapping("/post/delete")
    public PetsyncResponse<?> deletePost(@RequestParam int userId,
                                     @RequestParam int postId) {
        int response = communityService.deletePost(userId, postId);
        return PetsyncResponse.success(response);
    }

    //用户根据keywords和tags检索帖子
    @GetMapping("/post/search")
    public PetsyncResponse<?> searchPosts(@RequestParam int userId,
                                      @RequestParam(required = false) String keywords,
                                      @RequestParam(required = false) String tag) {
        if(keywords == null && tag == null) return PetsyncResponse.failure(0, "非法参数");
        List<PostResponse> response = communityService.searchPosts(userId, keywords, tag);
        return PetsyncResponse.success(response);
    }

    //获取关注用户帖子 is_expert(是否只查看咨询师帖子), sort(排序方式：newest/most_supported)
    @GetMapping("/post/following")
    public PetsyncResponse<?> getFollowingPosts(@RequestParam int userId,
                                            @RequestParam(required = false, defaultValue = "false") boolean isExpert,
                                            @RequestParam(required = false, defaultValue = "newest") String sort) {
        List<PostResponse> response = communityService.getFollowingPosts(userId, isExpert, sort);
        return PetsyncResponse.success(response);
    }

    //用户查看帖子详情
    @GetMapping("/post/{id}")
    public PetsyncResponse<?> viewPostDetails(@PathVariable int id,
                                          @RequestParam int userId) {
        PostResponse response = communityService.viewPostDetails(userId, id);
        return PetsyncResponse.success(response);
    }

    //用户点赞帖子
    @PostMapping("/post/{id}/like")
    public PetsyncResponse<?> likePost(@PathVariable int id,
                                   @RequestParam int userId) {
        int response = communityService.likePost(userId, id);
        return PetsyncResponse.success(response);
    }

    //用户取消点赞
    @PostMapping("/post/{id}/unlike")
    public PetsyncResponse<?> unlikePost(@PathVariable int id,
                                     @RequestParam int userId) {
        int response = communityService.unlikePost(userId, id);
        return PetsyncResponse.success(response);
    }

    //用户给帖子投币
    @PostMapping("/post/{id}/insert_coin")
    public PetsyncResponse<?> insertCoin(@PathVariable int id,
                                     @RequestParam int userId) {
        int response = communityService.insertCoin(userId, id);
        return PetsyncResponse.success(response);
    }

    //用户在帖子下发表评论
    @PostMapping("/comment/publish")
    public PetsyncResponse<?> comment(@RequestParam int userId,
                                  @RequestParam int postId,
                                  @RequestParam String content) {
        int response = communityService.comment(userId, postId, content);
        return PetsyncResponse.success(response);
    }

    //用户修改自己的评论
    @PostMapping("/comment/alter")
    public PetsyncResponse<?> alterComment(@RequestParam int userId,
                                       @RequestParam int commentId,
                                       @RequestParam String content) {
        int response = communityService.alterComment(userId, commentId, content);
        return PetsyncResponse.success(response);
    }

    //用户删除自己的评论
    @PostMapping("/comment/delete")
    public PetsyncResponse<?> deleteComment(@RequestParam int userId,
                                        @RequestParam int commentId) {
        int response = communityService.deleteComment(userId, commentId);
        return PetsyncResponse.success(response);
    }

    //在社区模块关注用户
    @PostMapping("/follow")
    public PetsyncResponse<?> followUser(@RequestParam int userId,
                                     @RequestParam int follow) {
        int response = communityService.followUser(userId, follow);
        return PetsyncResponse.success(response);
    }

    //在社区模块取消关注用户
    @PostMapping("/unfollow")
    public PetsyncResponse<?> unfollowUser(@RequestParam int userId,
                                       @RequestParam int follow) {
        int response = communityService.unfollowUser(userId, follow);
        return PetsyncResponse.success(response);
    }

    //获取指定用户社区主页
    @GetMapping("/profile/{id}")
    public PetsyncResponse<?> getCommunityProfile(@PathVariable int id,
                                              @RequestParam int userId) {
        CommunityProfile response = communityService.getCommunityProfile(userId, id);
        return PetsyncResponse.success(response);
    }

    //获取指定用户的帖子
    @GetMapping("/profile/{id}/posts")
    public PetsyncResponse<?> getUserPosts(@PathVariable int id,
                                       @RequestParam int userId) {
        List<PostResponse> response = communityService.getUserPosts(userId, id);
        return PetsyncResponse.success(response);
    }

    //获取指定用户社区关注列表
    @GetMapping("/profile/{id}/following")
    public PetsyncResponse<?> getFollowingUsers(@PathVariable int id,
                                            @RequestParam int userId) {
        List<CommunityProfile> response = communityService.getFollowingUsers(userId, id);
        return PetsyncResponse.success(response);
    }

    //获取指定用户社区粉丝列表
    @GetMapping("/profile/{id}/followed")
    public PetsyncResponse<?> getFollowedUsers(@PathVariable int id,
                                           @RequestParam int userId) {
        List<CommunityProfile> response = communityService.getFollowedUsers(userId, id);
        return PetsyncResponse.success(response);
    }

    //获取指定用户帖子浏览记录
    @GetMapping("/profile/{id}/views")
    public PetsyncResponse<?> getUserViewRecords(@PathVariable int id,
                                             @RequestParam int userId) {
        List<PostResponse> response = communityService.getUserViewRecords(userId, id);
        return PetsyncResponse.success(response);
    }

    //获取指定用户点赞帖子列表
    @GetMapping("/profile/{id}/likes")
    public PetsyncResponse<?> getUserLikes(@PathVariable int id,
                                       @RequestParam int userId) {
        List<PostResponse> response = communityService.getUserLikes(userId, id);
        return PetsyncResponse.success(response);
    }

    //获取指定用户投币帖子列表
    @GetMapping("/profile/{id}/insert_coins")
    public PetsyncResponse<?> getUserInserts(@PathVariable int id,
                                         @RequestParam int userId) {
        List<PostResponse> response = communityService.getUserInserts(userId, id);
        return PetsyncResponse.success(response);
    }
}