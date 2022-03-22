package com.cs.core.runtime.interactor.model.relationship;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.cs.core.bgprocess.idto.IEntityRelationshipInfoDTO;
import com.cs.core.runtime.interactor.model.datarule.IAddedRemovedElementsModel;
import com.cs.core.runtime.interactor.model.instance.AddedRemovedElementsModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class RelationshipDataTransferInfoModel implements IRelationshipDataTransferInfoModel {
  
  private static final long                         serialVersionUID = 1L;
  protected Long                                    contentId;
  /*  protected String                              defaultAssetInstanceId;
  protected List<String>                            removedAssetInstanceIds;*/
  protected Map<String, IEntityRelationshipInfoDTO> relationshipIdAddedDeletedElementsMap;
  protected Set<String>                             changedRelationshipIds;
  protected Set<String>                             changedNatureRelationshipIds;
  
  @Override
  public Long getContentId()
  {
    return contentId;
  }
  
  @Override
  public void setContentId(Long contentId)
  {
    this.contentId = contentId;
  }

  
  /* @Override
  public String getDefaultAssetInstanceId()
  {
    return defaultAssetInstanceId;
  }
  
  @Override
  public void setDefaultAssetInstanceId(String defaultAssetInstanceId)
  {
    this.defaultAssetInstanceId = defaultAssetInstanceId;
  }
  
  @Override
  public List<String> getRemovedAssetInstanceIds()
  {
    if (removedAssetInstanceIds == null) {
      removedAssetInstanceIds = new ArrayList<>();
    }
    return removedAssetInstanceIds;
  }
  */
  /*  @Override
  public void setRemovedAssetInstanceIds(List<String> removedAssetInstanceIds)
  {
    this.removedAssetInstanceIds = removedAssetInstanceIds;
  }*/
  
  @Override
  public Map<String, IEntityRelationshipInfoDTO> getRelationshipIdAddedDeletedElementsMap()
  {
    if (relationshipIdAddedDeletedElementsMap == null) {
      relationshipIdAddedDeletedElementsMap = new HashMap<>();
    }
    return relationshipIdAddedDeletedElementsMap;
  }
  
  @JsonDeserialize(contentAs = AddedRemovedElementsModel.class)
  @Override
  public void setRelationshipIdAddedDeletedElementsMap(
      Map<String, IEntityRelationshipInfoDTO> relationshipIdAddedDeletedElementsMap)
  {
    this.relationshipIdAddedDeletedElementsMap = relationshipIdAddedDeletedElementsMap;
  }
  
  @Override
  public Set<String> getChangedRelationshipIds()
  {
    if (changedRelationshipIds == null) {
      changedRelationshipIds = new HashSet<>();
    }
    return changedRelationshipIds;
  }
  
  @Override
  public void setChangedRelationshipIds(Set<String> changedRelationshipIds)
  {
    this.changedRelationshipIds = changedRelationshipIds;
  }
  
  @Override
  public Set<String> getChangedNatureRelationshipIds()
  {
    if (changedNatureRelationshipIds == null) {
      changedNatureRelationshipIds = new HashSet<>();
    }
    return changedNatureRelationshipIds;
  }
  
  @Override
  public void setChangedNatureRelationshipIds(Set<String> changedNatureRelationshipIds)
  {
    this.changedNatureRelationshipIds = changedNatureRelationshipIds;
  }
}
