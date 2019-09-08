package com.seko0716.es.plugin.pipeline.domains;

import com.seko0716.es.plugin.pipeline.exception.QuartzSchedulerException;
import com.seko0716.es.plugin.pipeline.services.ClientFactory;
import com.seko0716.es.plugin.pipeline.utils.MapUtils;
import org.elasticsearch.client.Client;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Map;

public abstract class AbstractJob implements Job {
    protected Client client;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        client = ClientFactory.INSTANCE.getEsClient();
        Map<String, Object> mergedJobDataMap = context.getMergedJobDataMap().getWrappedMap();
        Map<String, Object> config = MapUtils.getMap(mergedJobDataMap, "configuration");
        boolean isSyncJob = MapUtils.getBoolean(config, "sync_pipeline", false);
        Trigger trigger = context.getTrigger();
        TriggerKey key = trigger.getKey();

        String schedulerName = MapUtils.getString(mergedJobDataMap,"scheduler_name");
        Scheduler scheduler = AccessController.doPrivileged((PrivilegedAction<Scheduler>) () -> {
            try {
                final SchedulerFactory sf = new StdSchedulerFactory();
                return sf.getScheduler(schedulerName);
            } catch (final SchedulerException e) {
                throw new QuartzSchedulerException("Failed to create Scheduler.", e);
            }
        });
        if (scheduler != null) {
            try {
                if (isSyncJob) {
                    scheduler.pauseJob(context.getJobDetail().getKey());
                }
                scheduler.pauseTrigger(key);
                action(config);
                resume(context, isSyncJob, key, scheduler);
            } catch (SchedulerException e) {
                throw new QuartzSchedulerException("Can not pause job", e);
            } finally {
                resume(context, isSyncJob, key, scheduler);
            }
        }
    }

    private void resume(JobExecutionContext context, boolean isSyncJob, TriggerKey key, Scheduler scheduler) {
        try {
            if (isSyncJob) {
                scheduler.resumeJob(context.getJobDetail().getKey());
                scheduler.resumeTrigger(key);
            }
        } catch (SchedulerException e) {
            throw new QuartzSchedulerException("Can not resume job", e);
        }
    }

    protected abstract void action(Map<String, Object> config);
}
