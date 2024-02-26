package in.vakrangee.core.utils.network;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Vasundhara on 24/8/2018.
 */

public class EventData {
    private String key;
    private String data;

    public EventData(String data) {
        this.data = data;
    }

    public EventData(@NotNull String key, @NotNull String data) {
        this.key = key;
        this.data = data;
    }

    public String getKey() {
        return key;
    }

    public String getData() {
        return data;
    }
}
