package com.example.demo.services.impl;

import com.example.demo.dtos.*;
import com.example.demo.entities.*;
import com.example.demo.repositories.*;
import com.example.demo.services.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final DoctorProfileRepository doctorProfileRepository;
    private final RoleRepository roleRepository;
    private final GroupTagRepository groupTagRepository;
    private final GroupPostRepository groupPostRepository;
    private final GroupPostTagRepository groupPostTagRepository;
    private final SessionServiceImpl sessionService;

    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();
        Set<String> emptyNames = new HashSet<String>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    @Override
    public AbstractResponse post(PostDto postDto) {

        if(sessionService.isTokenExpire()){
            return new AbstractResponse("FAILED", "TOKEN_EXPIRED", 400);
        }

        GroupPost groupPost = new GroupPost(postDto.getTitle());
        groupPostRepository.save(groupPost);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName());
        Post post = new Post();
        post.setGroupPost(groupPost);
        post.setContent(postDto.getContent());
        post.setUserId(user.getId());
        if(postDto.getTagList() != null){
            for (String s : postDto.getTagList()) {
                GroupTag tag = groupTagRepository.findGroupTagByName(s);
                if (tag != null){
                    GroupPostTag groupPostTag = new GroupPostTag(groupPost, tag);
                    groupPostTagRepository.save(groupPostTag);
                }
            }
        }
        postRepository.save(post);
        return new AbstractResponse(post);
    }

    @Override
    public AbstractResponse edit(EditDto editDto) {
        if(sessionService.isTokenExpire()){
            return new AbstractResponse("FAILED", "TOKEN_EXPIRED", 400);
        }
        Post post = postRepository.findById(editDto.getId()).orElse(null);
        if (post == null) {
            return new AbstractResponse("FAILED", "POST_NOT_FOUND", 404);
        }
        BeanUtils.copyProperties(editDto, post, getNullPropertyNames(editDto));
        postRepository.save(post);
        return new AbstractResponse(post);
    }

    @Override
    public AbstractResponse comment(CommentDto commentDto) {
        if(sessionService.isTokenExpire()){
            return new AbstractResponse("FAILED", "TOKEN_EXPIRED", 400);
        }
        GroupPost groupPost = groupPostRepository.findGroupPostById(commentDto.getId());

        if (groupPost == null) {
            return new AbstractResponse("FAILED", "POST_NOT_FOUND", 404);
        }
        //Comment
        Post comment = new Post(groupPost);
        comment.setContent(commentDto.getContent());
        postRepository.save(comment);
        return new AbstractResponse();
    }
