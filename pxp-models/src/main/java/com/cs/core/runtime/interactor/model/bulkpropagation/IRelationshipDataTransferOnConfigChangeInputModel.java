package com.cs.core.runtime.interactor.model.bulkpropagation;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.relationship.ISideInfoForRelationshipDataTransferModel;

public interface IRelationshipDataTransferOnConfigChangeInputModel extends IModel {
  
  public static final String SIDE_INFO_FOR_RELATIONSHIP_DATA_TRANSFER = "sideInfoForRelationshipDataTransfer";
  public static final String CONTENT_TRANSFER_MAPPING                 = "contentTransferMapping";
  
  public ISideInfoForRelationshipDataTransferModel getSideInfoForRelationshipDataTransfer();
  
  public void setSideInfoForRelationshipDataTransfer(
      ISideInfoForRelationshipDataTransferModel sideInfoForRelationshipDataTransfer);
  
  public IContentTransferHelperModel getContentTransferMapping();
  
  public void setContentTransferMapping(IContentTransferHelperModel contentTransferMapping);
}
