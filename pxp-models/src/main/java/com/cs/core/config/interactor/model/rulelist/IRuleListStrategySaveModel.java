package com.cs.core.config.interactor.model.rulelist;

import java.util.List;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;
import com.cs.core.config.interactor.model.configdetails.IConfigModel;
import com.cs.core.config.interactor.model.datarule.IDataRuleModel;

public interface IRuleListStrategySaveModel
    extends IConfigModel, IRuleList, IConfigResponseWithAuditLogModel {
  
  public static final String DATA_RULE_LIST        = "dataRuleList";
  public static final String IS_RULE_LIST_MODIFIED = "isRuleListModified";
  
  public List<String> getKlassIds();
  
  public void setKlassIds(List<String> klassIds);
  
  public List<IDataRuleModel> getDataRuleList();
  
  public void setDataRuleList(List<IDataRuleModel> dataRuleList);
  
  public Boolean getIsRuleListModififed();
  
  public void setIsRuleListModified(Boolean isRuleListModified);
  
}
