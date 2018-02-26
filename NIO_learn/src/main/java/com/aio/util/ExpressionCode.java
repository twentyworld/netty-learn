package com.aio.util;

/**
 * Created by teemper on 2018/2/26, 21:54.
 *
 * @auther Zed.
 * copy as you like, but with these words.
 * from win.
 */
public enum ExpressionCode {
    ADD("+"),
    SUBTRACT("-"),
    MUITIPLY("*"),
    DEVIDE("/");

    String expression;

    ExpressionCode(String expression) {
        this.expression = expression;
    }
}
