package com.cs.core.runtime.interactor.model.relationship;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.transfer.IDataTransferInputModel;

import java.util.Map;

public interface IRelationshipDataTransferInputModel extends IModel {
  
  public static final String DATA_TRANSFER          = "dataTransfer";
  public static final String MODIFIED_RELATIONSHIPS = "modifiedRelationships";
  
  public IDataTransferInputModel getDataTransfer();
  
  public void setDataTransfer(IDataTransferInputModel dataTransfer);
  
  public Map<String, IEntityRelationshipInfoModel> getModifiedRelationships();
  
  public void setModifiedRelationships(
      Map<String, IEntityRelationshipInfoModel> modifiedRelationships);
}
