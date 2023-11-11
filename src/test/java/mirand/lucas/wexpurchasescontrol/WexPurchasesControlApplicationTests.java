package mirand.lucas.wexpurchasescontrol;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.MySQLContainer;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class WexPurchasesControlApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.2.0")
            .withUsername("test")
            .withPassword("123456")
            .withDatabaseName("testdb");

    static {
        mysql.start();
    }

    @DynamicPropertySource
    static void mysqlProperties(DynamicPropertyRegistry registry) {
        registry.add("MYSQLDB_PORT", mysql::getFirstMappedPort);
        registry.add("MYSQLDB_DATABASE", mysql::getDatabaseName);
        registry.add("MYSQLDB_USERNAME", mysql::getUsername);
        registry.add("MYSQLDB_PASSWORD", mysql::getPassword);
    }

    @Test
    public void testCreatePurchaseSuccess1() throws Exception {
        String purchaseJson = "{\"description\": \"Chocolate\", \"transactionDate\": \"2023-11-08\", \"purchaseAmountUSD\": 1.00}";

        MvcResult result = this.mockMvc.perform(post("/store").contentType(MediaType.APPLICATION_JSON).content(purchaseJson)).andReturn();

        Assertions.assertEquals( HttpStatus.CREATED.value(), result.getResponse().getStatus());

    }

    @Test
    public void testCreatePurchaseInvalidAmount() throws Exception {
        String purchaseJson = "{\"description\": \"Chocolate\", \"transactionDate\": \"2023-11-08\", \"purchaseAmountUSD\": 1.00504}";

        MvcResult result = this.mockMvc.perform(post("/store").contentType(MediaType.APPLICATION_JSON).content(purchaseJson)).andReturn();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());

    }


    @Test
    public void testCreatePurchaseIncompleteData() throws Exception {
        String purchaseJson = "{\"transactionDate\": \"2023-11-08\", \"purchaseAmountUSD\": 1.00}";

        MvcResult result = this.mockMvc.perform(post("/store").contentType(MediaType.APPLICATION_JSON).content(purchaseJson)).andReturn();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());

    }


}
