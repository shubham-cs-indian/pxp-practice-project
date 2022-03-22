package com.cs.core.runtime.interactor.model.relationship;

import com.cs.core.runtime.interactor.model.transfer.IDataTransferInputModel;

import java.util.HashMap;
import java.util.Map;

public class RelationshipDataTransferInputModel implements IRelationshipDataTransferInputModel {
  
  private static final long                           serialVersionUID = 1L;
  protected Map<String, IEntityRelationshipInfoModel> modifiedRelationships;
  protected IDataTransferInputModel                   dataTransfer;
  
  @Override
  public Map<String, IEntityRelationshipInfoModel> getModifiedRelationships()
  {
    if (modifiedRelationships == null) {
      modifiedRelationships = new HashMap<>();
    }
    return modifiedRelationships;
  }
  
  @Override
  public void setModifiedRelationships(
      Map<String, IEntityRelationshipInfoModel> modifiedRelationships)
  {
    this.modifiedRelationships = modifiedRelationships;
  }
  
  @Override
  public IDataTransferInputModel getDataTransfer()
  {
    return dataTransfer;
  }
  
  @Override
  public void setDataTransfer(IDataTransferInputModel dataTransfer)
  {
    this.dataTransfer = dataTransfer;
  }
}
