package com.cs.core.config.interactor.model.articleimportcomponent;

import com.cs.core.config.interactor.model.component.IProcessInstanceEntity;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IProcessInstanceModel extends IProcessInstanceEntity, IModel {
  
  public List<IComponentInstanceModel> getComponents();
  
  public void setComponents(List<IComponentInstanceModel> processId);
}
