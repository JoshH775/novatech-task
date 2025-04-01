package com.example.models;

import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Order {

    private String order_number;
    private DeliverTo deliver_to;
    private Person person;
    private final List<ObjectNode> order_lines;

    public Order(String order_number, DeliverTo deliver_to, Person person) {
        this.order_number = order_number;
        this.deliver_to = deliver_to;
        this.order_lines = createOrderLines(); 
        this.person = person;
    }

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public DeliverTo getDeliver_to() {
        return deliver_to;
    }

    public void setDeliver_to(DeliverTo deliver_to) {
        this.deliver_to = deliver_to;
    }

    public List<ObjectNode> getOrder_lines() { 
        return order_lines;
    }

    //Order Lines remain static
    private static List<ObjectNode> createOrderLines() { 
        ObjectMapper objectMapper = new ObjectMapper();

        ObjectNode orderLine1 = objectMapper.createObjectNode();
        ObjectNode orderLine2 = objectMapper.createObjectNode();

        orderLine1.put("sku", "TEST-SKU01");
        orderLine1.put("quantity", 1);

        orderLine2.put("sku", "TEST-SKU02");
        orderLine2.put("quantity", 2);

        return List.of(orderLine1, orderLine2);
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
