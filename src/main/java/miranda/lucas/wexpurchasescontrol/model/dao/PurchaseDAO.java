package miranda.lucas.wexpurchasescontrol.model.dao;

import miranda.lucas.wexpurchasescontrol.model.entity.PurchaseTransaction;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Repository
public class PurchaseDAO implements RowMapper<PurchaseTransaction> {
    private final JdbcTemplate jdbcTemplate;

    public PurchaseDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public void insertPurchase(PurchaseTransaction purchase) {
        final String sql = "INSERT INTO purchase_transaction (description, purchase_amount_usd, transaction_date) VALUES (?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(
                new PreparedStatementCreator() {
                    public PreparedStatement createPreparedStatement(java.sql.Connection connection) throws SQLException {
                        PreparedStatement stm = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                        stm.setObject(1, purchase.getDescription());
                        stm.setObject(2, purchase.getPurchaseAmountUsd());
                        stm.setObject(3, purchase.getTransactionDate());

                        return stm;
                    }
                }, keyHolder);

        // Obtém o ID gerado
        purchase.setId(keyHolder.getKey().longValue());
    }


    public PurchaseTransaction findPurchaseById(Long id) {
        String sql = "SELECT id, description, purchase_amount_usd, transaction_date FROM purchase_transaction WHERE id = ?";

        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(PurchaseTransaction.class), id);
    }


    @Override
    public PurchaseTransaction mapRow(ResultSet rs, int rowNum) throws SQLException {
        PurchaseTransaction purchase = new PurchaseTransaction();
        purchase.setId(rs.getLong("id"));
        purchase.setDescription(rs.getString("description"));
        purchase.setPurchaseAmountUsd(rs.getBigDecimal("purchase_amount_usd"));
        purchase.setTransactionDate(rs.getDate("purchase_amount_usd").toLocalDate());

        return purchase;
    }
}
