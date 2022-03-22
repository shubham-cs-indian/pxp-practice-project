package com.cs.core.runtime.interactor.model.context;

import com.cs.core.runtime.interactor.entity.idandtype.IIdAndBaseType;
import com.cs.core.runtime.interactor.entity.idandtype.IdAndBaseType;
import com.cs.core.runtime.interactor.entity.timerange.IInstanceTimeRange;
import com.cs.core.runtime.interactor.entity.timerange.InstanceTimeRange;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class ModifiedContextInstanceModel implements IModifiedContextInstanceModel {
  
  private static final long      serialVersionUID       = 1L;
  protected String               id;
  protected String               contextId;
  protected IInstanceTimeRange   timeRange;
  protected List<IIdAndBaseType> addedLinkedInstances   = new ArrayList<>();
  protected List<String>         deletedLinkedInstances = new ArrayList<>();
  
  @Override
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public String getContextId()
  {
    return contextId;
  }
  
  @Override
  public void setContextId(String contextId)
  {
    this.contextId = contextId;
  }
  
  @Override
  public IInstanceTimeRange getTimeRange()
  {
    return timeRange;
  }
  
  @JsonDeserialize(as = InstanceTimeRange.class)
  @Override
  public void setTimeRange(IInstanceTimeRange timeRange)
  {
    this.timeRange = timeRange;
  }
  
  @Override
  public List<IIdAndBaseType> getAddedLinkedInstances()
  {
    return addedLinkedInstances;
  }
  
  @Override
  @JsonDeserialize(contentAs = IdAndBaseType.class)
  public void setAddedLinkedInstances(List<IIdAndBaseType> addedlinkedInstances)
  {
    this.addedLinkedInstances = addedlinkedInstances;
  }
  
  @Override
  public List<String> getDeletedLinkedInstances()
  {
    return deletedLinkedInstances;
  }
  
  @Override
  public void setDeletedLinkedInstances(List<String> deletedlinkedInstances)
  {
    this.deletedLinkedInstances = deletedlinkedInstances;
  }
}
