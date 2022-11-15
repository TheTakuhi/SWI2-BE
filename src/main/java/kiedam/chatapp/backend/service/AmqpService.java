package kiedam.chatapp.backend.service;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Properties;
import java.util.function.Supplier;

@Service
public class AmqpService {
    private final AmqpAdmin amqpAdmin;

    public AmqpService(AmqpAdmin amqpAdmin) {
        this.amqpAdmin = amqpAdmin;
    }

    public String declareQueue(String queueName) {
        amqpAdmin.declareQueue(
                new Queue(queueName, true, false, false)
        );
        return queueName;
    }

    public String declareExchange(Supplier<Exchange> supplier) {
        Exchange exchange = supplier.get();
        amqpAdmin.declareExchange(exchange);
        return exchange.getName();
    }

    public Integer qetQueueCount(String queueName) {
        Properties queueProperties = Objects.requireNonNull(amqpAdmin.getQueueProperties(queueName));
        return (Integer) queueProperties.get("QUEUE_MESSAGE_COUNT");
    }
}