//
//    @Override
//    public AbstractResponse viewAllPost() {
//        if(sessionService.isTokenExpire()){
//            return new AbstractResponse("FAILED", "TOKEN_EXPIRED", 400);
//        }
//        List<Post> list = postRepository.findAll();
//        return new AbstractResponse(list);
//    }
//
//    @Override
//    public AbstractResponse viewAllPostAsc() {
//        if(sessionService.isTokenExpire()){
//            return new AbstractResponse("FAILED", "TOKEN_EXPIRED", 400);
//        }
//        List<Post> list = postRepository.findAllByOrderByIdAsc();
//        return new AbstractResponse(list);
//    }
//
//    @Override
//    public AbstractResponse viewAllPostDesc() {
//        if(sessionService.isTokenExpire()){
//            return new AbstractResponse("FAILED", "TOKEN_EXPIRED", 400);
//        }
//        List<Post> list = postRepository.findAllByOrderByIdDesc();
//        return new AbstractResponse(list);
//    }
//
//    @Override
//    public AbstractResponse viewMostLikedPost(FilterRequest filterRequest) {
//        if(sessionService.isTokenExpire()){
//            return new AbstractResponse("FAILED", "TOKEN_EXPIRED", 400);
//        }
//        List<Post> postList = postRepository.findAllByRoleIdOrderByTotalLikeAsc(filterRequest.getRoleId());
//        List<PostSearchResultDto> postSearchResultDtoList = convertPostToPostDto(postList);
//        return new AbstractResponse(postSearchResultDtoList);
//    }
//
////    @Override
////    public AbstractResponse viewPostByTag(FilterRequest filterRequest) {
////        if(sessionService.isTokenExpire()){
////            return new AbstractResponse("FAILED", "TOKEN_EXPIRED", 400);
////        }
////        List<Post> postList = postRepository.findAll(PostSpecs.search(filterRequest));
////        List<PostSearchResultDto> postSearchResultDtoList = convertPostToPostDto(postList);
////        return new AbstractResponse(postSearchResultDtoList);
////    }
//
    public List<PostSearchResultDto> convertPostToPostDto(List<GroupPost> groupPostList) {
        List<PostSearchResultDto> data = new ArrayList<>();
        for (GroupPost groupPost : groupPostList) {
            PostSearchResultDto postSearchResultDto = new PostSearchResultDto();
            User user = userRepository.findByEmail(groupPost.getCreatedBy());
            postSearchResultDto.setId(groupPost.getId());
            postSearchResultDto.setTitle(groupPost.getTitle());
            postSearchResultDto.setContent(postRepository.findByGroupPost(groupPost).get(0).getContent());
            postSearchResultDto.setAuthor(userProfileRepository.findByUser(user).getFullName());
            List<Post> commentList = postRepository.findByGroupPost(groupPost);
            List<CommentResultDto> commentResultDtoList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(commentList)) {
                commentList.forEach(item -> {
                    CommentResultDto commentResultDto = new CommentResultDto();
                    commentResultDto.setId(item.getId());
                    commentResultDto.setContent(item.getContent());
                    commentResultDto.setCreatedAt(item.getCreatedAt());

                    User foundUser = userRepository.findByEmail(item.getCreatedBy());

                    UserProfile userProfile = userProfileRepository.findByUser(foundUser);

                    if(userProfile != null){
                        commentResultDto.setCreatedBy(userProfile.getFullName());
                    } else {
                        DoctorProfile doctorProfile = doctorProfileRepository.findByUser(foundUser);
                        commentResultDto.setCreatedBy(doctorProfile.getFullName());
                    }

                    commentResultDto.setUpdatedAt(item.getUpdatedAt());
                    commentResultDtoList.add(commentResultDto);
                });
            }
            postSearchResultDto.setCommentList(commentResultDtoList);
            data.add(postSearchResultDto);
        }
        return data;
    }
//
//    @Override
//    public AbstractResponse like(InteractDto interactDto) {
//        if(sessionService.isTokenExpire()){
//            return new AbstractResponse("FAILED", "TOKEN_EXPIRED", 400);
//        }
//        Post post = postRepository.findPostById(interactDto.getId());
//        if (post == null) {
//            return new AbstractResponse("FAILED", "POST_NOT_FOUND", 404);
//        }
//
//        //Prev likes of post.
//        Integer prevLikes = post.getTotalLike();
//        if (prevLikes == null) {
//            prevLikes = 0;
//        }
//        post.setTotalLike(prevLikes + 1);
//        postRepository.save(post);
//        return new AbstractResponse();
//    }
//
//    @Override
//    public AbstractResponse dislike(InteractDto interactDto) {
//        if(sessionService.isTokenExpire()){
//            return new AbstractResponse("FAILED", "TOKEN_EXPIRED", 400);
//        }
//        Post post = postRepository.findPostById(interactDto.getId());
//        if (post == null) {
//            return new AbstractResponse("FAILED", "POST_NOT_FOUND", 404);
//        }
//
//        //Prev dislikes of post.
//        Integer prevDislikes = post.getTotalDislike();
//        if (prevDislikes == null) {
//            prevDislikes = 0;
//        }
//        post.setTotalDislike(prevDislikes + 1);
//        postRepository.save(post);
//        return new AbstractResponse();
//    }
}

