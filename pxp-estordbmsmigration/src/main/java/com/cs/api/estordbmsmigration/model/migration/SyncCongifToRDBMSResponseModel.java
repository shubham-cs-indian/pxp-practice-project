package com.cs.api.estordbmsmigration.model.migration;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.config.interactor.model.datarule.IDataRuleModel;
import com.cs.core.config.interactor.model.tag.DataRuleModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class SyncCongifToRDBMSResponseModel implements ISyncCongifToRDBMSResponseModel {
  
  private static final long             serialVersionUID     = 1L;
  private List<ISyncCongifToRDBMSModel> list                 = new ArrayList<>();
  private Long                          count                = 0l;
  private List<IDataRuleModel>          dataRuleResponseList = new ArrayList<>();
  
  @Override
  @JsonDeserialize(contentAs = SyncCongifToRDBMSModel.class)
  public void setList(List<ISyncCongifToRDBMSModel> list)
  {
    this.list = list;
  }
  
  @Override
  public List<ISyncCongifToRDBMSModel> getList()
  {
    return list;
  }
  
  @Override
  public void setCount(Long count)
  {
    this.count = count;
  }
  
  @Override
  public Long getCount()
  {
    return count;
  }

  @Override
  @JsonDeserialize(contentAs = DataRuleModel.class)
  public void setDataRuleResponseList(List<IDataRuleModel> dataRuleResponseList)
  {
    this.dataRuleResponseList = dataRuleResponseList;
  }

  @Override
  public List<IDataRuleModel> getDataRuleResponseList()
  {
    return dataRuleResponseList;
  }
  
}
