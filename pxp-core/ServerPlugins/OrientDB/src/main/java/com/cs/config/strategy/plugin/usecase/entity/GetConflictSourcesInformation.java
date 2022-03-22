package com.cs.config.strategy.plugin.usecase.entity;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.configuration.base.IConfigMasterEntity;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.klassinstance.IConflictSourcesRequestModel;
import com.cs.core.runtime.interactor.model.klassinstance.IGetConflictSourcesInformationModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class GetConflictSourcesInformation extends AbstractOrientPlugin {
  
  public static final List<String> fieldsToFetch = Arrays.asList(CommonConstants.CODE_PROPERTY,
      IConfigMasterEntity.LABEL, IConfigMasterEntity.CODE);
  
  public GetConflictSourcesInformation(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetConflictSourcesInformation/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<String> klasses = (List<String>) requestMap.get(IConflictSourcesRequestModel.KLASSES);
    List<String> relationships = (List<String>) requestMap
        .get(IConflictSourcesRequestModel.RELATIONSHIPS);
    List<String> taxonomies = (List<String>) requestMap
        .get(IConflictSourcesRequestModel.TAXONOMIES);
    List<String> contexts = (List<String>) requestMap
        .get(IConflictSourcesRequestModel.VARIANT_CONTEXTS);
    List<String> languages = (List<String>) requestMap.get(IConflictSourcesRequestModel.LANGUAGES);
    
    Map<String, Object> klassMap = fetchEntitiesInfo(klasses,
        VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
    Map<String, Object> relationshipMap = fetchEntitiesInfo(relationships,
        VertexLabelConstants.ROOT_RELATIONSHIP);
    Map<String, Object> taxonomyMap = fetchEntitiesInfo(taxonomies,
        VertexLabelConstants.ROOT_KLASS_TAXONOMY);
    Map<String, Object> contextMap = fetchEntitiesInfo(contexts,
        VertexLabelConstants.VARIANT_CONTEXT);
    Map<String, Object> languageMap = fetchLanguageInfo(languages);
    
    Map<String, Object> returnMap = new HashMap<>();
    returnMap.put(IGetConflictSourcesInformationModel.KLASSES, klassMap);
    returnMap.put(IGetConflictSourcesInformationModel.RELATIONSHIPS, relationshipMap);
    returnMap.put(IGetConflictSourcesInformationModel.TAXONOMIES, taxonomyMap);
    returnMap.put(IGetConflictSourcesInformationModel.VARIANT_CONTEXTS, contextMap);
    returnMap.put(IGetConflictSourcesInformationModel.LANGUAGES, languageMap);
    return returnMap;
  }
  
  private Map<String, Object> fetchLanguageInfo(List<String> languages) throws Exception
  {
    Map<String, Object> returnMap = new HashMap<>();
    for (String language : languages) {
      Vertex vertex = UtilClass.getVertexByCode(language, VertexLabelConstants.LANGUAGE);
      Map<String, Object> map = UtilClass.getMapFromVertex(fieldsToFetch, vertex);
      String code = (String) map.get(IConfigMasterEntity.CODE);
      returnMap.put(code, map);
    }
    return returnMap;
  }
  
  private Map<String, Object> fetchEntitiesInfo(List<String> entityIds, String vertexLabel)
      throws Exception
  {
    Map<String, Object> returnMap = new HashMap<>();
    
    Iterable<Vertex> vertices = UtilClass.getVerticesByIndexedIds(entityIds, vertexLabel);
    for (Vertex vertex : vertices) {
      Map<String, Object> map = UtilClass.getMapFromVertex(fieldsToFetch, vertex);
      String id = (String) map.get(IConfigMasterEntity.ID);
      returnMap.put(id, map);
    }
    return returnMap;
  }
}
