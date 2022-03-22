package com.cs.config.strategy.plugin.usecase.klass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataResponseModel;
import com.cs.core.config.interactor.model.klass.IGetSectionInfoForRelationshipExportModel;
import com.cs.core.config.interactor.model.klass.IGetSectionInfoForRelationshipExportResponseModel;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.di.workflow.constants.DiConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;

public class GetSectionInfoForRelationshipExport extends AbstractOrientPlugin {
  
  private static final List<String> FIELDS_TO_FETCH         = Arrays.asList(
      CommonConstants.ICON_PROPERTY, CommonConstants.CID_PROPERTY, CommonConstants.LABEL_PROPERTY,
      CommonConstants.TYPE_PROPERTY, CommonConstants.CODE_PROPERTY);
  
  private static final List<String> FIELDS_TO_FETCH_FOR_TAG = Arrays.asList(
      CommonConstants.CID_PROPERTY, CommonConstants.LABEL_PROPERTY, CommonConstants.TYPE_PROPERTY,
      ITag.ICON, ITag.COLOR, CommonConstants.CODE_PROPERTY);
  
  public GetSectionInfoForRelationshipExport(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetSectionInfoForRelationshipExport/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> returnMap = new HashMap<>();
    String klassId = (String) requestMap.get(IGetSectionInfoForRelationshipExportModel.KLASS_ID);
    String entityType = (String) requestMap.get(IGetSectionInfoForRelationshipExportModel.ENTITY_TYPE);
    Vertex klassNode = null;
    try {
      klassNode = UtilClass.getVertexByIndexedId(klassId, VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
    }
    catch (Exception e) {
      throw new KlassNotFoundException();
    }
    
    if (entityType.equals(IGetSectionInfoForRelationshipExportResponseModel.RELATIONSHIPS)) {
      List<Map<String, Object>> relationshipsList = getSectionalInfo(klassNode,
          RelationshipLabelConstants.HAS_RELATIONSHIP_TO_EXPORT, FIELDS_TO_FETCH);
      returnMap.put(IGetConfigDataResponseModel.RELATIONSHIPS, relationshipsList);
    }
    
    if (entityType.equals(CommonConstants.PROPERTIES)) {
      List<Map<String, Object>> attributesList = getSectionalInfo(klassNode,
          RelationshipLabelConstants.HAS_ATTRIBUTE_TO_EXPORT, FIELDS_TO_FETCH);
      returnMap.put(IGetConfigDataResponseModel.ATTRIBUTES, attributesList);
      
      List<Map<String, Object>> tagsList = getSectionalInfo(klassNode,
          RelationshipLabelConstants.HAS_TAG_TO_EXPORT, FIELDS_TO_FETCH_FOR_TAG);
      returnMap.put(IGetConfigDataResponseModel.TAGS, tagsList);
    }
    
    return returnMap;
  }
  
  /**
   * @param klassNode
   * @param relationshipLable
   * @param fieldsToFetch
   * @return
   */
  private List<Map<String, Object>> getSectionalInfo(Vertex klassNode,
      String relationshipLable, List<String> fieldsToFetch)
  {
    List<Map<String, Object>> entitiesList = new ArrayList<>();
    Iterable<Vertex> entityNodes = klassNode.getVertices(Direction.OUT, relationshipLable);
    for (Vertex entityNode : entityNodes) {
      Map<String, Object> entityMap = UtilClass.getMapFromVertex(fieldsToFetch, entityNode);
      entitiesList.add(entityMap);
    }
    return entitiesList;
  }
}