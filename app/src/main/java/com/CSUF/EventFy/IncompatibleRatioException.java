package com.CSUF.EventFy;

/**
 * Created by hmmousa.im on 4/30/16.
 */
public class IncompatibleRatioException extends RuntimeException {

    private static final long serialVersionUID = 234608108593115395L;

    public IncompatibleRatioException() {
        super("Can't perform Ken Burns effect on rects with distinct aspect ratios!");
    }
}