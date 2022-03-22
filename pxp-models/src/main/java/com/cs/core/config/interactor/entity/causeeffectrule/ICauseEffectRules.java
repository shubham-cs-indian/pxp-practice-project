package com.cs.core.config.interactor.entity.causeeffectrule;

import com.cs.core.config.interactor.entity.action.IAction;
import com.cs.core.config.interactor.entity.condition.ICondition;
import com.cs.core.config.interactor.entity.configuration.base.IEntity;

import java.util.List;

public interface ICauseEffectRules extends IEntity {
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public String getEvent();
  
  public void setEvent(String event);
  
  public List<ICondition> getConditions();
  
  public void setConditions(List<ICondition> condition);
  
  public List<IAction> getActions();
  
  public void setActions(List<IAction> actions);
}
