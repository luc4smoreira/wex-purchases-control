package miranda.lucas.wexpurchasescontrol.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Component
public class DatabaseInitializer {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public DatabaseInitializer(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @PostConstruct
    public void initialize() {
        if (!isTableExist("purchase_transaction")) {
            logger.warn("Table doesn't exist, creating....");
            try {
                Resource resource = new ClassPathResource("sql/create_purchase_transaction_table.sql");

                ScriptUtils.executeSqlScript(jdbcTemplate.getDataSource().getConnection(), resource);
            } catch (SQLException e) {
                logger.error("Error creating database.", e);
            }
        }
    }

    private boolean isTableExist(String tableName) {
        try {
            List<Map<String, Object>> result = jdbcTemplate.queryForList("SHOW TABLES LIKE '" + tableName + "'");
            return !result.isEmpty();
        } catch (Exception e) {
            logger.error("Error checking if table exist.", e);
            return false;
        }
    }
}
