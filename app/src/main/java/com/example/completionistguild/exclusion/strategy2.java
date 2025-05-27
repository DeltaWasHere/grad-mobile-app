package com.example.completionistguild.exclusion;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

public class strategy2 implements ExclusionStrategy {
    @Override
    public boolean shouldSkipField(FieldAttributes f) {
        return f.getAnnotation(strategy2Exclude.class) != null;
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        return false;
    }
}
