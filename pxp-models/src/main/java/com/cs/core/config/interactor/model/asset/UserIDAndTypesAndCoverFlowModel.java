package com.cs.core.config.interactor.model.asset;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.interactor.model.assetinstance.ITypesAndCoverFlowModel;
import com.cs.core.runtime.interactor.model.assetinstance.IUserIDAndTypesAndCoverFlowModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class UserIDAndTypesAndCoverFlowModel implements IUserIDAndTypesAndCoverFlowModel {
  
  private static final long                     serialVersionUID = 1L;
  protected String                              userId;
  protected IListModel<ITypesAndCoverFlowModel> typesAndCoverFlowModelList;
  
  @Override
  public String getUserId()
  {
    return userId;
  }
  
  @Override
  public void setUserId(String userId)
  {
    this.userId = userId;
  }
  
  @Override
  @JsonDeserialize(contentAs = TypesAndCoverFlowModel.class)
  public IListModel<ITypesAndCoverFlowModel> getTypesAndCoverFlowModelList()
  {
    return typesAndCoverFlowModelList;
  }
  
  @Override
  public void setTypesAndCoverFlowModelList(
      IListModel<ITypesAndCoverFlowModel> typesAndCoverFlowModelList)
  {
    this.typesAndCoverFlowModelList = typesAndCoverFlowModelList;
  }
}
