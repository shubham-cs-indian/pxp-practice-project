package com.cs.core.runtime.interactor.model.fileupload;

import com.cs.core.runtime.interactor.model.offboarding.IKlassRelationshipSidesInfoModel;

import java.util.Map;

public interface IFileUploadRelationshipInstanceImportModel extends IFileUploadInstanceImportModel {
  
  public static final String RELATIONSHIP_ID_TO_RELATIONSHIP_MODEL_MAP = "relationshipIdToRelationshipModelMap";
  public static final String SOURCE_ID_TO_TYPE_KLASS_MAP               = "sourceIdToTypeKlassMap";
  public static final String RELATIONSHIP_ID_TO_DESTINATION_IDS_MAP    = "relationshipIdToDestinationIdsMap";
  public static final String SOURCEID                                  = "sourceId";
  public static final String IS_LINKED                                 = "isLinked";
  public static final String RELATIONSHIP_DATA_MAP                     = "relationshipDataMap";
  public static final String VERTEX_LABLE_CODE_ID_MAP                  = "vertexLabelCodeIdMap";
  
  public Map<String, IKlassRelationshipSidesInfoModel> getRelationshipIdToRelationshipModelMap();
  
  public void setRelationshipIdToRelationshipModelMap(
      Map<String, IKlassRelationshipSidesInfoModel> relationshipIdToRelationshipModelMap);
  
  public Map<String, String> getSourceIdToTypeKlassMap();
  
  public void setSourceIdToTypeKlassMap(Map<String, String> sourceIdToTypeKlassMap);
  
  public Map<String, Object> getRelationshipIdToDestinationIdsMap();
  
  public void setRelationshipIdToDestinationIdsMap(
      Map<String, Object> relationshipIdToDestinationIdsMap);
  
  public String getSourceId();
  
  public void setSourceId(String sourceId);
  
  public Boolean getIsLinked();
  
  public void setIsLinked(Boolean isLinked);
  
  public Map<String, Object> getRelationshipDataMap();
  
  public void setRelationshipDataMap(Map<String, Object> relationshipDataMap);
  
  public Map<String, Object> getVertexLabelCodeIdMap();
  
  public void setVertexLabelCodeIdMap(Map<String, Object> vertexLabelCodeIdMap);
}
