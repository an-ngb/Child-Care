package com.example.demo.services.impl;

import com.example.demo.dtos.*;
import com.example.demo.entities.*;
import com.example.demo.repositories.*;
import com.example.demo.services.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private static final String UPLOADED_FOLDER = System.getProperty("user.dir") + "/uploads";
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final DoctorProfileRepository doctorProfileRepository;
    private final RoleRepository roleRepository;
    private final GroupTagRepository groupTagRepository;
    private final GroupPostRepository groupPostRepository;
    private final GroupPostTagRepository groupPostTagRepository;
    private final SessionServiceImpl sessionService;
    private final FileRepository fileRepository;
    private final PostFileRepository postFileRepository;

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

    private void saveUploadedFile(Post post, MultipartFile file) throws IOException {
//        if (!file.isEmpty()) {
//            byte[] bytes = file.getBytes();
//            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
//            Files.write(path, bytes);
//        }
        Path fileNameAndPath = Paths.get(UPLOADED_FOLDER, file.getOriginalFilename());
        Files.write(fileNameAndPath, file.getBytes());
        File file1 = new File(fileNameAndPath.toString());
        fileRepository.save(file1);
        PostFile postFile = new PostFile(post, file1);
        postFileRepository.save(postFile);
    }

    @Override
    public AbstractResponse post(PostDto postDto) throws IOException {

        GroupPost groupPost = new GroupPost(postDto.getTitle());
        groupPostRepository.save(groupPost);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName());
        Post post = new Post();
        post.setGroupPost(groupPost);
        post.setContent(postDto.getContent());
        post.setUserId(user.getId());
        postRepository.save(post);
        saveUploadedFile(post, postDto.getImage());
//        if(postDto.getTagList() != null){
//            for (String s : postDto.getTagList()) {
//                GroupTag tag = groupTagRepository.findGroupTagByName(s);
//                if (tag != null){
//                    GroupPostTag groupPostTag = new GroupPostTag(groupPost, tag);
//                    groupPostTagRepository.save(groupPostTag);
//                }
//            }
//        }
        return new AbstractResponse(post);
    }

    @Override
    public AbstractResponse edit(EditDto editDto) {
        GroupPost groupPost = groupPostRepository.findById(editDto.getId()).orElse(null);
        if (groupPost == null) {
            return new AbstractResponse("FAILED", "POST_NOT_FOUND", 404);
        }

        if (editDto.getTitle() != null) {
            groupPost.setTitle(editDto.getTitle());
        }

        List<Post> postList = null;
        if (editDto.getContent() != null) {
            postList = postRepository.findByGroupPost(groupPost);
            postList.get(0).setContent(editDto.getContent());
        }

        groupPostRepository.save(groupPost);
        postRepository.saveAll(postList);
        return new AbstractResponse();
    }

    @Override
    public AbstractResponse comment(CommentDto commentDto) {
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
            postSearchResultDto.setContent(postRepository.findByGroupPostOrderById(groupPost).get(0).getContent());
            if(postFileRepository.findByPost(postRepository.findByGroupPostOrderById(groupPost).get(0)) != null){
                postSearchResultDto.setFile(postFileRepository.findByPost(postRepository.findByGroupPostOrderById(groupPost).get(0)).getPostFile().getResourceLink());
            } else {
                postSearchResultDto.setFile(null);
            }
            postSearchResultDto.setAuthor(userProfileRepository.findByUser(user).getFullName());
            List<Post> commentList = postRepository.findByGroupPost(groupPost);
            List<CommentResultDto> commentResultDtoList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(commentList)) {
                commentList.forEach(item -> {
                    CommentResultDto commentResultDto = new CommentResultDto();
                    commentResultDto.setId(item.getId());
                    commentResultDto.setContent(item.getContent());
                    commentResultDto.setCreatedAt(item.getCreatedAt());
                    if(postFileRepository.findByPost(item) != null){
                        commentResultDto.setFile(postFileRepository.findByPost(item).getPostFile().getResourceLink());
                    } else {
                        commentResultDto.setFile(null);
                    }
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

