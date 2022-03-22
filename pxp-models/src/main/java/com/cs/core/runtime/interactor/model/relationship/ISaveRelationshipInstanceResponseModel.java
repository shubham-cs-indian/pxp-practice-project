package com.cs.core.runtime.interactor.model.relationship;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface ISaveRelationshipInstanceResponseModel extends IModel {
  
  public static final String DATA_TRANSFER_INFO = "dataTransferInfo";
  
  public List<IRelationshipDataTransferInfoModel> getDataTransferInfo();
  
  public void setDataTransferInfo(List<IRelationshipDataTransferInfoModel> dataTransferInfo);
}
