package in.house.financial.utils;

import in.house.financial.RequestDTO.UploadProgress;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class ServerSentEventUtil {

    public Map<Long, SseEmitter> progressEmitters = new ConcurrentHashMap<>();

    public void sendProgressUpdate(String message, int progress, long uniqueEmitterId) {
        SseEmitter emitter = progressEmitters.get(uniqueEmitterId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().data(new UploadProgress(message, progress)));
            } catch (IllegalStateException e) {
                // Handle the case where the emitter is already completed
                log.warn("Emitter already completed for ID: {}", uniqueEmitterId);
                progressEmitters.remove(uniqueEmitterId);
            } catch (IOException e) {
                // Handle other IO exceptions
                log.error("Error sending progress update for ID: {}", uniqueEmitterId, e);
                emitter.completeWithError(e);
                progressEmitters.remove(uniqueEmitterId);
            }
        } else {
            log.warn("No active emitter found for ID: {}", uniqueEmitterId);
        }
    }

    public SseEmitter getProgressEmitter(long uniqueEmitterId) {
        return progressEmitters.get(uniqueEmitterId);
    }


}
