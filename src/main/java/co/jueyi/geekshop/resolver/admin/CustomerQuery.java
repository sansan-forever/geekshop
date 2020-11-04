package co.jueyi.geekshop.resolver.admin;

import co.jueyi.geekshop.types.customer.Customer;
import co.jueyi.geekshop.types.customer.CustomerList;
import co.jueyi.geekshop.types.customer.CustomerListOptions;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.stereotype.Component;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Component
public class CustomerQuery implements GraphQLQueryResolver {
    /**
     * Query
     */
    public CustomerList customers(CustomerListOptions options) {
        return null; // TODO
    }

    public Customer customer(Long id) {
        return null; // TODO
    }
}
