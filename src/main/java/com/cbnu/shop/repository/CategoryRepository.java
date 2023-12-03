package com.cbnu.shop.repository;

import com.cbnu.shop.domain.Category;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
@Slf4j
public class CategoryRepository {

    private final JdbcTemplate template;
    private final Long etcID = 128L;

    public CategoryRepository(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    public Long findByCategoryName(String name) {
        try {
            String sql = "select * from category where category.name = ?";
            return template.queryForObject(sql, categoryRowMapper(), name).getCategoryId();
        } catch (EmptyResultDataAccessException e) {
            // 카테고리를 찾지 못한 경우에 대한 예외 처리
            log.warn("Category not found for {}", name);
            return etcID; // 또는 다른 디폴트 값으로 설정할 수 있음
        }
    }

    public List<Category> findAll() {
        String sql = "select * from category";
        return template.query(sql, categoryRowMapper());
    }

    private RowMapper<Category> categoryRowMapper() {
        return (rs, rowNum) -> {
            Category category = new Category();
            category.setCategoryId(rs.getLong("category_id"));
            category.setName(rs.getString("name"));
            category.setParentCategoryId(rs.getLong("parentcategory_id"));
            return category;
        };
    }
}
