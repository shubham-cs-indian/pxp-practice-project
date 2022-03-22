package com.cs.core.config.interactor.model.component;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.datarule.IDataRuleModel;
import com.cs.core.config.interactor.model.tag.DataRuleModel;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class TransferDataStrategyRequestModel extends ApplyDataRulesRequestModel
    implements ITransferDataStrategyRequestModel {
  
  private static final long            serialVersionUID     = 1L;
  protected String                     sourceIndexName;
  protected String                     targetIndexName;
  protected String                     componentId;
  protected IListModel<IDataRuleModel> postProcessDataRules = new ListModel<>();
  protected String                     id;
  
  @Override
  public IListModel<IDataRuleModel> getPostProcessDataRules()
  {
    if (postProcessDataRules == null) {
      postProcessDataRules = new ListModel<>();
    }
    return postProcessDataRules;
  }
  
  @Override
  @JsonDeserialize(contentAs = DataRuleModel.class)
  public void setPostProcessDataRules(IListModel<IDataRuleModel> postProcessDataRules)
  {
    this.postProcessDataRules = postProcessDataRules;
  }
  
  @Override
  public String getSourceIndexName()
  {
    return sourceIndexName;
  }
  
  @Override
  public void setSourceIndexName(String sourceIndexName)
  {
    this.sourceIndexName = sourceIndexName;
  }
  
  @Override
  public String getTargetIndexName()
  {
    return targetIndexName;
  }
  
  @Override
  public void setTargetIndexName(String targetIndexName)
  {
    this.targetIndexName = targetIndexName;
  }
  
  @Override
  public String getComponentId()
  {
    return componentId;
  }
  
  @Override
  public void setComponentId(String componentId)
  {
    this.componentId = componentId;
  }
  
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
}
