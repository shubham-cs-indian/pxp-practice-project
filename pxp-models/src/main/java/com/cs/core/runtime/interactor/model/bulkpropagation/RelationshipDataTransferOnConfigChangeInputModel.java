package com.cs.core.runtime.interactor.model.bulkpropagation;

import com.cs.core.runtime.interactor.model.relationship.ISideInfoForRelationshipDataTransferModel;

public class RelationshipDataTransferOnConfigChangeInputModel
    implements IRelationshipDataTransferOnConfigChangeInputModel {
  
  private static final long                           serialVersionUID = 1L;
  protected ISideInfoForRelationshipDataTransferModel sideInfoForRelationshipDataTransfer;
  protected IContentTransferHelperModel               contentTransferMapping;
  
  @Override
  public ISideInfoForRelationshipDataTransferModel getSideInfoForRelationshipDataTransfer()
  {
    return sideInfoForRelationshipDataTransfer;
  }
  
  @Override
  public void setSideInfoForRelationshipDataTransfer(
      ISideInfoForRelationshipDataTransferModel sideInfoForRelationshipDataTransfer)
  {
    this.sideInfoForRelationshipDataTransfer = sideInfoForRelationshipDataTransfer;
  }
  
  @Override
  public IContentTransferHelperModel getContentTransferMapping()
  {
    return contentTransferMapping;
  }
  
  @Override
  public void setContentTransferMapping(IContentTransferHelperModel contentTransferMapping)
  {
    this.contentTransferMapping = contentTransferMapping;
  }
}
