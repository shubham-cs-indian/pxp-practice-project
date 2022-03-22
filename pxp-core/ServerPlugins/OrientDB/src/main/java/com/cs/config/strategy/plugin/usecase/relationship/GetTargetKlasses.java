package com.cs.config.strategy.plugin.usecase.relationship;

import com.cs.config.strategy.plugin.usecase.globalpermission.util.GlobalPermissionUtils;
import com.cs.config.strategy.plugin.usecase.relationship.util.RelationshipUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.exception.relationship.RelationshipSectionNotFoundException;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.targetinstance.IGetTargetKlassesModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GetTargetKlasses extends AbstractOrientPlugin {
  
  public GetTargetKlasses(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String klassId = null;
    String relationshipId = null;
    
    // map = (HashMap<String, Object>) map.get("map");
    
    klassId = (String) requestMap.get(IGetTargetKlassesModel.KLASS_ID);
    String userId = (String) requestMap.get(IGetTargetKlassesModel.USER_ID);
    relationshipId = (String) requestMap.get(IGetTargetKlassesModel.RELATIONSHIP_ID);
    Vertex klassNode = null;
    try {
      klassNode = UtilClass.getVertexByIndexedId(klassId,
          VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
    }
    catch (NotFoundException e) {
      throw new KlassNotFoundException();
    }
    // HashMap<String, Object> getKlassMap =
    // RelationshipUtils.getKlass(klassNode);
    
    // String targetKlassId = RelationshipUtils.getTargetSideId(sections,
    // relationshipId);
    String targetKlassId = RelationshipUtils.getTargetSideId(klassNode, relationshipId);
    /*String targetRelationshipMappingId = RelationshipUtils
    .getTargetRelationshipMappingId(sections, relationshipId);*/
    if (targetKlassId == null) {
      Map<String, Object> relationshipSide = RelationshipUtils
          .getRelationshipSideForNatureRelationship(klassNode, relationshipId);
      targetKlassId = (String) relationshipSide.get(CommonConstants.RELATIONSHIP_SIDE_KLASSID);
      String targetRelationshipMappingId = (String) relationshipSide
          .get(CommonConstants.TARGET_RELATIONSHIP_MAPPING_ID_PROPERTY);
      if (targetRelationshipMappingId == null) {
        throw new RelationshipSectionNotFoundException();
      }
    }
    
    /*      String contentId = (String) map.get("id");
    List<String> selectedElements = RelationshipUtils.getSelectedElements(contentId,
        relationshipSectionId, targetRelationshipMappingId);*/
    // selectedElements.add((String) map.get("id"));
    try {
      klassNode = UtilClass.getVertexByIndexedId(targetKlassId,
          VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
    }
    catch (NotFoundException e) {
      throw new KlassNotFoundException();
    }
    
    String rid = klassNode.getId()
        .toString();
    String klassType = (String) klassNode.getProperty(CommonConstants.TYPE_PROPERTY);
    Iterable<Vertex> i = UtilClass.getGraph()
        .command(new OCommandSQL(
            "select from(traverse in('Child_Of') from " + rid + " strategy BREADTH_FIRST)"))
        .execute();
    List<String> klassAndChildNodeIds = new ArrayList<>();
    for (Vertex node : i) {
      klassAndChildNodeIds.add(node.getProperty(CommonConstants.CODE_PROPERTY)
          .toString());
    }
    Map<String, Object> returnMap = new HashMap<>();
    GlobalPermissionUtils.fillEntitiesAndKlassIdsAndTaxonomyIdsHavingReadPermission(userId,
        returnMap);
    Set<String> klassIdsHavingRP = (Set<String>) returnMap
        .remove(CommonConstants.KLASS_IDS_HAVING_RP);
    if (!klassIdsHavingRP.isEmpty()) {
      klassAndChildNodeIds.retainAll(klassIdsHavingRP);
    }
    returnMap.put("allowedTypes", klassAndChildNodeIds);
    returnMap.put("klassType", klassType);
    
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetTargetKlasses/*" };
  }
}
