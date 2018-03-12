package org.apache.aries.jax.rs.example.sse;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
public class Stats {
    private long timestamp;
    private int load;

    public Stats() {
    }

    public Stats(long timestamp, int load) {
        this.timestamp = timestamp;
        this.load = load;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getLoad() {
        return load;
    }

    public void setLoad(int load) {
        this.load = load;
    }
}
