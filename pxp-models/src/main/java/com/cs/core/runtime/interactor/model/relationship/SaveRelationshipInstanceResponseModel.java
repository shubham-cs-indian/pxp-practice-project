package com.cs.core.runtime.interactor.model.relationship;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class SaveRelationshipInstanceResponseModel
    implements ISaveRelationshipInstanceResponseModel {
  
  private static final long                          serialVersionUID = 1L;
  protected List<IRelationshipDataTransferInfoModel> dataTransferInfo;
  
  @Override
  public List<IRelationshipDataTransferInfoModel> getDataTransferInfo()
  {
    if(dataTransferInfo == null) {
      this.dataTransferInfo = new ArrayList<>();
    }
    return dataTransferInfo;
  }
  
  @JsonDeserialize(contentAs = RelationshipDataTransferInfoModel.class)
  @Override
  public void setDataTransferInfo(List<IRelationshipDataTransferInfoModel> dataTransferInfo)
  {
    this.dataTransferInfo = dataTransferInfo;
  }
}
