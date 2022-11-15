package kiedam.chatapp.backend.event;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class SubscriptionService {
    private final Map<String, Set<String>> sessionQueues = new HashMap<>();

    public HashSet<String> getSubscribedQueues(String sessionId) {
        HashSet<String> queues = new HashSet<>();
        if (sessionQueues.containsKey(sessionId))
            queues.addAll(sessionQueues.get(sessionId));
        return queues;
    }

    public void addQueue(String sessionId, String queueName) {
        if (!sessionQueues.containsKey(sessionId))
            sessionQueues.put(sessionId, new HashSet<>());
        sessionQueues.get(sessionId).add(queueName);
    }

    public void removeQueue(String sessionId, String queueName) {
        if (sessionQueues.containsKey(sessionId))
            sessionQueues.get(sessionId).remove(queueName);
    }

    public void removeAllQueues(String sessionId) {
        if (sessionQueues.containsKey(sessionId) && sessionQueues.get(sessionId) != null)
            sessionQueues
                    .get(sessionId)
                    .forEach(queueName -> removeQueue(sessionId, queueName));
        sessionQueues.remove(sessionId);
    }
}
