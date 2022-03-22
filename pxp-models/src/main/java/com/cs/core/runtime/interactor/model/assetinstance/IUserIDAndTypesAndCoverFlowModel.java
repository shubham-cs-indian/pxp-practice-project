package com.cs.core.runtime.interactor.model.assetinstance;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IUserIDAndTypesAndCoverFlowModel extends IModel {
  
  public static final String USER_ID                        = "userId";
  public static final String TYPES_AND_COVERFLOW_MODEL_LIST = "typesAndCoverFlowModelList";
  
  public String getUserId();
  
  public void setUserId(String userId);
  
  public IListModel<ITypesAndCoverFlowModel> getTypesAndCoverFlowModelList();
  
  public void setTypesAndCoverFlowModelList(
      IListModel<ITypesAndCoverFlowModel> typesAndCoverFlowModelList);
}
