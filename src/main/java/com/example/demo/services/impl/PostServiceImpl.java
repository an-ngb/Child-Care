package com.example.demo.services.impl;

import com.example.demo.dtos.*;
import com.example.demo.entities.Comment;
import com.example.demo.entities.Post;
import com.example.demo.entities.Tag;
import com.example.demo.entities.User;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.exceptions.UnauthorizedException;
import com.example.demo.repositories.*;
import com.example.demo.services.PostService;
import com.example.demo.specification.PostSpecs;
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
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TagRepository tagRepository;
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

        Post post = new Post();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName());
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setRoleId(user.getRole().getId());
        Set<Tag> tagList = new HashSet<>();
        if(postDto.getTagList() != null){
            for (String s : postDto.getTagList()) {
                Tag tag = tagRepository.findByTagName(s);
                if (tag != null){
                    tagList.add(tag);
                }
            }
        }
        post.setTagsList(tagList);
        postRepository.save(post);

        Integer prevPosts = user.getTotalPost();
        if (prevPosts != null) {
            user.setTotalPost(prevPosts + 1);
        } else {
            user.setTotalPost(0);
        }
        userRepository.save(user);

        return new AbstractResponse(post);
    }

    @Override
    public AbstractResponse edit(EditDto editDto) {
        if(sessionService.isTokenExpire()){
            return new AbstractResponse("FAILED", "TOKEN_EXPIRED", 400);
        }
        Post post = postRepository.findById(editDto.getId()).orElse(null);
        if (post == null) {
            throw new NotFoundException();
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
        Post post = postRepository.findPostById(commentDto.getId());
        if (post == null) {
            throw new NotFoundException();
        }
        Comment comment = new Comment();
        comment.setComment(commentDto.getContent());
        comment.setPost(post);
        commentRepository.save(comment);
        post.getComment().add(comment);
        postRepository.save(post);

        User user = userRepository.findByEmail(post.getCreatedBy());
        Integer prevComments = user.getTotalComment();
        if (prevComments != null) {
            user.setTotalComment(prevComments + 1);
        } else {
            user.setTotalComment(0);
        }
        userRepository.save(user);

        return new AbstractResponse();
    }

    @Override
    public AbstractResponse viewAllPost() {
        if(sessionService.isTokenExpire()){
            return new AbstractResponse("FAILED", "TOKEN_EXPIRED", 400);
        }
        List<Post> list = postRepository.findAll();
        return new AbstractResponse(list);
    }

    @Override
    public AbstractResponse viewAllPostAsc() {
        if(sessionService.isTokenExpire()){
            return new AbstractResponse("FAILED", "TOKEN_EXPIRED", 400);
        }
        List<Post> list = postRepository.findAllByOrderByIdAsc();
        return new AbstractResponse(list);
    }

    @Override
    public AbstractResponse viewAllPostDesc() {
        if(sessionService.isTokenExpire()){
            return new AbstractResponse("FAILED", "TOKEN_EXPIRED", 400);
        }
        List<Post> list = postRepository.findAllByOrderByIdDesc();
        return new AbstractResponse(list);
    }

    @Override
    public AbstractResponse viewMostLikedPost(FilterRequest filterRequest) {
        if(sessionService.isTokenExpire()){
            return new AbstractResponse("FAILED", "TOKEN_EXPIRED", 400);
        }
        List<Post> postList = postRepository.findAllByRoleIdOrderByTotalLikeAsc(filterRequest.getRoleId());
        List<PostSearchResultDto> postSearchResultDtoList = convertPostToPostDto(postList);
        return new AbstractResponse(postSearchResultDtoList);
    }

    @Override
    public AbstractResponse viewPostByTag(FilterRequest filterRequest) {
        if(sessionService.isTokenExpire()){
            return new AbstractResponse("FAILED", "TOKEN_EXPIRED", 400);
        }
        List<Post> postList = postRepository.findAll(PostSpecs.search(filterRequest));
        List<PostSearchResultDto> postSearchResultDtoList = convertPostToPostDto(postList);
        return new AbstractResponse(postSearchResultDtoList);
    }

    public List<PostSearchResultDto> convertPostToPostDto(List<Post> postList) {
        List<PostSearchResultDto> data = new ArrayList<>();
        for (Post post : postList) {
            PostSearchResultDto postSearchResultDto = new PostSearchResultDto();
            User user = userRepository.findByEmail(post.getCreatedBy());
            postSearchResultDto.setId(post.getId());
            postSearchResultDto.setTitle(post.getTitle());
            postSearchResultDto.setContent(post.getContent());
            if (post.getTotalDislike() != null) {
                postSearchResultDto.setTotalDislike(post.getTotalDislike());
            } else {
                postSearchResultDto.setTotalDislike(0);
            }
            if (post.getTotalLike() != null) {
                postSearchResultDto.setTotalLike(post.getTotalLike());
            } else {
                postSearchResultDto.setTotalLike(0);
            }
            postSearchResultDto.setDoctorName(user.getFullName());
            List<Comment> commentList = commentRepository.findAllByPost(post);
            List<CommentResultDto> commentResultDtoList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(commentList)) {
                commentList.forEach(item -> {
                    CommentResultDto commentResultDto = new CommentResultDto();
                    commentResultDto.setId(item.getId());
                    commentResultDto.setContent(item.getComment());
                    if (item.getTotalCommentDislike() != null) {
                        commentResultDto.setTotalDislike(item.getTotalCommentDislike());
                    } else {
                        commentResultDto.setTotalDislike(0);
                    }
                    if (item.getTotalCommentLike() != null) {
                        commentResultDto.setTotalLike(item.getTotalCommentLike());
                    } else {
                        commentResultDto.setTotalLike(0);
                    }
                    commentResultDtoList.add(commentResultDto);
                });
            }
            postSearchResultDto.setCommentList(commentResultDtoList);

            Set<Tag> tagList = post.getTagsList();
            List<TagDto> tagDtoList = new ArrayList<>();
            if(!CollectionUtils.isEmpty(tagList)){
                tagList.forEach(item -> {
                    TagDto tagDto = new TagDto();
                    tagDto.setTag(item.getTagName());
                    tagDtoList.add(tagDto);
                });
            }
            postSearchResultDto.setTagListDto(tagDtoList);

            data.add(postSearchResultDto);
        }
        return data;
    }

    @Override
    public AbstractResponse like(InteractDto interactDto) {
        if(sessionService.isTokenExpire()){
            return new AbstractResponse("FAILED", "TOKEN_EXPIRED", 400);
        }
        Post post = postRepository.findPostById(interactDto.getId());
        if (post == null) {
            throw new NotFoundException();
        }

        //Prev likes of post.
        Integer prevLikes = post.getTotalLike();
        if (prevLikes == null) {
            prevLikes = 0;
        }
        post.setTotalLike(prevLikes + 1);

        //Prev likes of author.
        User user = userRepository.findByEmail(post.getCreatedBy());
        Integer prevLikesAuthor = user.getTotalLike();
        if (prevLikesAuthor == null) {
            prevLikesAuthor = 0;
        }
        user.setTotalLike(prevLikesAuthor + 1);
        userRepository.save(user);
        postRepository.save(post);
        return new AbstractResponse();
    }

    @Override
    public AbstractResponse dislike(InteractDto interactDto) {
        if(sessionService.isTokenExpire()){
            return new AbstractResponse("FAILED", "TOKEN_EXPIRED", 400);
        }
        Post post = postRepository.findPostById(interactDto.getId());
        if (post == null) {
            throw new NotFoundException();
        }

        //Prev dislikes of post.
        Integer prevDislikes = post.getTotalDislike();
        if (prevDislikes == null) {
            prevDislikes = 0;
        }
        post.setTotalDislike(prevDislikes + 1);

        //Prev likes of author.
        User user = userRepository.findByEmail(post.getCreatedBy());
        Integer prevDislikesAuthor = user.getTotalDislike();
        if (prevDislikesAuthor == null) {
            prevDislikesAuthor = 0;
        }
        user.setTotalDislike(prevDislikesAuthor + 1);
        userRepository.save(user);
        postRepository.save(post);
        return new AbstractResponse();
    }
}
