package com.cbnu.shop.repository;

import com.cbnu.shop.domain.Item;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;

@Repository
public class ItemRepository {
    private final JdbcTemplate template;
    public ItemRepository(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }
    public void saveItem(Item item) {
        String sql = "INSERT INTO item (category_id, item_name, price, quantity, brand, release_date) VALUES (?, ?, ?, ?, ?, ?)";

        template.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, item.getCategoryId());
            ps.setString(2, item.getItemName());
            ps.setBigDecimal(3, item.getPrice());
            ps.setLong(4, item.getQuantity());
            ps.setString(5, item.getBrand());
            ps.setTimestamp(6, new Timestamp(item.getReleaseDate().getTime()));
            return ps;
        });
    }

    public BigDecimal findPriceById(Long id) {
        String sql = "SELECT price from item where item_id = ?";
        return template.queryForObject(sql, BigDecimal.class, id);
    }
}
