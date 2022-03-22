package com.cs.core.config.interactor.model.component;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.datarule.IDataRuleModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface ITransferDataStrategyRequestModel extends IModel, IApplyDataRulesRequestModel {
  
  public static final String SOURCE_INDEX_NAME       = "sourceIndexName";
  public static final String TARGET_INDEX_NAME       = "targetIndexName";
  public static final String COMPONENT_ID            = "componentId";
  public static final String POST_PROCESS_DATA_RULES = "postProcessDataRules";
  public static final String ID                      = "id";
  
  public IListModel<IDataRuleModel> getPostProcessDataRules();
  
  public void setPostProcessDataRules(IListModel<IDataRuleModel> postProcessDataRules);
  
  public String getSourceIndexName();
  
  public void setSourceIndexName(String sourceIndexName);
  
  public String getTargetIndexName();
  
  public void setTargetIndexName(String targetIndexName);
  
  public String getComponentId();
  
  public void setComponentId(String componentId);
  
  public String getId();
  
  public void setId(String id);
}
