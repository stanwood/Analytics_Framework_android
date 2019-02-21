package io.stanwood.framework.analytics.generic;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

public class TrackerParams {
    private final String eventName;
    private String itemId;
    private String name;
    private String category;
    private String contentType;
    private Map<String, Object> customProps;

    private TrackerParams(@NonNull Builder builder) {
        eventName = builder.eventName;
        itemId = builder.itemId;
        name = builder.name;
        category = builder.category;
        contentType = builder.contentType;
        customProps = builder.customProps;
    }

    public static Builder builder(String eventName) {
        return new Builder(eventName);
    }

    public String getEventName() {
        return eventName;
    }

    public String getItemId() {
        return itemId;
    }

    public Map<String, Object> getCustomPropertys() {
        return customProps;
    }

    public <T> T getCustomProperty(String key) {
        return customProps != null ? (T) customProps.get(key) : null;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getContentType() {
        return contentType;
    }

    public Builder newBuilder(String eventName){
        return new Builder(eventName)
                .setId(itemId)
                .setName(name)
                .setCategory(category)
                .setContentType(contentType)
                .addCustomProperty(customProps);
    }

    @Override
    public String toString() {
        return "TrackerParams{" +
                "eventName='" + eventName + '\'' +
                ", itemId='" + itemId + '\'' +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", contentType='" + contentType + '\'' +
                '}';
    }

    public static class Builder {
        private String eventName;
        private String itemId = null;
        private String name = null;
        private String category = null;
        private String contentType = null;
        private Map<String, Object> customProps;

        Builder(@NonNull String eventName) {
            this.eventName = eventName;
        }

        public Builder setId(String itemId) {
            this.itemId = itemId;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setCategory(String category) {
            this.category = category;
            return this;
        }

        public Builder setContentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        public Builder addCustomProperty(String key, Object value) {
            if (customProps == null) {
                customProps = new HashMap<>();
            }
            customProps.put(key, value);
            return this;
        }

        public Builder addCustomProperty(@Nullable Map<String, Object> value) {
            if (value != null && !value.isEmpty()) {
                if (customProps == null) {
                    customProps = new HashMap<>();
                }
                customProps.putAll(value);
            }
            return this;
        }

        public TrackerParams build() {
            return new TrackerParams(this);
        }
    }
}