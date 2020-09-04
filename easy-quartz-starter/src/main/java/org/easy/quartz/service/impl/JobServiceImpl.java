package org.easy.quartz.service.impl;



import lombok.extern.slf4j.Slf4j;
import org.easy.quartz.entity.QuartzJob;
import org.easy.quartz.service.IJobService;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class JobServiceImpl implements IJobService {

    private static final String PARAM_NAME = "paramName";
    private static final String PARAM_VALUE = "paramValue";
    private static final String SCHEDULER_INSTANCE_NAME = "schedulerInstanceName";

    @Value("${spring.quartz.properties.org.quartz.scheduler.instanceName}")
    //@Value("${org.quartz.scheduler.instanceName}")
    private String schedulerInstanceName;

    @Autowired
    private Scheduler scheduler;


    private void fillJobData(List<QuartzJob> jobList) {
        jobList.forEach(job -> {
            JobKey key = new JobKey(job.getJobName(), job.getJobGroup());
            try {
                JobDataMap jobDataMap = scheduler.getJobDetail(key).getJobDataMap();
                List<Map<String, Object>> jobDataParam = new ArrayList<>();
                jobDataMap.forEach((k, v) -> {
                    Map<String, Object> jobData = new LinkedHashMap<>(2);
                    jobData.put(PARAM_NAME, k);
                    jobData.put(PARAM_VALUE, v);
                    jobDataParam.add(jobData);
                });
                job.setJobDataParam(jobDataParam);
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
        });
    }


    @Override
    public QuartzJob saveJob(String jobName, String jobGroup, String description, Job cls, String cronExpression, String triggerName,JobDataMap jobDataMap) throws SchedulerException {

        //删除旧任务
        scheduler.deleteJob(new JobKey(jobName, jobGroup));

        //构建job信息
        JobDetail job = JobBuilder.newJob(cls.getClass()).withIdentity(jobName, jobGroup).withDescription(description). usingJobData(jobDataMap) .build();
         // 触发时间点
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression.trim());
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity(jobName, jobGroup).startNow().withSchedule(cronScheduleBuilder).build();
        //交由Scheduler安排触发
        scheduler.scheduleJob(job, trigger);
        QuartzJob quartzJob = new QuartzJob(jobName, jobGroup, description, cls.getClass().getName(), cronExpression, triggerName);
        putDataMap(job, quartzJob);
        log.info("{} New Job: {}" ,scheduler.getSchedulerName(), quartzJob);
        return quartzJob;

    }

    @Override
    public QuartzJob saveJob(QuartzJob quartz) throws SchedulerException, ClassNotFoundException, IllegalAccessException, InstantiationException {

        //如果是修改  展示旧的 任务
        if (quartz.getOldJobGroup() != null && !"".equals(quartz.getOldJobGroup())) {
            //删除旧任务
            scheduler.deleteJob(new JobKey(quartz.getOldJobName(), quartz.getOldJobGroup()));
        }

        //构建job信息
        Class cls = Class.forName(quartz.getJobClassName());
        cls.newInstance();
        JobDetail job = JobBuilder.newJob(cls).withIdentity(quartz.getJobName(),quartz.getJobGroup()).withDescription(quartz.getDescription()).build();
        putDataMap(job, quartz);

        // 触发时间点
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(quartz.getCronExpression().trim());
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity(quartz.getJobName(), quartz.getJobGroup()).startNow().withSchedule(cronScheduleBuilder).build();
        //交由Scheduler安排触发
        scheduler.scheduleJob(job, trigger);
        log.info("{} New Job: {}" ,scheduler.getSchedulerName(), quartz);
        return quartz;
    }

    @Override
    public JobKey triggerJob(String jobName, String jobGroup) throws SchedulerException {
        JobKey key = new JobKey(jobName, jobGroup);
        scheduler.triggerJob(key);
        log.info("{} Trigger Job: {}" ,scheduler.getSchedulerName(), key);
        return key;
    }

    @Override
    public JobKey pauseJob(String jobName, String jobGroup) throws SchedulerException {
        JobKey key = new JobKey(jobName, jobGroup);
        scheduler.pauseJob(key);
        log.info("{} Pause Job: {}" ,scheduler.getSchedulerName(), key);
        return key;
    }

    @Override
    public JobKey resumeJob(String jobName, String jobGroup) throws SchedulerException {
        JobKey key = new JobKey(jobName, jobGroup);
        scheduler.resumeJob(key);
        log.info("{} Resume Job: {}" ,scheduler.getSchedulerName(), key);
        return key;
    }

    @Override
    public JobKey removeJob(String jobName, String jobGroup) throws SchedulerException {
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
        // 停止触发器
        scheduler.pauseTrigger(triggerKey);
        // 移除触发器
        scheduler.unscheduleJob(triggerKey);
        // 删除任务
        JobKey key = new JobKey(jobName, jobGroup);
        scheduler.deleteJob(key);
        log.info("{} Remove Job: {}", scheduler.getSchedulerName(), key);
        return key;
    }

    @Override
    public List<JobKey> removeJob(String jobGroup) throws SchedulerException {
//        // 停止触发器
//        scheduler.pauseTriggers(GroupMatcher.groupEquals(jobGroup));
//        // 移除触发器
//        scheduler.unscheduleJobs(CollectionUtils.arrayToList(scheduler.getTriggerKeys(GroupMatcher.groupEquals(jobGroup)).toArray()));
        // 删除任务
        List<JobKey> jobKeyList = new ArrayList<JobKey>();
        jobKeyList.addAll(scheduler.getJobKeys(GroupMatcher.groupEquals(jobGroup)));
        if(jobKeyList!=null && jobKeyList.size()>0) {
            scheduler.deleteJobs(jobKeyList);
        }
        log.info("{} Remove Jobs: {}" ,scheduler.getSchedulerName(), jobKeyList);
        return jobKeyList;
    }

    private void putDataMap(JobDetail job, QuartzJob quartz) {
        // 将scheduler instanceName存入jobDataMap，方便业务job中进行数据库操作
        JobDataMap jobDataMap = job.getJobDataMap();
        jobDataMap.put(SCHEDULER_INSTANCE_NAME, schedulerInstanceName);

        List<Map<String, Object>> jobDataParam = quartz.getJobDataParam();
        if (jobDataParam == null || jobDataParam.isEmpty()) {
            return;
        }
        jobDataParam.forEach(jobData -> jobDataMap.put(String.valueOf(jobData.get(PARAM_NAME)), jobData.get(PARAM_VALUE)));
    }
}
