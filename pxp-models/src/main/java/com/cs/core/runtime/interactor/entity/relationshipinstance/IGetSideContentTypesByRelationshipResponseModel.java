package com.cs.core.runtime.interactor.entity.relationshipinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.klassinstance.ITypesTaxonomiesBaseTypeModel;
import java.util.List;
import java.util.Map;

public interface IGetSideContentTypesByRelationshipResponseModel extends IModel {
  
  public static final String CONTENT_ID_VS_RELATIONSHIP_IDS = "contentIdVsRelationshipIds";
  public static final String CONTENT_ID_VS_TYPES_TAXONOMIES = "contentIdVsTypesTaxonomies";
  public static final String SOURCE_ID_VS_TARGET_IDS        = "sourceIdVsTargetIds";
  public static final String CONTENT_ID_VS_LANGUAGE_CODES   = "contentIdVsLanguageCodes";
  
  // Key : contentId , value : [natureRelationshipId_sideId]
  public Map<String, List<String>> getContentIdVsRelationshipIds();
  
  public void setContentIdVsRelationshipIds(Map<String, List<String>> contentIdVsRelationshipIds);
  
  public Map<String, ITypesTaxonomiesBaseTypeModel> getContentIdVsTypesTaxonomies();
  
  public void setContentIdVsTypesTaxonomies(
      Map<String, ITypesTaxonomiesBaseTypeModel> baseTypeVsContentIds);
  
  public Map<String, List<String>> getSourceIdVsTargetIds();
  
  public void setSourceIdVsTargetIds(Map<String, List<String>> sourceIdVsTargetIds);
  
  public Map<String, List<String>> getContentIdVsLanguageCodes();
  
  public void setContentIdVsLanguageCodes(Map<String, List<String>> contentIdVsLanguageCodes);
}
