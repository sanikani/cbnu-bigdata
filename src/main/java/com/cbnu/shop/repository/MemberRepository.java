package com.cbnu.shop.repository;

import com.cbnu.shop.domain.Category;
import com.cbnu.shop.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class MemberRepository {

    private final JdbcTemplate template;

    public MemberRepository(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    public List<Member> findAll() {
        String sql = "select * from member";
        return template.query(sql, memberRowMapper());
    }

    public List<Member> findAll(String name, Long age) {

        String sql = "select * from member";
        //동적 쿼리
        if (StringUtils.hasText(name) || age != null) {
            sql += " where";
        }
        boolean andFlag = false;
        List<Object> param = new ArrayList<>();
        if (StringUtils.hasText(name)) {
            sql += " name like concat('%',?,'%')";
            param.add(name);
            andFlag = true;
        }
        if (age != null) {
            if (andFlag) {
                sql += " and";
            }
            sql += " age >= ?";
            param.add(age);
        }
        log.info("sql={}", sql);
        return template.query(sql, memberRowMapper(), param.toArray());
    }

    private RowMapper<Member> memberRowMapper() {
        return (rs, rowNum) -> {
            Member member = new Member();
            member.setMemberId(rs.getLong("member_id"));
            member.setName(rs.getString("name"));
            member.setPhone(rs.getString("phone"));
            member.setEmail(rs.getString("email"));
            member.setAge(rs.getLong("age"));
            member.setPassword(rs.getString("password"));
            member.setRegistrationDate(rs.getDate("registration_date"));
            return member;
        };
    }

    public void saveCategories(MultiValueMap<Long, String> categories) {
        String sql = "INSERT INTO category (parentcategory_id, name) VALUES (?, ?)";

        for (Long parentCategoryId : categories.keySet()) {
            List<String> names = categories.get(parentCategoryId);
            for (String name : names) {
                template.update(sql, parentCategoryId, name);
            }
        }
    }


}


