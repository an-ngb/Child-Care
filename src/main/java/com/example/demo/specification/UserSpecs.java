package com.example.demo.specification;

import com.example.demo.dtos.SearchDto;
import com.example.demo.entities.*;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class UserSpecs {

    public static Specification<UserProfile> search(SearchDto searchDto) {
        return (root, query, builder) -> {
            List<Predicate> conditions = new ArrayList<>();

            if(Strings.isNotEmpty(searchDto.getKey())){
                conditions.add(builder.like(builder.upper(root.get(UserProfile_.FULL_NAME) == null ? root.get(DoctorProfile_.FULL_NAME) : root.get(UserProfile_.FULL_NAME)), '%' + searchDto.getKey().toUpperCase() + '%'));
            }

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
