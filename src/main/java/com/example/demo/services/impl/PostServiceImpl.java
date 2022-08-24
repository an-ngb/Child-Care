package com.example.demo.services.impl;

import com.example.demo.dtos.AbstractResponse;
import com.example.demo.dtos.CommentDto;
import com.example.demo.dtos.EditDto;
import com.example.demo.dtos.PostDto;
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

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
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
        post.setRole(user.getRole());
        postRepository.save(post);
        return new AbstractResponse(post);
    }

    @Override
    public AbstractResponse edit(EditDto editDto) {
        Post post = postRepository.findById(editDto.getId()).orElse(null);
        if(post == null){
            throw new NotFoundException();
        }
        BeanUtils.copyProperties(editDto, post, getNullPropertyNames(editDto));
        postRepository.save(post);
        return new AbstractResponse(post);
    }

    @Override
    public AbstractResponse comment(CommentDto commentDto) {
        Post post = postRepository.findById(commentDto.getId()).orElse(null);
        if(post == null){
            throw new NotFoundException();
        }
        List<Comment> listComment = post.getComment();
        listComment.add(new Comment(commentDto.getContent(), post));
        return new AbstractResponse(commentDto);
    }
}
