package com.cs.core.runtime.interactor.model.fileupload;

import com.cs.core.runtime.interactor.model.offboarding.IKlassRelationshipSidesInfoModel;
import com.cs.core.runtime.interactor.model.offboarding.KlassRelationshipSidesInfoModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Map;

public class FileUploadRelationshipInstanceImportModel extends FileUploadInstanceImportModel
    implements IFileUploadRelationshipInstanceImportModel {
  
  private static final long                               serialVersionUID = 1L;
  
  protected Map<String, IKlassRelationshipSidesInfoModel> relationshipIdToRelationshipModelMap;
  protected Map<String, String>                           sourceIdToTypeKlassMap;
  protected Map<String, Object>                           relationshipIdToDestinationIdsMap;
  protected String                                        sourceId;
  protected Boolean                                       isLinked;
  protected Map<String, Object>                           relationshipDataMap;
  protected Map<String, Object>                           vertexLabelCodeIdMap;
  
  public FileUploadRelationshipInstanceImportModel()
  {
  }
  
  @Override
  public Map<String, IKlassRelationshipSidesInfoModel> getRelationshipIdToRelationshipModelMap()
  {
    return relationshipIdToRelationshipModelMap;
  }
  
  @Override
  @JsonDeserialize(contentAs = KlassRelationshipSidesInfoModel.class)
  public void setRelationshipIdToRelationshipModelMap(
      Map<String, IKlassRelationshipSidesInfoModel> relationshipIdToRelationshipModelMap)
  {
    this.relationshipIdToRelationshipModelMap = relationshipIdToRelationshipModelMap;
  }
  
  @Override
  public Map<String, String> getSourceIdToTypeKlassMap()
  {
    return sourceIdToTypeKlassMap;
  }
  
  @Override
  public void setSourceIdToTypeKlassMap(Map<String, String> sourceIdToTypeKlassMap)
  {
    this.sourceIdToTypeKlassMap = sourceIdToTypeKlassMap;
  }
  
  @Override
  public Map<String, Object> getRelationshipIdToDestinationIdsMap()
  {
    return relationshipIdToDestinationIdsMap;
  }
  
  @Override
  public void setRelationshipIdToDestinationIdsMap(
      Map<String, Object> relationshipIdToDestinationIdsMap)
  {
    this.relationshipIdToDestinationIdsMap = relationshipIdToDestinationIdsMap;
  }
  
  @Override
  public String getSourceId()
  {
    return sourceId;
  }
  
  @Override
  public void setSourceId(String sourceId)
  {
    this.sourceId = sourceId;
  }
  
  @Override
  public Boolean getIsLinked()
  {
    return isLinked;
  }
  
  @Override
  public void setIsLinked(Boolean isLinked)
  {
    this.isLinked = isLinked;
  }
  
  @Override
  public Map<String, Object> getRelationshipDataMap()
  {
    return relationshipDataMap;
  }
  
  @Override
  public void setRelationshipDataMap(Map<String, Object> relationshipDataMap)
  {
    this.relationshipDataMap = relationshipDataMap;
  }
  
  @Override
  public Map<String, Object> getVertexLabelCodeIdMap()
  {
    return vertexLabelCodeIdMap;
  }
  
  @Override
  public void setVertexLabelCodeIdMap(Map<String, Object> vertexLabelCodeIdMap)
  {
    this.vertexLabelCodeIdMap = vertexLabelCodeIdMap;
  }
}
