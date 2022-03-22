package com.cs.core.config.interactor.model.goldenrecord;

import com.cs.core.runtime.interactor.model.configdetails.IInheritDefaultValueResponseModel;
import com.cs.core.runtime.interactor.model.relationship.IRelationshipDataTransferInfoModel;

import java.util.List;

public interface IUpdateGoldenRecordResponseModel extends IInheritDefaultValueResponseModel {
  
  public static final String DATA_TRANSFER_INFO = "dataTransferInfo";
  
  public List<IRelationshipDataTransferInfoModel> getDataTransferInfo();
  
  public void setDataTransferInfo(List<IRelationshipDataTransferInfoModel> dataTransferInfo);
}
