package com.cs.core.config.interactor.model.articleimportcomponent;

import com.cs.core.config.interactor.model.component.ProcessInstanceEntity;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class SaveProcessInstanceModel extends ProcessInstanceEntity
    implements ISaveProcessInstanceModel {
  
  protected List<IComponentInstanceModel> modifiedComponents = new ArrayList<>();
  protected List<IComponentInstanceModel> addedComponents    = new ArrayList<>();
  
  @Override
  public List<IComponentInstanceModel> getModifiedComponents()
  {
    return modifiedComponents;
  }
  
  @Override
  @JsonDeserialize(contentAs = ComponentInstanceModel.class)
  public void setModifiedComponents(List<IComponentInstanceModel> modifiedComponents)
  {
    this.modifiedComponents = modifiedComponents;
  }
  
  @Override
  public List<IComponentInstanceModel> getAddedComponents()
  {
    return addedComponents;
  }
  
  @Override
  @JsonDeserialize(contentAs = ComponentInstanceModel.class)
  public void setAddedComponents(List<IComponentInstanceModel> addedComponents)
  {
    this.addedComponents = addedComponents;
  }
}
