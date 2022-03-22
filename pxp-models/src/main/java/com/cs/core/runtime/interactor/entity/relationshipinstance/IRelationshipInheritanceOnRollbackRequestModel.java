package com.cs.core.runtime.interactor.entity.relationshipinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.relationship.IRelationshipDataTransferInfoModel;
import java.util.List;

public interface IRelationshipInheritanceOnRollbackRequestModel extends IModel {
  
  String DATA_TRANSFER_INFO = "dataTransferInfo";
  String KLASS_INSTANCE_ID  = "klassInstanceId";
  String BASE_TYPE          = "baseType";
  String TYPES              = "types";
  
  public List<IRelationshipDataTransferInfoModel> getDataTransferInfo();
  
  public void setDataTransferInfo(List<IRelationshipDataTransferInfoModel> dataTransferInfo);
  
  public String getKlassInstanceId();
  
  public void setKlassInstanceId(String klassInstanceId);
  
  public String getBaseType();
  
  public void setBaseType(String baseType);
  
  public List<String> getTypes();
  
  public void setTypes(List<String> types);
}
