package miranda.lucas.wexpurchasescontrol.integrationtests;

import com.fasterxml.jackson.databind.ObjectMapper;
import miranda.lucas.wexpurchasescontrol.dto.ExchangedPurchaseDTO;
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

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * Integration tests using Testcontainers.
 * Requires Docker to run a MySQL instance via MySQLContainer library.
 */

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class WexPurchasesControlApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @DynamicPropertySource
    static void mysqlProperties(DynamicPropertyRegistry registry) {
        registry.add("MYSQLDB_PORT", mysql::getFirstMappedPort);
        registry.add("MYSQLDB_DATABASE", mysql::getDatabaseName);
        registry.add("MYSQLDB_USERNAME", mysql::getUsername);
        registry.add("MYSQLDB_PASSWORD", mysql::getPassword);
    }


    static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.2.0")
            .withUsername("test")
            .withPassword("123456")
            .withDatabaseName("testdb");

    static {
        mysql.start();
    }



    @Test
    public void testCreatePurchaseSuccess1() throws Exception {
        String purchaseJson = "{\"description\": \"Chocolate\", \"transactionDate\": \"2023-11-08\", \"purchaseAmountUSD\": 1.00}";

        MvcResult result = this.mockMvc.perform(post("/store").contentType(MediaType.APPLICATION_JSON).content(purchaseJson)).andReturn();

        Assertions.assertEquals( HttpStatus.CREATED.value(), result.getResponse().getStatus());

    }

    @Test
    public void testCreatePurchaseSuccess2() throws Exception {
        String purchaseJson = "{\"description\": \"Acentuação\", \"transactionDate\": \"2023-11-08\", \"purchaseAmountUSD\": 1}";

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
    public void testCreatePurchaseIncompleteDataNoDescription() throws Exception {
        String purchaseJson = "{\"transactionDate\": \"2023-11-08\", \"purchaseAmountUSD\": 1.00}";

        MvcResult result = this.mockMvc.perform(post("/store").contentType(MediaType.APPLICATION_JSON).content(purchaseJson)).andReturn();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());

    }


    @Test
    public void testCreatePurchaseInvalidNegativeAmount() throws Exception {
        String purchaseJson = "{\"description\": \"Chocolate\", \"transactionDate\": \"2023-11-08\", \"purchaseAmountUSD\": -1.00}";

        MvcResult result = this.mockMvc.perform(post("/store").contentType(MediaType.APPLICATION_JSON).content(purchaseJson)).andReturn();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());

    }

    @Test
    public void testCreatePurchaseInvalidNumber() throws Exception {
        String purchaseJson = "{\"description\": \"Chocolate\", \"transactionDate\": \"2023-11-08\", \"purchaseAmountUSD\": \"eeee\"}";

        MvcResult result = this.mockMvc.perform(post("/store").contentType(MediaType.APPLICATION_JSON).content(purchaseJson)).andReturn();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());

    }


    @Test
    public void testCreatePurchaseInvalidFutureDate() throws Exception {
        String purchaseJson = "{\"description\": \"Chocolate\", \"transactionDate\": \"9999-11-08\", \"purchaseAmountUSD\": \"1.00\"}";

        MvcResult result = this.mockMvc.perform(post("/store").contentType(MediaType.APPLICATION_JSON).content(purchaseJson)).andReturn();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());

    }


    @Test
    public void testCreatePurchaseInvalidEmptyDescription() throws Exception {
        String purchaseJson = "{\"description\": \"\", \"transactionDate\": \"2023-11-08\", \"purchaseAmountUSD\": \"1.00\"}";

        MvcResult result = this.mockMvc.perform(post("/store").contentType(MediaType.APPLICATION_JSON).content(purchaseJson)).andReturn();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());

    }


    @Test
    public void testCreatePurchaseInvalidDate() throws Exception {
        String purchaseJson = "{\"description\": \"Milk\", \"transactionDate\": \"2021-99-08\", \"purchaseAmountUSD\": \"1.00\"}";

        MvcResult result = this.mockMvc.perform(post("/store").contentType(MediaType.APPLICATION_JSON).content(purchaseJson)).andReturn();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());

    }

    @Test
    public void testCreatePurchaseInvalidDescriptionTooLong() throws Exception {
        String purchaseJson = "{\"description\": \"Another useful approach is to not start the server at all but to test only the layer below that, where Spring handles the incoming HTTP request and hands it off to your controller. \", \"transactionDate\": \"2021-08-01\", \"purchaseAmountUSD\": \"1.00\"}";

        MvcResult result = this.mockMvc.perform(post("/store").contentType(MediaType.APPLICATION_JSON).content(purchaseJson)).andReturn();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());

    }

    @Test
    public void testCreateAndGetPurchaseSucess1() throws Exception {
        BigDecimal amount = new BigDecimal("15.35");
        String description = "Uber from A to B";
        String purchaseJson = String.format("{\"description\": \"%s\", \"transactionDate\": \"2022-08-01\", \"purchaseAmountUSD\": \"%s\"}", description, amount);

        MvcResult result = this.mockMvc.perform(post("/store").contentType(MediaType.APPLICATION_JSON).content(purchaseJson)).andReturn();

        Assertions.assertEquals(HttpStatus.CREATED.value(), result.getResponse().getStatus());
        Long id = Long.valueOf(result.getResponse().getContentAsString());

        MvcResult resultPurchase = this.mockMvc.perform(get(String.format("/get?id=%d&country=Brazil&currency=Real", id)).contentType(MediaType.APPLICATION_JSON).content(purchaseJson)).andReturn();
        Assertions.assertEquals(HttpStatus.OK.value(), resultPurchase.getResponse().getStatus());

        ExchangedPurchaseDTO exchangedPurchaseDTO = objectMapper.readValue(resultPurchase.getResponse().getContentAsString(), ExchangedPurchaseDTO.class);
        Assertions.assertEquals(id, exchangedPurchaseDTO.getId());
        Assertions.assertEquals(amount, exchangedPurchaseDTO.getPurchaseAmountUSD());
        Assertions.assertEquals(description, exchangedPurchaseDTO.getDescription());

    }


    @Test
    public void testCreateAndGetPurchaseSucess2() throws Exception {
        BigDecimal amount = new BigDecimal("1.17");
        String description = "Uber from A to B";
        String purchaseJson = String.format("{\"description\": \"%s\", \"transactionDate\": \"2022-09-06\", \"purchaseAmountUSD\": \"%s\"}", description, amount);

        MvcResult result = this.mockMvc.perform(post("/store").contentType(MediaType.APPLICATION_JSON).content(purchaseJson)).andReturn();

        Assertions.assertEquals(HttpStatus.CREATED.value(), result.getResponse().getStatus());
        Long id = Long.valueOf(result.getResponse().getContentAsString());

        MvcResult resultPurchase = this.mockMvc.perform(get(String.format("/get?id=%d&country=United Kingdom&currency=Pound", id)).contentType(MediaType.APPLICATION_JSON).content(purchaseJson)).andReturn();
        Assertions.assertEquals(HttpStatus.OK.value(), resultPurchase.getResponse().getStatus());

        ExchangedPurchaseDTO exchangedPurchaseDTO = objectMapper.readValue(resultPurchase.getResponse().getContentAsString(), ExchangedPurchaseDTO.class);
        Assertions.assertEquals(id, exchangedPurchaseDTO.getId());
        Assertions.assertEquals(amount, exchangedPurchaseDTO.getPurchaseAmountUSD());
        Assertions.assertEquals(description, exchangedPurchaseDTO.getDescription());

    }

    @Test
    public void testCreateAndGetPurchaseCurrencyNotAvailable() throws Exception {
        BigDecimal amount = new BigDecimal("0.42");
        String description = "Maternity parking ticket";
        String purchaseJson = String.format("{\"description\": \"%s\", \"transactionDate\": \"1986-08-01\", \"purchaseAmountUSD\": \"%s\"}", description, amount);

        MvcResult result = this.mockMvc.perform(post("/store").contentType(MediaType.APPLICATION_JSON).content(purchaseJson)).andReturn();

        Assertions.assertEquals(HttpStatus.CREATED.value(), result.getResponse().getStatus());
        Long id = Long.valueOf(result.getResponse().getContentAsString());

        MvcResult resultPurchase = this.mockMvc.perform(get(String.format("/get?id=%d&country=Canada&currency=Real", id)).contentType(MediaType.APPLICATION_JSON).content(purchaseJson)).andReturn();
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), resultPurchase.getResponse().getStatus());
    }


    @Test
    public void testCreateAndGetPurchaseSucessEmptyCountryAndCurrency() throws Exception {
        BigDecimal amount = new BigDecimal("0.42");
        String description = "Maternity parking ticket";
        String purchaseJson = String.format("{\"description\": \"%s\", \"transactionDate\": \"1986-08-01\", \"purchaseAmountUSD\": \"%s\"}", description, amount);

        MvcResult result = this.mockMvc.perform(post("/store").contentType(MediaType.APPLICATION_JSON).content(purchaseJson)).andReturn();

        Assertions.assertEquals(HttpStatus.CREATED.value(), result.getResponse().getStatus());
        Long id = Long.valueOf(result.getResponse().getContentAsString());

        MvcResult resultPurchase = this.mockMvc.perform(get(String.format("/get?id=%d", id)).contentType(MediaType.APPLICATION_JSON).content(purchaseJson)).andReturn();
        Assertions.assertEquals(HttpStatus.OK.value(), resultPurchase.getResponse().getStatus());

        ExchangedPurchaseDTO exchangedPurchaseDTO = objectMapper.readValue(resultPurchase.getResponse().getContentAsString(), ExchangedPurchaseDTO.class);
        Assertions.assertEquals(id, exchangedPurchaseDTO.getId());
        Assertions.assertEquals(amount, exchangedPurchaseDTO.getPurchaseAmountUSD());
        Assertions.assertEquals(description, exchangedPurchaseDTO.getDescription());
    }


}
