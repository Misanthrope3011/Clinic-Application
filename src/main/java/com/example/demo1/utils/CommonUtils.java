package com.example.demo1.utils;

import lombok.experimental.UtilityClass;

import java.util.function.Consumer;

@UtilityClass
public class CommonUtils {

    <T> void setIfNotNull(T value, Consumer<T> setter) {
        if(value != null) {
            setter.accept(value);
        }
    }

}
