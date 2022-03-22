package com.cs.core.runtime.interactor.entity.eventinstance;

import com.cs.core.config.interactor.entity.datarule.ILinkedEntities;
import com.cs.core.runtime.interactor.entity.configuration.base.ITimelineInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstance;
import java.util.List;

public interface IEventInstance extends IKlassInstance, ITimelineInstance {
  
  public static final String LINKED_ENTITIES   = "linkedEntities";
  public static final String LONG_DESCRIPTION  = "longDescription";
  public static final String SHORT_DESCRIPTION = "shortDescription";
  
  public List<ILinkedEntities> getLinkedEntities();
  
  public void setLinkedEntities(List<ILinkedEntities> linkedEntities);
  
  public String getLongDescription();
  
  public void setLongDescription(String longDescription);
  
  public String getShortDescription();
  
  public void setShortDescription(String shortDescription);
}
