package com.cs.core.config.interactor.entity.causeeffectrule;

import com.cs.core.config.interactor.entity.action.Action;
import com.cs.core.config.interactor.entity.action.IAction;
import com.cs.core.config.interactor.entity.condition.Condition;
import com.cs.core.config.interactor.entity.condition.ICondition;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class CauseEffectRules implements ICauseEffectRules {
  
  protected String           id;
  protected Long             versionId;
  protected Long             versionTimestamp;
  protected String           lastModifiedBy;
  protected String           label;
  protected String           event;
  protected List<ICondition> condition;
  protected List<IAction>    actions;
  
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
  public Long getVersionId()
  {
    return versionId;
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    this.versionId = versionId;
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    return versionTimestamp;
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    this.versionTimestamp = versionTimestamp;
  }
  
  @Override
  public String getLastModifiedBy()
  {
    return lastModifiedBy;
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    this.lastModifiedBy = lastModifiedBy;
  }
  
  @Override
  public String getLabel()
  {
    return label;
  }
  
  @Override
  public void setLabel(String label)
  {
    this.label = label;
  }
  
  @Override
  public String getEvent()
  {
    return event;
  }
  
  @Override
  public void setEvent(String event)
  {
    this.event = event;
  }
  
  @Override
  public List<ICondition> getConditions()
  {
    if (condition == null) {
      condition = new ArrayList<>();
    }
    return condition;
  }
  
  @JsonDeserialize(contentAs = Condition.class)
  @Override
  public void setConditions(List<ICondition> condition)
  {
    this.condition = condition;
  }
  
  @Override
  public List<IAction> getActions()
  {
    if (actions == null) {
      actions = new ArrayList<>();
    }
    return actions;
  }
  
  @JsonDeserialize(contentAs = Action.class)
  @Override
  public void setActions(List<IAction> actions)
  {
    this.actions = actions;
  }
}
