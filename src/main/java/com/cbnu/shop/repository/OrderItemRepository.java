package com.cbnu.shop.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class OrderItemRepository {

    private final JdbcTemplate template;
    private final ItemRepository itemRepository;
    private static final int ITEM_COUNT = 1280;
    private static final int ORDER_COUNT = 5000;

    //주문번호 랜덤(1~5000) 상품번호 랜덤(1~1283) 상품 가격 ItemRepository.findPriceById, 상품 수량 랜덤
    public void insertIntoRandomOrderItems() {
        Random random = new Random();
        for (int i = 1; i <= ORDER_COUNT + 1; i++) {
            Set<Long> addedItemIds = new HashSet<>();  // Track added item_ids for each order_id
            for (int j = 0; j < random.nextInt(10); j++) {
                long order_id = i;
                long item_id = random.nextInt(ITEM_COUNT) + 1;

                // Check if the same order_id and item_id combination already exists
                if (!addedItemIds.contains(item_id)) {
                    BigDecimal item_price = itemRepository.findPriceById(item_id);
                    long quantity = random.nextInt(10)+1;

                    // Insert into order_item table
                    String sql = "INSERT INTO `order_item` (order_id, item_id, item_price, quantity) VALUES (?, ?, ?, ?)";
                    template.update(connection -> {
                        PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                        ps.setLong(1, order_id);
                        ps.setLong(2, item_id);
                        ps.setBigDecimal(3, item_price);
                        ps.setLong(4, quantity);
                        return ps;
                    });

                    // Add the item_id to the set to track added items for the current order_id
                    addedItemIds.add(item_id);
                }
            }
        }
    }
}
