package eu.agricore.indexer.security;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class BucketService {

    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    @Value("${rate.capacity}")
    private Integer bucketCapacity;
    @Value("${rate.timePeriod}")
    private Integer timePeriod;


    public Bucket resolveBucket(String apiKey) {
        return cache.computeIfAbsent(apiKey, this::newBucket);
    }

    private Bucket newBucket(String apiKey) {
        Bandwidth bandwidth = Bandwidth.classic(bucketCapacity, Refill.intervally(bucketCapacity, Duration.ofMillis(timePeriod)));
        return bucket(bandwidth);
    }

    private Bucket bucket(Bandwidth limit) {
        return Bucket4j.builder()
                .addLimit(limit)
                .build();
    }

}
