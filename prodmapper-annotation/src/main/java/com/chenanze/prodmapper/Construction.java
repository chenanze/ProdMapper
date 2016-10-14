package com.chenanze.prodmapper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by duian on 2016/10/11.
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.CONSTRUCTOR)
public @interface Construction {
}
