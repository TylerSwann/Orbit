package io.orbit.api;

import java.lang.annotation.*;

/**
 * Created by Tyler Swann on Thursday March 01, 2018 at 14:42
 *
 * Methods, Fields, or Parameters are marked with nullable if the return value from a method,
 * argument passed into a method call, or field, is potentially null. This annotation indicates that
 * the person who wrote the method or class has done what is necessary to prevent a NullPointerReference exception
 * from occurring.
 *
 */
@Documented
@Retention(RetentionPolicy.CLASS)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
public @interface Nullable { }
