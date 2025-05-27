package com.example.completionistguild.exclusion;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

public class strategy3 implements ExclusionStrategy {
    @Override
    public boolean shouldSkipField(FieldAttributes f) {
        return f.getAnnotation(strategy3Exclude.class) != null;
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        return false;
    }
}
