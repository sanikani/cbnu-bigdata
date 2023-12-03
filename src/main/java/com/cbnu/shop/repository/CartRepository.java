package com.cbnu.shop.repository;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

@Repository
public class CartRepository {
    private final JdbcTemplate template;
    private static final int ITEM_COUNT = 1282;
    private static final int CART_COUNT = 10000;
    private static final int DATA_COUNT = 30000;

    public CartRepository(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    public void insertRandomCartItems() {
        String sql = "INSERT INTO cart_item (item_id, cart_id, quantity) VALUES (?, ?, ?)";

        template.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                Random random = new Random();
                long itemId = random.nextInt(ITEM_COUNT - 2) + 3;  // Item IDs from 3 to 1282
                long cartId = random.nextInt(CART_COUNT) + 1;      // Cart IDs from 1 to 10000
                int quantity = random.nextInt(30) + 1;            // Quantity from 1 to 30

                preparedStatement.setLong(1, itemId);
                preparedStatement.setLong(2, cartId);
                preparedStatement.setInt(3, quantity);
            }

            @Override
            public int getBatchSize() {
                return DATA_COUNT;
            }
        });
    }
}
