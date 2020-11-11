package co.jueyi.geekshop;

import co.jueyi.geekshop.config.TestConfig;
import co.jueyi.geekshop.service.ConfigService;
import co.jueyi.geekshop.types.common.CreateAddressInput;
import co.jueyi.geekshop.types.common.CreateCustomerInput;
import co.jueyi.geekshop.types.customer.Customer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.javafaker.Faker;
import com.graphql.spring.boot.test.GraphQLResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.IOException;

/**
 * A service for creating mock data via the GraphQL API.
 *
 * Created on Nov, 2020 by @author bobo
 */
@Slf4j
public class MockDataService {

    public static final String MOCK_GRAPHQL_RESOURCE_TEMPLATE = "graphql/mock/%s.graphqls";
    public static final String CREATE_CUSTOMER =
            String.format(MOCK_GRAPHQL_RESOURCE_TEMPLATE, "create_customer");
    public static final String CREATE_CUSTOMER_ADDRESS =
            String.format(MOCK_GRAPHQL_RESOURCE_TEMPLATE, "create_customer_address");

    public static final String TEST_PASSWORD = "test123456";

    @Autowired
    ObjectMapper mapper;

    @Autowired
    @Qualifier(TestConfig.ADMIN_CLIENT_BEAN)
    ApiClient adminClient;

    @Autowired
    ConfigService configService;

    private final Faker faker = Faker.instance();

    public void populate(PopulateOptions options) throws IOException {
        boolean originalRequiredVerification = this.configService.getAuthOptions().isRequireVerification();
        this.configService.getAuthOptions().setRequireVerification(false);
        adminClient.asSuperAdmin();

        Integer customerCount = options.getCustomerCount() == null ? 5 : options.getCustomerCount();
        populateCustomers(customerCount);

        this.configService.getAuthOptions().setRequireVerification(originalRequiredVerification);
    }

    public void populateCustomers() throws IOException {
        this.populateCustomers(5);
    }

    public void populateCustomers(int count) throws IOException {
        for (int i = 0; i < count; i++) {
            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();

            CreateCustomerInput createCustomerInput = new CreateCustomerInput();
            createCustomerInput.setFirstName(firstName);
            createCustomerInput.setLastName(lastName);
            createCustomerInput.setEmailAddress(faker.internet().emailAddress());
            createCustomerInput.setPhoneNumber(faker.phoneNumber().phoneNumber());

            JsonNode inputNode = mapper.valueToTree(createCustomerInput);
            ObjectNode variables = mapper.createObjectNode();
            variables.set("input", inputNode);
            variables.put("password", TEST_PASSWORD);

            GraphQLResponse graphQLResponse = this.adminClient.perform(CREATE_CUSTOMER, variables);
            Customer customer = graphQLResponse.get("$.data.createCustomer", Customer.class);

            if (customer == null) continue;

            CreateAddressInput createAddressInput = new CreateAddressInput();
            createAddressInput.setFullName(firstName + " " + lastName);
            createAddressInput.setStreetLine1(faker.address().streetAddress());
            createAddressInput.setCity(faker.address().city());
            createAddressInput.setProvince(faker.address().state());
            createAddressInput.setPostalCode(faker.address().zipCode());

            inputNode = mapper.valueToTree(createAddressInput);
            variables = mapper.createObjectNode();
            variables.set("input", inputNode);
            variables.put("customerId", customer.getId());

            this.adminClient.perform(CREATE_CUSTOMER_ADDRESS, variables);
        }
        log.info("Created " + count + " Customers");
    }
}
