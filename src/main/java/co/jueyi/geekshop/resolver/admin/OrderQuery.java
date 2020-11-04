package co.jueyi.geekshop.resolver.admin;

import co.jueyi.geekshop.types.order.Order;
import co.jueyi.geekshop.types.order.OrderList;
import co.jueyi.geekshop.types.order.OrderListOptions;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.stereotype.Component;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Component
public class OrderQuery implements GraphQLQueryResolver {
    /**
     * Query
     */
    public Order order(Long id) {
        return null; // TODO
    }

    public OrderList orders(OrderListOptions options) {
        return null; // TODO
    }
}
