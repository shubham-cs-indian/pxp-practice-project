package com.cs.core.config.interactor.model.articleimportcomponent;

import com.cs.core.config.interactor.model.component.IProcessInstanceEntity;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface ISaveProcessInstanceModel extends IProcessInstanceEntity, IModel {
  
  public final String MODIFIED_COMPONENTS = "modifiedComponents";
  public final String ADDED_COMPONENTS    = "addedComponents";
  
  public List<IComponentInstanceModel> getModifiedComponents();
  
  public void setModifiedComponents(List<IComponentInstanceModel> modifiedComponents);
  
  public List<IComponentInstanceModel> getAddedComponents();
  
  public void setAddedComponents(List<IComponentInstanceModel> addedComponents);
}
