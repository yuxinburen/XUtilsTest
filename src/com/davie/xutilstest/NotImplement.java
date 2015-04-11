package com.davie.xutilstest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * User: davie
 * Date: 15-4-9
 */
@Target({ElementType.METHOD,ElementType.FIELD})//代表注解能够用到方法上
public @interface NotImplement {
    //所有的注解当中的变量或者数值都要采用方法的形式来进行指定
    //所有的value名字的内容,可以直接使用数值的形式,不需要完全写出(value = xxx),可以略写为:(xxx)
    int value() default 4;

}
