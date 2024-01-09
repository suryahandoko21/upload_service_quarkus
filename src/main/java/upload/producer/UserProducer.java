package upload.producer;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@ApplicationScoped
public class UserProducer {

    private final Emitter<String> emitterUserCreated;

    @Inject
    public UserProducer(@Channel("user-created") Emitter<String> emitterUserCreated) {
        this.emitterUserCreated = emitterUserCreated;
    }

    public void sendUserCreated(String message) {
        emitterUserCreated.send(message).toCompletableFuture().join();
    }
}
