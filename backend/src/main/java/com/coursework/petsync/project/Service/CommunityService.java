package com.coursework.petsync.project.Service;

import com.coursework.petsync.project.DTO.Response.PostResponse;
import com.coursework.petsync.project.Mapper.CommunityMapper;
import com.coursework.petsync.project.Mapper.UserMapper;
import com.example.wantsound2.mapper.CommunityMapper;
import com.example.wantsound2.mapper.UserMapper;
import com.example.wantsound2.model.entity.User;
import com.example.wantsound2.model.entity.community.Comment;
import com.example.wantsound2.model.entity.community.Post;
import com.example.wantsound2.model.entity.enums.UserRole;
import com.example.wantsound2.model.request.PostDTO;
import com.example.wantsound2.model.response.CommentResponse;
import com.example.wantsound2.model.response.CommunityProfile;
import com.example.wantsound2.model.response.PostResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;

@Service
public class CommunityService {
    @Autowired
    private CommunityMapper communityMapper;
    @Autowired
    private UserMapper userMapper;

    @Value("${post_image.upload_dir}")
    private String uploadDir;

    @Value("${post_image.access-url-prefix}")
    private String accessUrlPrefix;
    private boolean fillPostRelationInfo(int userId, PostResponse postResponse) {
        int postId = postResponse.getPostId();
        postResponse.setFollow(communityMapper.isFollow(userId, postResponse.getUserId()) == 1);
        postResponse.setLike(communityMapper.isLike(userId, postId) == 1);
        postResponse.setCoin(communityMapper.isCoin(userId, postId) == 1);
        return true;
    }

    private boolean fillPostUserInfo(PostResponse postResponse) {
        postResponse.fillUserInfo(userMapper.findByID(postResponse.getUserId()));
        return true;
    }

    private boolean fillCommentUserInfo(CommentResponse commentResponse) {
        User user = userMapper.findByID(commentResponse.getUserId());
        commentResponse.setUserAvatar(user.getAvatar());
        commentResponse.setUserName(user.getName());
        return true;
    }
    private boolean fillPostComments(PostResponse postResponse) {
        postResponse.setComments(communityMapper.getPostDetails(postResponse.getPostId()));
        for(CommentResponse commentResponse : postResponse.getComments()) {
            fillCommentUserInfo(commentResponse);
        }
        return true;
    }

    private boolean fillFollowRelation(int userId, CommunityProfile communityProfile) {
        if(userId == communityProfile.getUserId()) return false;

        communityProfile.setFollow(communityMapper.isFollow(userId, communityProfile.getUserId()) == 1);
        return true;
    }

    private boolean fillProfileUserInfo(CommunityProfile communityProfile) {
        User user = userMapper.findByID(communityProfile.getUserId());
        communityProfile.setUserName(user.getName());
        communityProfile.setUserAvatar(user.getAvatar());
        communityProfile.setConsultant(Objects.equals(user.getRole(), UserRole.CONSULTANT.name()));

        return true;
    }

