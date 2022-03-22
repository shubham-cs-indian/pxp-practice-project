package com.cs.core.config.interactor.model.relationship;

import java.util.List;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;
import com.cs.core.runtime.interactor.model.relationship.ISideInfoForRelationshipDataTransferModel;

public interface ISaveRelationshipStrategyResponseModel extends IConfigResponseWithAuditLogModel {
  
  public static final String RELATIONSHIP_RESPONSE          = "relationshipResponse";
  public static final String RELATIONSHIP_DATA_FOR_TRANSFER = "relationshipDataForTransfer";
  public static final String REMOVED_CONTEXT_INFO           = "removedContextInfo";
  public IGetRelationshipModel getRelationshipResponse();
  
  public void setRelationshipResponse(IGetRelationshipModel relationshipResponse);
  
  public List<ISideInfoForRelationshipDataTransferModel> getRelationshipDataForTransfer();
  
  public void setRelationshipDataForTransfer(
      List<ISideInfoForRelationshipDataTransferModel> relationshipDataForTransfer);
  
  public IRemovedContextInfoModel getRemovedContextInfo();
  
  public void setRemovedContextInfo(IRemovedContextInfoModel removedContextInfo);
}
