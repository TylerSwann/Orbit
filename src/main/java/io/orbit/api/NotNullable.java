package io.orbit.api;

import java.lang.annotation.*;

/**
 * Created by Tyler Swann on Friday March 09, 2018 at 15:31
 */
@Documented
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE})
public @interface NotNullable { }