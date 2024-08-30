package org.example.catch_line.notification.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class EmitterRepositoryTest {

    private EmitterRepository emitterRepository;

    @BeforeEach
    void setUp() {
        emitterRepository = new EmitterRepository();
    }

    @Test
    void testSave() {
        String id = "1_123456789";
        SseEmitter emitter = new SseEmitter();

        SseEmitter result = emitterRepository.save(id, emitter);

        assertNotNull(result);
        assertEquals(emitter, result);
        assertEquals(1, emitterRepository.emitters.size());
        assertTrue(emitterRepository.emitters.containsKey(id));
    }

    @Test
    void testSaveEventCache() {
        String id = "1_event";
        Object event = new Object();

        emitterRepository.saveEventCache(id, event);

        // SaveEventCache 이후 findAllEventCacheStartWithId로 확인
        Map<String, Object> cachedEvents = emitterRepository.findAllEventCacheStartWithId("1_event");
        assertEquals(1, cachedEvents.size());
        assertTrue(cachedEvents.containsKey(id));
        assertEquals(event, cachedEvents.get(id));
    }

    @Test
    void testFindAllStartWithById() {
        SseEmitter emitter1 = new SseEmitter();
        SseEmitter emitter2 = new SseEmitter();
        emitterRepository.save("1_123456789", emitter1);
        emitterRepository.save("1_987654321", emitter2);
        emitterRepository.save("2_123456789", new SseEmitter());

        Map<String, SseEmitter> results = emitterRepository.findAllStartWithById("1_");

        assertEquals(2, results.size());
        assertTrue(results.containsKey("1_123456789"));
        assertTrue(results.containsKey("1_987654321"));
    }

    @Test
    void testFindAllEventCacheStartWithId() {
        emitterRepository.saveEventCache("1_event1", new Object());
        emitterRepository.saveEventCache("1_event2", new Object());
        emitterRepository.saveEventCache("2_event1", new Object());

        Map<String, Object> results = emitterRepository.findAllEventCacheStartWithId("1_");

        assertEquals(2, results.size());
        assertTrue(results.containsKey("1_event1"));
        assertTrue(results.containsKey("1_event2"));
    }

    @Test
    void testDeleteAllStartWithId() {
        SseEmitter emitter1 = new SseEmitter();
        SseEmitter emitter2 = new SseEmitter();
        emitterRepository.save("1_123456789", emitter1);
        emitterRepository.save("1_987654321", emitter2);
        emitterRepository.save("2_123456789", new SseEmitter());

        emitterRepository.deleteAllStartWithId("1_");

        assertEquals(1, emitterRepository.emitters.size());
        assertFalse(emitterRepository.emitters.containsKey("1_123456789"));
        assertFalse(emitterRepository.emitters.containsKey("1_987654321"));
    }

    @Test
    void testDeleteById() {
        SseEmitter emitter = new SseEmitter();
        emitterRepository.save("1_123456789", emitter);

        emitterRepository.deleteById("1_123456789");

        assertEquals(0, emitterRepository.emitters.size());
        assertFalse(emitterRepository.emitters.containsKey("1_123456789"));
    }

    @Test
    void testDeleteAllEventCacheStartWithId() {
        emitterRepository.saveEventCache("1_event1", new Object());
        emitterRepository.saveEventCache("1_event2", new Object());
        emitterRepository.saveEventCache("2_event1", new Object());

        emitterRepository.deleteAllEventCacheStartWithId("1_");

        // deleteAllEventCacheStartWithId 이후 findAllEventCacheStartWithId로 확인
        Map<String, Object> remainingEvents = emitterRepository.findAllEventCacheStartWithId("1_");
        assertEquals(0, remainingEvents.size());

        // 확인: 2_event1은 삭제되지 않음
        Map<String, Object> otherEvents = emitterRepository.findAllEventCacheStartWithId("2_");
        assertEquals(1, otherEvents.size());
        assertTrue(otherEvents.containsKey("2_event1"));
    }
}