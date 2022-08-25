package com.example.demo.services.impl;

import com.example.demo.dtos.*;
import com.example.demo.entities.Comment;
import com.example.demo.entities.Post;
import com.example.demo.entities.User;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.repositories.CommentRepository;
import com.example.demo.repositories.PostRepository;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.repositories.UserRepository;
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
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

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
        Post post = new Post();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName());
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setRoleId(user.getRole().getId());
        postRepository.save(post);
        return new AbstractResponse(post);
    }

    @Override
    public AbstractResponse edit(EditDto editDto) {
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
        return new AbstractResponse();
    }

    @Override
    public AbstractResponse viewAllPost() {
        List<Post> list = postRepository.findAll();
        return new AbstractResponse(list);
    }

    @Override
    public AbstractResponse viewAllPostAsc() {
        List<Post> list = postRepository.findAllByOrderByIdAsc();
        return new AbstractResponse(list);
    }

    @Override
    public AbstractResponse viewAllPostDesc() {
        List<Post> list = postRepository.findAllByOrderByIdDesc();
        return new AbstractResponse(list);
    }

    @Override
    public AbstractResponse viewMostLikedPost(FilterRequest filterRequest) {
        List<Post> postList = postRepository.findAllByRoleIdOrderByTotalLikeAsc(filterRequest.getRoleId());
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
            data.add(postSearchResultDto);
        }
        return new AbstractResponse(data);
    }

    @Override
    public AbstractResponse like(LikeDto likeDto) {
        Post post = postRepository.findPostById(likeDto.getId());
        if (post == null) {
            throw new NotFoundException();
        }
        Integer prevLikes = post.getTotalLike();
        if (prevLikes == null) {
            prevLikes = 0;
        }
        post.setTotalLike(prevLikes += 1);
        postRepository.save(post);
        return new AbstractResponse();
    }
}
