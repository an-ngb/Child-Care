package com.example.demo.services.impl;

import com.cloudinary.utils.StringUtils;
import com.example.demo.dtos.*;
import com.example.demo.entities.*;
import com.example.demo.repositories.*;
import com.example.demo.services.PostService;
import com.example.demo.specification.PostSpecs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.print.DocFlavor;
import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

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
    private final ParentGroupRepository parentGroupRepository;
    private final ReactionRepository reactionRepository;

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

        GroupPost groupPost = new GroupPost(postDto.getTitle(), postDto.getThreadId());
        groupPostRepository.save(groupPost);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName());
        Post post = new Post();
        post.setGroupPost(groupPost);
        post.setContent(postDto.getContent());
        post.setUserId(user.getId());
        post.setThumbnailImage(postDto.getThumbnailImage());
        postRepository.save(post);
//        if(postDto.getTagList() != null){
//            for (String s : postDto.getTagList()) {
//                GroupTag tag = groupTagRepository.findGroupTagByName(s);
//                if (tag != null){
//                    GroupPostTag groupPostTag = new GroupPostTag(groupPost, tag);
//                    groupPostTagRepository.save(groupPostTag);
//                }
//            }
//        }
        List<GroupPost> groupPostList = new ArrayList<>();
        groupPostList.add(groupPost);
        return new AbstractResponse(new ArrayList<>(convertPostToPostDto(groupPostList)));
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

        List<Post> postList = new ArrayList<>();
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
        GroupPost groupPost = groupPostRepository.findGroupPostById(commentDto.getId()).orElse(null);

        if (groupPost == null) {
            return new AbstractResponse("FAILED", "POST_NOT_FOUND", 404);
        }

        //Comment
        Post comment = new Post(groupPost);
        comment.setContent(commentDto.getContent());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName());

        comment.setUserId(user.getId());

        postRepository.save(comment);
        return new AbstractResponse();
    }

    @Override
    public AbstractResponse deletePost(DeleteDto deleteDto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        GroupPost groupPost = groupPostRepository.findGroupPostById(deleteDto.getId()).orElse(null);
        List<Post> postList = postRepository.findByGroupPost(groupPost);
        if(groupPost != null && postList != null){
            postRepository.deleteAll(postList);
            groupPostRepository.delete(groupPost);
        } else {
            return new AbstractResponse("FAILED", "POST_NOT_FOUND", 404);
        }
        return new AbstractResponse();
    }

    @Override
    public AbstractResponse search(SearchDto searchDto) {
        if(Strings.isEmpty(searchDto.getKey())){
            return new AbstractResponse();
        }
        return new AbstractResponse(postRepository.findAll(PostSpecs.search(searchDto)));
    }

    @Override
    public AbstractResponse getAllParentGroup() {
        return new AbstractResponse(parentGroupRepository.findAll());
    }

    @Override
    public AbstractResponse getPostInsideThread(GetPostByThreadDto getPostByThreadDto) {

        List<GroupPost> groupPostList = groupPostRepository.findAllByParentGroup(getPostByThreadDto.getThreadId());

        List<PostSearchResultDto> postSearchResultDtoList = convertPostToPostDto(groupPostList);

        return new AbstractResponse(postSearchResultDtoList);
    }

    @Override
    public AbstractResponse getPostByPostId(int id){
        GroupPost groupPost = groupPostRepository.findGroupPostById(id).orElse(null);
        List<PostSearchResultDto> postSearchResultDtoList;
        if (groupPost == null) {
            return new AbstractResponse("FAILED", "POST_NOT_FOUND", 404);
        } else {
            List<GroupPost> groupPostList = new ArrayList<>();
            groupPostList.add(groupPost);
            postSearchResultDtoList = convertPostToPostDto(groupPostList);
        }
        return new AbstractResponse(postSearchResultDtoList);
    }

    //
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
        String defaultImg = "http://res.cloudinary.com/annb/image/upload/v1667571608/a8phdesstsrxxuylgdgl.jpg";
        for (GroupPost groupPost : groupPostList) {
            PostSearchResultDto postSearchResultDto = new PostSearchResultDto();
            User user = userRepository.findByEmail(groupPost.getCreatedBy());
            Post post = postRepository.findByGroupPostOrderById(groupPost).get(0);
            postSearchResultDto.setId(groupPost.getId());
            if(StringUtils.isEmpty(groupPost.getTitle())){
                continue;
            }
            postSearchResultDto.setTitle(groupPost.getTitle());
            postSearchResultDto.setThumbnailImage(StringUtils.isEmpty(post.getThumbnailImage()) ? defaultImg : post.getThumbnailImage());
            Integer totalLike = (int) reactionRepository.findAllByPost(post).stream().filter(e -> e.getIsUpvote() != null).map(Reaction::getIsUpvote).count();
            postSearchResultDto.setTotalLike(totalLike);
            Integer totalDislike = (int) reactionRepository.findAllByPost(post).stream().filter(e -> e.getIsUpvote() != null).map(item -> !item.getIsUpvote()).count();
            postSearchResultDto.setTotalDislike(totalDislike);
            if(postRepository.findByGroupPostOrderById(groupPost).size() > 0){
                postSearchResultDto.setContent(StringUtils.isEmpty(post.getContent()) ? null : post.getContent());
            } else {
                postSearchResultDto.setContent(null);
            }
            postSearchResultDto.setAuthor(userProfileRepository.findByUser(user).getFullName());
            postSearchResultDto.setCreateAt(groupPost.getCreatedAt().toEpochMilli());
            postSearchResultDto.setUpdatedAt(groupPost.getUpdatedAt().toEpochMilli());
            List<Post> commentList = postRepository.findByGroupPost(groupPost);
            List<CommentResultDto> commentResultDtoList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(commentList)) {
                for (Post item : commentList) {
                    if((!StringUtils.isEmpty(postRepository.findByGroupPostOrderById(groupPost).get(0).getContent()) && postRepository.findByGroupPostOrderById(groupPost).get(0).getContent().equals(item.getContent())) || StringUtils.isEmpty(item.getContent())){
                        continue;
                    }
                    CommentResultDto commentResultDto = new CommentResultDto();
                    commentResultDto.setId(item.getId());
                    commentResultDto.setContent(item.getContent());
                    commentResultDto.setCreatedAt(item.getCreatedAt().toEpochMilli());
                    User foundUser = userRepository.findByEmail(item.getCreatedBy());

                    UserProfile userProfile = userProfileRepository.findByUser(foundUser);

                    if(userProfile != null){
                        commentResultDto.setCreatedBy(userProfile.getFullName());
                    } else {
                        DoctorProfile doctorProfile = doctorProfileRepository.findByUser(foundUser);
                        commentResultDto.setCreatedBy(doctorProfile.getFullName());
                    }

                    commentResultDto.setUpdatedAt(item.getUpdatedAt().toEpochMilli());
                    commentResultDtoList.add(commentResultDto);
                }
            }
            postSearchResultDto.setCommentList(commentResultDtoList);
            data.add(postSearchResultDto);
        }
        return data;
    }

    public List<PostSearchResultDto> convertPostToPostDtoV2(List<Post> postList) {
        List<PostSearchResultDto> data = new ArrayList<>();
        for (Post post : postList) {
            PostSearchResultDto postSearchResultDto = new PostSearchResultDto();
            User user = userRepository.findByEmail(post.getCreatedBy());
            postSearchResultDto.setId(post.getId());
            postSearchResultDto.setTitle(post.getGroupPost().getTitle());
            postSearchResultDto.setContent(post.getContent());
            postSearchResultDto.setAuthor(userProfileRepository.findByUser(user).getFullName());
            List<Post> commentList = postRepository.findByGroupPost(post.getGroupPost());
            List<CommentResultDto> commentResultDtoList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(commentList)) {
                commentList.forEach(item -> {
                    CommentResultDto commentResultDto = new CommentResultDto();
                    commentResultDto.setId(item.getId());
                    commentResultDto.setContent(item.getContent());
                    commentResultDto.setCreatedAt(item.getCreatedAt().toEpochMilli());
                    User foundUser = userRepository.findByEmail(item.getCreatedBy());

                    UserProfile userProfile = userProfileRepository.findByUser(foundUser);

                    if(userProfile != null){
                        commentResultDto.setCreatedBy(userProfile.getFullName());
                    } else {
                        DoctorProfile doctorProfile = doctorProfileRepository.findByUser(foundUser);
                        commentResultDto.setCreatedBy(doctorProfile.getFullName());
                    }

                    commentResultDto.setUpdatedAt(item.getUpdatedAt().toEpochMilli());
                    commentResultDtoList.add(commentResultDto);
                });
            }
            postSearchResultDto.setCommentList(commentResultDtoList);
            data.add(postSearchResultDto);
        }
        return data;
    }

    @Override
    public AbstractResponse getCommentListByPost(Integer id){
        GroupPost groupPost = groupPostRepository.findGroupPostById(id).orElse(null);
        List<CommentResultDto> commentResultDtoList = new ArrayList<>();
        if(groupPost != null){
            List<Post> commentList = postRepository.findByGroupPost(groupPost);
            if (!CollectionUtils.isEmpty(commentList)) {
                for (Post item : commentList) {
                    if((!StringUtils.isEmpty(postRepository.findByGroupPostOrderById(groupPost).get(0).getContent()) && postRepository.findByGroupPostOrderById(groupPost).get(0).getContent().equals(item.getContent())) || StringUtils.isEmpty(item.getContent())){
                        continue;
                    }
                    CommentResultDto commentResultDto = new CommentResultDto();
                    commentResultDto.setId(item.getId());
                    commentResultDto.setContent(item.getContent());
                    commentResultDto.setCreatedAt(item.getCreatedAt().toEpochMilli());
                    User foundUser = userRepository.findByEmail(item.getCreatedBy());

                    UserProfile userProfile = userProfileRepository.findByUser(foundUser);

                    if(userProfile != null){
                        commentResultDto.setCreatedBy(userProfile.getFullName());
                    } else {
                        DoctorProfile doctorProfile = doctorProfileRepository.findByUser(foundUser);
                        commentResultDto.setCreatedBy(doctorProfile.getFullName());
                    }
                    commentResultDto.setUpdatedAt(item.getUpdatedAt().toEpochMilli());
                    commentResultDtoList.add(commentResultDto);
                }
            }
        }
        return new AbstractResponse(commentResultDtoList);
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
@Override
public AbstractResponse getPostByLoggedUser(GetPostDto getPostDto){

    User user = userRepository.findUserById(getPostDto.getUserId());

    List<GroupPost> groupPostList = groupPostRepository.findAllByCreatedBy(user.getEmail());
    List<PostSearchResultDto> postSearchResultDtoList;
    if (groupPostList == null) {
        return new AbstractResponse("FAILED", "POST_NOT_FOUND", 404);
    } else {
        postSearchResultDtoList = convertPostToPostDto(groupPostList);
    }
    return new AbstractResponse(postSearchResultDtoList);
}


    @Override
    public AbstractResponse interactWithPost(Integer id, InteractWithPostDto interactWithPostDto) {

        GroupPost groupPost = groupPostRepository.findGroupPostById(id).orElse(null);

        if(groupPost != null){
            Post post = postRepository.findByGroupPost(groupPost).get(0);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = userRepository.findByEmail(authentication.getName());
            Reaction reaction = new Reaction(post, user, interactWithPostDto.getInteract() == null ? null : interactWithPostDto.getInteract());
            reactionRepository.save(reaction);
        }
        return new AbstractResponse();
    }

    @Override
    public AbstractResponse interactionCheck(Integer id){
        GroupPost groupPost = groupPostRepository.findGroupPostById(id).orElse(null);
        if(groupPost != null){
            Post post = postRepository.findByGroupPost(groupPost).get(0);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = userRepository.findByEmail(authentication.getName());
            Reaction reaction = reactionRepository.findByPostAndUser(post, user);
            if(reaction != null){
                return new AbstractResponse(true);
            }
        }
        return new AbstractResponse(false);
    }
}

