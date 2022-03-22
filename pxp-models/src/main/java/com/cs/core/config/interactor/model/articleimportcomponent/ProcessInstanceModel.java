package com.cs.core.config.interactor.model.articleimportcomponent;

import com.cs.core.config.interactor.model.component.ProcessInstanceEntity;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class ProcessInstanceModel extends ProcessInstanceEntity implements IProcessInstanceModel {
  
  private static final long               serialVersionUID = 1L;
  
  protected List<IComponentInstanceModel> model            = new ArrayList();
  
  @Override
  public List<IComponentInstanceModel> getComponents()
  {
    if (model == null) {
      model = new ArrayList();
    }
    return model;
  }
  
  @Override
  @JsonDeserialize(contentAs = ComponentInstanceModel.class)
  public void setComponents(List<IComponentInstanceModel> processId)
  {
    this.model = processId;
  }
}
