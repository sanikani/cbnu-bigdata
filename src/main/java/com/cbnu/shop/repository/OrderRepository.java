package com.cbnu.shop.repository;



import com.cbnu.shop.domain.Order;
import com.cbnu.shop.domain.PaymentStatus;
import com.cbnu.shop.domain.ShippingStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Repository
public class OrderRepository{

    private final JdbcTemplate template;
    private static final int MEMBER_COUNT = 10000;
    private static final int DATA_COUNT = 5000;

    @Autowired
    public OrderRepository(JdbcTemplate jdbcTemplate) {
        this.template = jdbcTemplate;
    }

    public List<Order> findAll() {
        String sql = "SELECT * FROM `order`";
        return template.query(sql, this::mapRowToOrder);
    }

    public Order findById(Long orderId) {
        String sql = "SELECT * FROM `order` WHERE order_id = ?";
        return template.queryForObject(sql, this::mapRowToOrder, orderId);
    }

    public void save(Order order) {
        String sql = "INSERT INTO `order` (member_id, total_price, order_date, payment_status, shipping_status) VALUES (?, ?, ?, ?, ?)";
        template.update(sql, order.getMemberId(), order.getTotalPrice(),
                order.getOrderDate(), order.getPaymentStatus(), order.getShippingStatus());
    }

    private Order mapRowToOrder(ResultSet rs, int rowNum) throws SQLException {
        Order order = new Order();
        order.setOrderId(rs.getLong("order_id"));
        order.setMemberId(rs.getLong("member_id"));
        order.setTotalPrice(rs.getBigDecimal("total_price"));
        order.setOrderDate(rs.getTimestamp("order_date"));
        order.setPaymentStatus(Enum.valueOf(PaymentStatus.class, rs.getString("payment_status")));
        order.setShippingStatus(Enum.valueOf(ShippingStatus.class, rs.getString("shipping_status")));
        return order;
    }

    public void insertRandomOrders() {
        String sql = "INSERT INTO `order` (member_id, total_price, order_date, payment_status, shipping_status) VALUES (?, ?, ?, ?, ?)";

        template.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                Random random = new Random();
                long memberId = random.nextInt(MEMBER_COUNT) + 1;
                BigDecimal totalPrice = null;
                LocalDateTime orderDate = LocalDateTime.now().minusDays(random.nextInt(365));
                Timestamp orderTimestamp = Timestamp.valueOf(orderDate);
                String paymentStatus;
                if (random.nextBoolean()) {
                    paymentStatus = "미결제";
                } else {
                    paymentStatus = random.nextBoolean() ? "결제완료" : "취소";
                }

                String shippingStatus;
                if ("미결제".equals(paymentStatus)) {
                    shippingStatus = "상품준비중";
                } else {
                    shippingStatus = random.nextBoolean() ? "배송중" : "배송완료";
                }

                preparedStatement.setLong(1, memberId);
                preparedStatement.setBigDecimal(2, totalPrice);
                preparedStatement.setTimestamp(3, orderTimestamp);
                preparedStatement.setString(4, paymentStatus);
                preparedStatement.setString(5, shippingStatus);

                if (i % 1000 == 0) {
                    preparedStatement.executeBatch();
                }
            }

            @Override
            public int getBatchSize() {
                return DATA_COUNT;
            }
        });
    }
}