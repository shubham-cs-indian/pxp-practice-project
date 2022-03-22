package com.cs.core.runtime.interactor.model.goldenrecord;

import com.cs.core.config.interactor.model.goldenrecord.IUpdateGoldenRecordResponseModel;
import com.cs.core.runtime.interactor.model.configdetails.InheritDefaultValueResponseModel;
import com.cs.core.runtime.interactor.model.relationship.IRelationshipDataTransferInfoModel;
import com.cs.core.runtime.interactor.model.relationship.RelationshipDataTransferInfoModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class UpdateGoldenRecordResponseModel extends InheritDefaultValueResponseModel
    implements IUpdateGoldenRecordResponseModel {
  
  private static final long                          serialVersionUID = 1L;
  protected List<IRelationshipDataTransferInfoModel> dataTransferInfo;
  
  @Override
  public List<IRelationshipDataTransferInfoModel> getDataTransferInfo()
  {
    return dataTransferInfo;
  }
  
  @JsonDeserialize(contentAs = RelationshipDataTransferInfoModel.class)
  @Override
  public void setDataTransferInfo(List<IRelationshipDataTransferInfoModel> dataTransferInfo)
  {
    this.dataTransferInfo = dataTransferInfo;
  }
}
