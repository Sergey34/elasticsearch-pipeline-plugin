package com.seko0716.es.plugin.pipeline.services;

import com.seko0716.es.plugin.pipeline.exception.QuartzInterruptException;
import com.seko0716.es.plugin.pipeline.exception.QuartzSchedulerException;
import org.elasticsearch.SpecialPermission;
import org.elasticsearch.common.component.AbstractLifecycleComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.quartz.Calendar;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.ListenerManager;
import org.quartz.Scheduler;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SchedulerMetaData;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.UnableToInterruptJobException;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;

import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;

import static org.quartz.impl.StdSchedulerFactory.PROP_SCHED_INSTANCE_NAME;

public class ScheduleService extends AbstractLifecycleComponent {
    private final Scheduler scheduler;
    private final String schedulerName;
    private boolean isPause = false;

    @Inject
    public ScheduleService(final Settings settings) {
        super(settings);
        logger.info("Creating Scheduler...");

        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            // unprivileged code such as scripts do not have SpecialPermission
            sm.checkPermission(new SpecialPermission());
        }
        scheduler = AccessController.doPrivileged((PrivilegedAction<Scheduler>) () -> {
            try {
                Properties properties = new Properties();
                properties.put("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");
                properties.put("org.quartz.threadPool.threadCount", "10");
                properties.put("org.quartz.threadPool.threadPriority", "5");
                properties.put(PROP_SCHED_INSTANCE_NAME, UUID.randomUUID().toString());
                properties.put("org.quartz.threadPool.threadsInheritContextClassLoaderOfInitializingThread", "true");
                final SchedulerFactory sf = new StdSchedulerFactory(properties);// TODO: 03.09.2019 rewrite to configuration file
                return sf.getScheduler();
            } catch (final SchedulerException e) {
                throw new QuartzSchedulerException("Failed to create Scheduler.", e);
            }
        });
        try {
            schedulerName = scheduler.getSchedulerName();
        } catch (SchedulerException e) {
            throw new QuartzSchedulerException("Failed to set scheduler name.", e);
        }
    }


    @Override
    protected void doStart() {
        logger.info("Starting Scheduler...");

        synchronized (scheduler) {
            try {
                if (isPause) {
                    scheduler.resumeAll();
                } else {
                    scheduler.start();
                }
            } catch (final SchedulerException e) {
                throw new QuartzSchedulerException("Failed to start Scheduler.", e);
            }
        }
    }

    @Override
    protected void doStop() {
        logger.info("Stopping Scheduler...");
        synchronized (scheduler) {
            try {
                scheduler.pauseAll();
                isPause = true;
            } catch (final SchedulerException e) {
                throw new QuartzSchedulerException("Failed to stop Scheduler.", e);
            }
        }
    }

    @Override
    protected void doClose() throws IOException {
        logger.info("Closing Scheduler...");

        try {
            scheduler.shutdown(true);
        } catch (final SchedulerException e) {
            throw new QuartzSchedulerException("Failed to shutdown Scheduler.", e);
        }
    }

    public String getSchedulerName() {
        try {
            return scheduler.getSchedulerName();
        } catch (final SchedulerException e) {
            throw new QuartzSchedulerException(e);
        }
    }

    public String getSchedulerInstanceId() {
        try {
            return scheduler.getSchedulerInstanceId();
        } catch (final SchedulerException e) {
            throw new QuartzSchedulerException(e);
        }
    }

    public SchedulerContext getContext() {
        try {
            return scheduler.getContext();
        } catch (final SchedulerException e) {
            throw new QuartzSchedulerException(e);
        }
    }

    public void standby() {
        try {
            scheduler.standby();
        } catch (final SchedulerException e) {
            throw new QuartzSchedulerException(e);
        }
    }

    public SchedulerMetaData getMetaData() {
        try {
            return scheduler.getMetaData();
        } catch (final SchedulerException e) {
            throw new QuartzSchedulerException(e);
        }
    }

    public List<JobExecutionContext> getCurrentlyExecutingJobs() {
        try {
            return scheduler.getCurrentlyExecutingJobs();
        } catch (final SchedulerException e) {
            throw new QuartzSchedulerException(e);
        }
    }

    public ListenerManager getListenerManager() {
        try {
            return scheduler.getListenerManager();
        } catch (final SchedulerException e) {
            throw new QuartzSchedulerException(e);
        }
    }

    public Date scheduleJob(final JobDetail jobDetail, final Trigger trigger) {
        try {
            return scheduler.scheduleJob(jobDetail, trigger);
        } catch (final SchedulerException e) {
            throw new QuartzSchedulerException(e);
        }
    }

    public Date scheduleJob(final Trigger trigger) {
        try {
            return scheduler.scheduleJob(trigger);
        } catch (final SchedulerException e) {
            throw new QuartzSchedulerException(e);
        }
    }

    public void scheduleJobs(
            final Map<JobDetail, Set<? extends Trigger>> triggersAndJobs,
            final boolean replace) {
        try {
            scheduler.scheduleJobs(triggersAndJobs, replace);
        } catch (final SchedulerException e) {
            throw new QuartzSchedulerException(e);
        }
    }

    public void scheduleJob(final JobDetail jobDetail,
                            final Set<? extends Trigger> triggersForJob, final boolean replace) {
        try {
            scheduler.scheduleJob(jobDetail, triggersForJob, replace);
        } catch (final SchedulerException e) {
            throw new QuartzSchedulerException(e);
        }
    }

    public boolean unscheduleJob(final TriggerKey triggerKey) {
        try {
            return scheduler.unscheduleJob(triggerKey);
        } catch (final SchedulerException e) {
            throw new QuartzSchedulerException(e);
        }
    }

    public boolean unscheduleJobs(final List<TriggerKey> triggerKeys) {
        try {
            return scheduler.unscheduleJobs(triggerKeys);
        } catch (final SchedulerException e) {
            throw new QuartzSchedulerException(e);
        }
    }

    public Date rescheduleJob(final TriggerKey triggerKey, final Trigger newTrigger) {
        try {
            return scheduler.rescheduleJob(triggerKey, newTrigger);
        } catch (final SchedulerException e) {
            throw new QuartzSchedulerException(e);
        }
    }

    public void addOrReplaceJob(final JobDetail jobDetail) {
        try {
            scheduler.addJob(jobDetail, true);
        } catch (final SchedulerException e) {
            throw new QuartzSchedulerException(e);
        }
    }

    public void addJob(final JobDetail jobDetail, final boolean replace) {
        try {
            scheduler.addJob(jobDetail, replace);
        } catch (final SchedulerException e) {
            throw new QuartzSchedulerException(e);
        }
    }

    public void addJob(final JobDetail jobDetail, final boolean replace,
                       final boolean storeNonDurableWhileAwaitingScheduling) {
        try {
            scheduler.addJob(jobDetail, replace,
                    storeNonDurableWhileAwaitingScheduling);
        } catch (final SchedulerException e) {
            throw new QuartzSchedulerException(e);
        }
    }

    public boolean deleteJob(final JobKey jobKey) {
        try {
            return scheduler.deleteJob(jobKey);
        } catch (final SchedulerException e) {
            throw new QuartzSchedulerException(e);
        }
    }

    public boolean deleteJobs(final List<JobKey> jobKeys) {
        try {
            return scheduler.deleteJobs(jobKeys);
        } catch (final SchedulerException e) {
            throw new QuartzSchedulerException(e);
        }
    }

    public void triggerJob(final JobKey jobKey) {
        try {
            scheduler.triggerJob(jobKey);
        } catch (final SchedulerException e) {
            throw new QuartzSchedulerException(e);
        }
    }

    public void triggerJob(final JobKey jobKey, final JobDataMap data) {
        try {
            scheduler.triggerJob(jobKey, data);
        } catch (final SchedulerException e) {
            throw new QuartzSchedulerException(e);
        }
    }

    public void pauseJob(final JobKey jobKey) {
        try {
            scheduler.pauseJob(jobKey);
        } catch (final SchedulerException e) {
            throw new QuartzSchedulerException(e);
        }
    }

    public void pauseJobs(final GroupMatcher<JobKey> matcher) {
        try {
            scheduler.pauseJobs(matcher);
        } catch (final SchedulerException e) {
            throw new QuartzSchedulerException(e);
        }
    }

    public void pauseTrigger(final TriggerKey triggerKey) {
        try {
            scheduler.pauseTrigger(triggerKey);
        } catch (final SchedulerException e) {
            throw new QuartzSchedulerException(e);
        }
    }

    public void pauseTriggers(final GroupMatcher<TriggerKey> matcher) {
        try {
            scheduler.pauseTriggers(matcher);
        } catch (final SchedulerException e) {
            throw new QuartzSchedulerException(e);
        }
    }

    public void resumeJob(final JobKey jobKey) {
        try {
            scheduler.resumeJob(jobKey);
        } catch (final SchedulerException e) {
            throw new QuartzSchedulerException(e);
        }
    }

    public void resumeJobs(final GroupMatcher<JobKey> matcher) {
        try {
            scheduler.resumeJobs(matcher);
        } catch (final SchedulerException e) {
            throw new QuartzSchedulerException(e);
        }
    }

    public void resumeTrigger(final TriggerKey triggerKey) {
        try {
            scheduler.resumeTrigger(triggerKey);
        } catch (final SchedulerException e) {
            throw new QuartzSchedulerException(e);
        }
    }

    public void resumeTriggers(final GroupMatcher<TriggerKey> matcher) {
        try {
            scheduler.resumeTriggers(matcher);
        } catch (final SchedulerException e) {
            throw new QuartzSchedulerException(e);
        }
    }

    public void pauseAll() {
        try {
            scheduler.pauseAll();
        } catch (final SchedulerException e) {
            throw new QuartzSchedulerException(e);
        }
    }

    public void resumeAll() {
        try {
            scheduler.resumeAll();
        } catch (final SchedulerException e) {
            throw new QuartzSchedulerException(e);
        }
    }

    public List<String> getJobGroupNames() {
        try {
            return scheduler.getJobGroupNames();
        } catch (final SchedulerException e) {
            throw new QuartzSchedulerException(e);
        }
    }

    public Set<JobKey> getJobKeys(final GroupMatcher<JobKey> matcher) {
        try {
            return scheduler.getJobKeys(matcher);
        } catch (final SchedulerException e) {
            throw new QuartzSchedulerException(e);
        }
    }

    public List<? extends Trigger> getTriggersOfJob(final JobKey jobKey) {
        try {
            return scheduler.getTriggersOfJob(jobKey);
        } catch (final SchedulerException e) {
            throw new QuartzSchedulerException(e);
        }
    }

    public List<String> getTriggerGroupNames() {
        try {
            return scheduler.getTriggerGroupNames();
        } catch (final SchedulerException e) {
            throw new QuartzSchedulerException(e);
        }
    }

    public Set<TriggerKey> getTriggerKeys(final GroupMatcher<TriggerKey> matcher) {
        try {
            return scheduler.getTriggerKeys(matcher);
        } catch (final SchedulerException e) {
            throw new QuartzSchedulerException(e);
        }
    }

    public Set<String> getPausedTriggerGroups() {
        try {
            return scheduler.getPausedTriggerGroups();
        } catch (final SchedulerException e) {
            throw new QuartzSchedulerException(e);
        }
    }

    public JobDetail getJobDetail(final JobKey jobKey) {
        try {
            return scheduler.getJobDetail(jobKey);
        } catch (final SchedulerException e) {
            throw new QuartzSchedulerException(e);
        }
    }

    public Trigger getTrigger(final TriggerKey triggerKey) {
        try {
            return scheduler.getTrigger(triggerKey);
        } catch (final SchedulerException e) {
            throw new QuartzSchedulerException(e);
        }
    }

    public Trigger.TriggerState getTriggerState(final TriggerKey triggerKey) {
        try {
            return scheduler.getTriggerState(triggerKey);
        } catch (final SchedulerException e) {
            throw new QuartzSchedulerException(e);
        }
    }

    public void addCalendar(final String calName, final Calendar calendar,
                            final boolean replace, final boolean updateTriggers) {
        try {
            scheduler.addCalendar(calName, calendar, replace, updateTriggers);
        } catch (final SchedulerException e) {
            throw new QuartzSchedulerException(e);
        }
    }

    public boolean deleteCalendar(final String calName) {
        try {
            return scheduler.deleteCalendar(calName);
        } catch (final SchedulerException e) {
            throw new QuartzSchedulerException(e);
        }
    }

    public Calendar getCalendar(final String calName) {
        try {
            return scheduler.getCalendar(calName);
        } catch (final SchedulerException e) {
            throw new QuartzSchedulerException(e);
        }
    }

    public List<String> getCalendarNames() {
        try {
            return scheduler.getCalendarNames();
        } catch (final SchedulerException e) {
            throw new QuartzSchedulerException(e);
        }
    }

    public boolean interrupt(final JobKey jobKey) {
        try {
            return scheduler.interrupt(jobKey);
        } catch (final UnableToInterruptJobException e) {
            throw new QuartzInterruptException("Failed to interrupt the job.", e);
        }
    }

    public boolean interrupt(final String fireInstanceId) {
        try {
            return scheduler.interrupt(fireInstanceId);
        } catch (final UnableToInterruptJobException e) {
            throw new QuartzInterruptException("Failed to interrupt the job.", e);
        }
    }

    public boolean checkExists(final JobKey jobKey) {
        try {
            return scheduler.checkExists(jobKey);
        } catch (final SchedulerException e) {
            throw new QuartzSchedulerException(e);
        }
    }

    public boolean checkExists(final TriggerKey triggerKey) {
        try {
            return scheduler.checkExists(triggerKey);
        } catch (final SchedulerException e) {
            throw new QuartzSchedulerException(e);
        }
    }

    public void clear() {
        try {
            scheduler.clear();
        } catch (final SchedulerException e) {
            throw new QuartzSchedulerException(e);
        }
    }
}
