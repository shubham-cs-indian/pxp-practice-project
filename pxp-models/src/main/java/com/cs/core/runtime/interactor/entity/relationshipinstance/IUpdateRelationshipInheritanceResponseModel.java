package com.cs.core.runtime.interactor.entity.relationshipinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.relationship.IRelationshipDataTransferInfoModel;
import java.util.List;

public interface IUpdateRelationshipInheritanceResponseModel extends IModel {
  
  public static final String DATA_TRANSFER_INFO            = "dataTransferInfo";
  public static final String RELATIONSHIP_INHERITANCE_INFO = "relationshipInheritanceInfo";
  
  public List<IRelationshipDataTransferInfoModel> getDataTransferInfo();
  
  public void setDataTransferInfo(List<IRelationshipDataTransferInfoModel> dataTransferInfo);
  
  public List<IRelationshipDataTransferInfoModel> getRelationshipInheritanceInfo();
  
  public void setRelationshipInheritanceInfo(
      List<IRelationshipDataTransferInfoModel> relationshipInheritanceInfo);
}
