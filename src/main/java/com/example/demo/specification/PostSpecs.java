package com.example.demo.specification;

import com.example.demo.dtos.SearchDto;
import com.example.demo.entities.Post;
import com.example.demo.entities.Post_;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class PostSpecs{

    public static Specification<Post> search(SearchDto searchDto) {
        return (root, query, builder) -> {
            List<Predicate> conditions = new ArrayList<>();

            if(Strings.isNotEmpty(searchDto.getKey())){
                conditions.add(builder.like(builder.upper(root.get(Post_.CONTENT)), '%' + searchDto.getKey().toUpperCase() + '%'));
            }

            query.orderBy(builder.desc(root.get(Post_.CREATED_AT)));
            return builder.and(conditions.toArray(new Predicate[0]));
        };
    }

//    public static Specification<MobileKit> searchKitName(OrderFilterRequest obj) {
//        return (root, query, builder) -> {
//            List<Predicate> conditions = new ArrayList<>();
//
//            if (!Strings.isEmpty(obj.getKey())) {
//                conditions.add(builder.like(builder.upper(root.get(MobileKit_.KIT_NAME)), "%" + obj.getKey().toUpperCase() + "%"));
//            }
//
//            query.orderBy(builder.asc(root.get(MobileKit_.CREATED_AT)));
//            return builder.and(conditions.toArray(new Predicate[0]));
//        };
//    }
}
