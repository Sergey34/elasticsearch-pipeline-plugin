package com.seko0716.es.plugin.pipeline.exception;

import org.elasticsearch.ElasticsearchException;
import org.quartz.SchedulerException;

public class QuartzSchedulerException extends ElasticsearchException {

    public QuartzSchedulerException(final String msg, final Throwable cause) {
        super(msg, cause);
    }

    public QuartzSchedulerException(final SchedulerException cause) {
        this("Scheduler has an exception on this process.", cause);
    }

}