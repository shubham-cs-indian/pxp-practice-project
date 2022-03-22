package com.cs.core.config.interactor.model.rulelist;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class GetAllRuleListsResponseModel implements IGetAllRuleListsResponseModel {
  
  private static final long                 serialVersionUID = 1L;
  protected List<IRuleListInformationModel> list;
  protected Long                            count;
  
  @Override
  public List<IRuleListInformationModel> getList()
  {
    return list;
  }
  
  @Override
  @JsonDeserialize(contentAs = RuleListInformationModel.class)
  public void setList(List<IRuleListInformationModel> list)
  {
    this.list = list;
  }
  
  @Override
  public Long getCount()
  {
    return count;
  }
  
  @Override
  public void setCount(Long count)
  {
    this.count = count;
  }
}
