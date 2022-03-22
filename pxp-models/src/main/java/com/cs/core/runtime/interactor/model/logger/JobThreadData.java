package com.cs.core.runtime.interactor.model.logger;

import org.springframework.stereotype.Component;

@Component
public class JobThreadData {
  
  ThreadLocal<JobData> jobData = new ThreadLocal<JobData>();
  
  public JobData getJobData()
  {
    JobData data = jobData.get();
    if (data == null) {
      data = new JobData();
      jobData.set(data);
    }
    return data;
  }
  
  public void setJobData(JobData jobData)
  {
    this.jobData.set(jobData);
  }
  
  public void removeJobData()
  {
    this.jobData.remove();
  }
}
