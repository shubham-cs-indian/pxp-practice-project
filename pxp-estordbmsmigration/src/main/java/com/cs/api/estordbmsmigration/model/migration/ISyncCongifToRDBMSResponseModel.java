package com.cs.api.estordbmsmigration.model.migration;

import java.util.List;

import com.cs.core.config.interactor.model.datarule.IDataRuleModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface ISyncCongifToRDBMSResponseModel extends IModel {
  
  public void setList(List<ISyncCongifToRDBMSModel> list);
  
  public List<ISyncCongifToRDBMSModel> getList();
  
  public void setCount(Long count);
  
  public Long getCount();
  
  public void setDataRuleResponseList(List<IDataRuleModel> list);
  
  public List<IDataRuleModel> getDataRuleResponseList();
  
}
