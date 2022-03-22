package com.cs.core.config.interactor.model.relationship;

import com.cs.base.interactor.model.audit.ConfigResponseWithAuditLogModel;
import com.cs.core.runtime.interactor.model.relationship.ISideInfoForRelationshipDataTransferModel;
import com.cs.core.runtime.interactor.model.relationship.SideInfoForRelationshipDataTransferModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class SaveRelationshipStrategyResponseModel extends ConfigResponseWithAuditLogModel
    implements ISaveRelationshipStrategyResponseModel {
  
  private static final long                                 serialVersionUID = 1L;
  protected IGetRelationshipModel                           relationshipResponse;
  protected List<ISideInfoForRelationshipDataTransferModel> relationshipDataForTransfer;
  protected IRemovedContextInfoModel                        removedContextInfo;
  
  @Override
  public IGetRelationshipModel getRelationshipResponse()
  {
    return relationshipResponse;
  }
  
  @Override
  @JsonDeserialize(as = GetRelationshipModel.class)
  public void setRelationshipResponse(IGetRelationshipModel relationshipResponse)
  {
    this.relationshipResponse = relationshipResponse;
  }
  
  @Override
  public List<ISideInfoForRelationshipDataTransferModel> getRelationshipDataForTransfer()
  {
    if (relationshipDataForTransfer == null) {
      relationshipDataForTransfer = new ArrayList<>();
    }
    return relationshipDataForTransfer;
  }
  
  @Override
  @JsonDeserialize(contentAs = SideInfoForRelationshipDataTransferModel.class)
  public void setRelationshipDataForTransfer(
      List<ISideInfoForRelationshipDataTransferModel> relationshipDataForTransfer)
  {
    this.relationshipDataForTransfer = relationshipDataForTransfer;
  }

  @Override
  public IRemovedContextInfoModel getRemovedContextInfo()
  {
    return removedContextInfo;
  }

  @Override
  @JsonDeserialize(as = RemovedContextInfoModel.class)
  public void setRemovedContextInfo(IRemovedContextInfoModel removedContextInfo)
  {
    this.removedContextInfo = removedContextInfo;
  }
}
