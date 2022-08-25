package com.example.demo.specification;

import com.example.demo.constant.ROLE_NAME;
import com.example.demo.dtos.FilterRequest;
import com.example.demo.entities.Post;
import com.example.demo.entities.User_;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PostSpecs{

    public static Specification<Post> search(FilterRequest obj) {
        return (root, query, builder) -> {
            List<Predicate> conditions = new ArrayList<>();

            List<ROLE_NAME> roleList = Arrays.asList(
                    ROLE_NAME.DOCTOR);

            if (!Strings.isEmpty(obj.getRoleName()) && roleList.contains(ROLE_NAME.valueOf(obj.getRoleName().toUpperCase()))) {
                conditions.add(builder.equal(root.get(User_.ROLE), ROLE_NAME.valueOf(obj.getRoleName())));
            } else if (!CollectionUtils.isEmpty(obj.getRoleNameList()) && obj.getRoleNameList() != null) {
                List<ROLE_NAME> checkRoleList = new ArrayList<>();
                obj.getRoleNameList().forEach(item -> {
                            if (roleList.contains(ROLE_NAME.valueOf(item))) {
                                checkRoleList.add(ROLE_NAME.valueOf(item));
                            }
                        }
                );
                conditions.add(root.get(User_.ROLE).in(checkRoleList));
            } else {
                conditions.add(root.get(User_.ROLE).in(roleList));
            }

            query.orderBy(builder.desc(root.get(User_.TOTAL_LIKE)));
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
