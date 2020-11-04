package co.jueyi.geekshop.resolver.admin;

import co.jueyi.geekshop.types.address.Address;
import co.jueyi.geekshop.types.common.CreateCustomerInput;
import co.jueyi.geekshop.types.common.DeletionResponse;
import co.jueyi.geekshop.types.common.UpdateAddressInput;
import co.jueyi.geekshop.types.customer.*;
import co.jueyi.geekshop.types.history.HistoryEntry;
import graphql.kickstart.tools.GraphQLMutationResolver;
import org.springframework.stereotype.Component;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Component
public class CustomerMutation implements GraphQLMutationResolver {
    /**
     * Mutation
     */

    /**
     * Create a new Customer. If a password is provided, a new User will also be created and linked to the Customer.
     */
    public Customer createCustomer(CreateCustomerInput input, String password) {
        return null; // TODO
    }

    /**
     * Update an existing Customer
     */
    public Customer updateCustomer(UpdateCustomerInput input) {
        return null; // TODO
    }

    /**
     * Delete a Customer
     */
    public DeletionResponse deleteCustomer(Long id) {
        return null; // TODO
    }

    /**
     * Create a new Address and associate it with the Customer specified by customerId
     */
    public Address createCustomerAddress(Long customerId, CreateCustomerInput input) {
        return null; // TODO
    }

    /**
     * Update an existing Address
     */
    public Address updateCustomerAddress(UpdateAddressInput input) {
        return null; // TODO
    }

    /**
     * Delete an existing Address
     */
    public Boolean deleteCustomerAddress(Long id) {
        return null; // TODO
    }

    public Customer addNoteToCustomer(AddNoteToCustomerInput input) {
        return null; // TODO
    }

    public HistoryEntry updateCustomerNode(UpdateCustomerNoteInput input) {
        return null; // TODO
    }

    public DeletionResponse deleteCustomerNote(Long id) {
        return null; // TODO
    }
}
