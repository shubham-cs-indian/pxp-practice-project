package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.runtime.interactor.model.instance.IContentInstanceSaveModel;

public interface IArticleInstanceSaveModel
    extends IContentInstanceSaveModel, IArticleInstanceModel {
  
  public static final String TRIGGER_EVENT = "triggerEvent";
  
  public boolean getTriggerEvent();
  
  public void setTriggerEvent(boolean triggerEvent);
}
