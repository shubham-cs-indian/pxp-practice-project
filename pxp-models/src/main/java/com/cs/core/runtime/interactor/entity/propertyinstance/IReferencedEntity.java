package com.cs.core.runtime.interactor.entity.propertyinstance;

import com.cs.core.config.interactor.entity.datarule.ILinkedEntities;
import com.cs.core.runtime.interactor.entity.configuration.base.ITimelineInstance;
import java.util.List;

/**
 * This interface is extended by IEventInstance & ITaskInstance
 *
 * @author Kshitij
 */
public interface IReferencedEntity extends ITimelineInstance {
  
  public static final String LINKED_ENTITIES = "linkedEntities";
  public static final String JOB_ID          = "jobId";
  public static final String CREATED_BY      = "createdBy";
  public static final String CREATED_ON      = "createdOn";
  
  public void setLinkedEntities(List<ILinkedEntities> LinkedEntities);
  
  public List<ILinkedEntities> getLinkedEntities();
  
  public String getJobId();
  
  public void setJobId(String jobId);
}
