package com.seko0716.es.plugin.pipeline.exception;

public class QuartzInterruptException extends QuartzSchedulerException {

    private static final long serialVersionUID = 1L;

    public QuartzInterruptException(final String msg, final Throwable cause) {
        super(msg, cause);
    }

}