    private String storePostImages(List<MultipartFile> images) throws IOException {
        if(images != null && !images.isEmpty()) {
            Path uploadPath = Paths.get(uploadDir);
            if(!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            StringBuilder ret = new StringBuilder();
            for(MultipartFile image : images) {
                String nameStored = System.currentTimeMillis() + '_' + image.getOriginalFilename();
                Path storePath = uploadPath.resolve(nameStored);

                Files.copy(image.getInputStream(), storePath, StandardCopyOption.REPLACE_EXISTING);
                ret.append(accessUrlPrefix.replace("**", "")).append(nameStored);
                ret.append('|');
            }
            ret.deleteCharAt(ret.length() - 1);
            return ret.toString();
        }
        else return null;
    }

    public List<PostResponse> getPostRecommendations(int userId) {
        List<PostResponse> postResponses = communityMapper.getPostRecommendations();
        if(postResponses != null) {
            for(PostResponse postResponse : postResponses) {
                fillPostUserInfo(postResponse);
                fillPostRelationInfo(userId, postResponse);
            }
            return postResponses;
        }
        else return null;
    }

    public int publishPost(PostDTO postDTO) {
        String images = null;
        try {
            images = storePostImages(postDTO.getImages());
        } catch (Exception e) {
            System.out.println("images store fail");
            return -1;
        }

        Post post = new Post(postDTO.getUserId(), 0,
                postDTO.getTitle(), postDTO.getContent(),
                images, postDTO.getTags());
        if(communityMapper.publishPost(post) > 0)
            return post.getPostId();
        else
            return -1;
    }

    public int alterPost(PostDTO postDTO) {
        String images = null;
        try {
            images = storePostImages(postDTO.getImages());
        } catch (Exception e) {
            System.out.println("images store fail");
            return -1;
        }

        Post post = new Post(postDTO.getUserId(), postDTO.getPostId(),
                postDTO.getTitle(), postDTO.getContent(),
                images, postDTO.getTags());
        if(communityMapper.updatePost(post) > 0)
            return 1;
        else
            return -1;
    }

    public int deletePost(int userId, int postId) {
        if(communityMapper.deletePost(userId, postId) == 0)
            return -1;
        return 1;
    }

    public List<PostResponse> searchPosts(int userId, String keywords, String tag) {
        List<PostResponse> postResponses = communityMapper.filterPosts(keywords, tag);

        if(postResponses != null) {
            for(PostResponse postResponse : postResponses) {
                fillPostUserInfo(postResponse);
                fillPostRelationInfo(userId, postResponse);
            }
            return postResponses;
        }
        else return null;
    }

    public List<PostResponse> getFollowingPosts(int userId, boolean isExpert, String sort){
        List<PostResponse> postResponses = communityMapper.getFollowingPosts(userId, isExpert, sort);

        for(PostResponse postResponse : postResponses) {
            fillPostUserInfo(postResponse);
            fillPostRelationInfo(userId, postResponse);
        }
        return postResponses;
    }

    public PostResponse viewPostDetails(int userId, int postId) {
        PostResponse postResponse = communityMapper.getPostById(postId);
        if(postResponse != null) {
            fillPostUserInfo(postResponse);
            fillPostRelationInfo(userId, postResponse);
            fillPostComments(postResponse);
            if(communityMapper.havePostView(userId, postId) == 0)
                communityMapper.insertPostViewRecord(userId, postId);
            else
                communityMapper.updatePostViewTime(userId, postId);

            return postResponse;
        }
        else return null;
    }

    public int likePost(int userId, int postId) {
        if(communityMapper.likePost(userId, postId) == 0)
            return -1;
        return 1;
    }

    public int unlikePost(int userId, int postId) {
        if(communityMapper.unlikePost(userId, postId) == 0)
            return -1;
        return 1;
    }

    public int insertCoin(int userId, int postId) {
        if(communityMapper.insertCoin(userId, postId) == 0)
            return -1;
        return 1;
    }

    public int comment(int userId, int postId, String content) {
        Comment comment = new Comment(0, postId, userId, content);
        if(communityMapper.comment(comment) == 0)
            return -1;
        return 1;
    }

    public int alterComment(int userId, int commentId, String content) {
        Comment comment = new Comment(commentId, 0, userId, content);
        if(communityMapper.updateComment(comment) == 0)
            return -1;
        return 1;
    }

    public int deleteComment(int userId, int commentId) {
        if(communityMapper.deleteComment(userId, commentId) == 0)
            return -1;
        return 1;
    }

    public int followUser(int userId, int follow) {
        if (communityMapper.followUser(userId, follow) == 0)
            return -1;
        return 1;
    }

    public int unfollowUser(int userId, int follow) {
        if (communityMapper.unfollowUser(userId, follow) == 0)
            return -1;
        return 1;
    }

    public CommunityProfile getCommunityProfile(int userId, int targetId) {
        CommunityProfile profile = communityMapper.getCommunityProfile(targetId);
        if (profile == null)
            return null;
        fillProfileUserInfo(profile);
        fillFollowRelation(userId, profile);
        if(targetId != userId)
            profile.setHaveCoins(-1);
        return profile;
    }

    public List<PostResponse> getUserPosts(int userId, int targetId) {
        List<PostResponse> postResponses = communityMapper.getUserPosts(targetId);
        if(userId != targetId)
            for(PostResponse postResponse : postResponses) {
                fillPostUserInfo(postResponse);
                fillPostRelationInfo(userId, postResponse);
            }

        return postResponses;
    }

    public List<CommunityProfile> getFollowingUsers(int userId, int targetId) {
        List<CommunityProfile> profiles = communityMapper.getFollowingUsers(targetId);
        for(CommunityProfile profile : profiles) {
            fillProfileUserInfo(profile);
            fillFollowRelation(userId, profile);
            if(profile.getUserId() != userId)
                profile.setHaveCoins(-1);
        }
        return profiles;
    }

    public List<CommunityProfile> getFollowedUsers(int userId, int targetId) {
        List<CommunityProfile> profiles = communityMapper.getFollowedUsers(targetId);
        for(CommunityProfile profile : profiles) {
            fillProfileUserInfo(profile);
            fillFollowRelation(userId, profile);
            if(profile.getUserId() != userId)
                profile.setHaveCoins(-1);
        }
        return profiles;
    }

    public List<PostResponse> getUserViewRecords(int userId, int targetId) {
        List<PostResponse> postResponses = communityMapper.getUserViewRecords(targetId);
        for(PostResponse postResponse : postResponses) {
            fillPostUserInfo(postResponse);
            if(userId != postResponse.getUserId())
                fillPostRelationInfo(userId, postResponse);
        }
        return postResponses;
    }

    public List<PostResponse> getUserLikes(int userId, int targetId) {
        List<PostResponse> postResponses = communityMapper.getUserLikes(targetId);
        for(PostResponse postResponse : postResponses) {
            fillPostUserInfo(postResponse);
            fillPostRelationInfo(userId, postResponse);
        }
        return postResponses;
    }

    public List<PostResponse> getUserInserts(int userId, int targetId) {
        List<PostResponse> postResponses = communityMapper.getUserInserts(targetId);
        for(PostResponse postResponse : postResponses) {
            fillPostUserInfo(postResponse);
            fillPostRelationInfo(userId, postResponse);
        }
        return postResponses;
    }
}
