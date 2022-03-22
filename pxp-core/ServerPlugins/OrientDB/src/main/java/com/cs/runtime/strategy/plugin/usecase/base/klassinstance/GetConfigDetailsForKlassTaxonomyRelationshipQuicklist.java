package com.cs.runtime.strategy.plugin.usecase.base.klassinstance;

import com.cs.config.strategy.plugin.usecase.globalpermission.util.GlobalPermissionUtils;
import com.cs.config.strategy.plugin.usecase.relationship.util.RelationshipUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.exception.relationship.RelationshipSectionNotFoundException;
import com.cs.core.config.interactor.model.configdetails.IConfigEntityTreeInformationModel;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.targetinstance.IGetTargetKlassesModel;
import com.cs.core.runtime.interactor.model.templating.IConfigDetailsForHierarchyRelationshipQuicklistModel;
import com.cs.runtime.strategy.plugin.usecase.base.AbstractConfigDetails;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unchecked")
public class GetConfigDetailsForKlassTaxonomyRelationshipQuicklist extends AbstractConfigDetails {
  
  public static final List<String> fieldsToFetch = Arrays.asList(CommonConstants.CODE_PROPERTY,
      IConfigEntityTreeInformationModel.LABEL, IConfigEntityTreeInformationModel.TYPE,
      IConfigEntityTreeInformationModel.CODE);
  
  public GetConfigDetailsForKlassTaxonomyRelationshipQuicklist(
      OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetConfigDetailsForKlassTaxonomyRelationshipQuicklist/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String klassId = (String) requestMap.get(IGetTargetKlassesModel.KLASS_ID);
    String userId = (String) requestMap.get(IGetTargetKlassesModel.USER_ID);
    String relationshipId = (String) requestMap.get(IGetTargetKlassesModel.RELATIONSHIP_ID);
    
    Vertex klassNode = UtilClass.getVertexByIndexedId(klassId,
        VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
    String targetKlassId = RelationshipUtils.getTargetSideId(klassNode, relationshipId);
    
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
    Vertex targetKlassNode = UtilClass.getVertexByIndexedId(targetKlassId,
        VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
    
    Map<String, Object> returnMap = new HashMap<>();
    GlobalPermissionUtils.fillEntitiesAndKlassIdsAndTaxonomyIdsHavingReadPermission(userId,
        returnMap);
    Set<String> klassIdsHavingRP = (Set<String>) returnMap
        .remove(CommonConstants.KLASS_IDS_HAVING_RP);
    
    List<Vertex> targetKlassesWithRP = new ArrayList<>();
    if (klassIdsHavingRP.isEmpty() || klassIdsHavingRP.contains(targetKlassId)) {
      targetKlassesWithRP.add(targetKlassNode);
    }
    else {
      fillImigiateChildTaxonomiesWithCreatePermission(targetKlassNode, klassIdsHavingRP,
          targetKlassesWithRP);
    }
    
    List<Map<String, Object>> klassList = new ArrayList<>();
    List<String> klassesIds = new ArrayList<>();
    for (Vertex klassVertex : targetKlassesWithRP) {
      Map<String, Object> klassMap = UtilClass.getMapFromVertex(fieldsToFetch, klassVertex);
      String klassVertexId = (String) klassMap.get(CommonConstants.ID_PROPERTY);
      klassesIds.add(klassVertexId);
      fillKlassesTree(klassVertex, klassMap, klassesIds);
      klassList.add(klassMap);
    }
    
    Set<String> allowedTypes = new HashSet<>(klassesIds);
    if (!klassIdsHavingRP.isEmpty()) {
      allowedTypes.retainAll(klassIdsHavingRP);
    }
    
    String klassType = (String) targetKlassNode.getProperty(CommonConstants.TYPE_PROPERTY);
    returnMap.put(IConfigDetailsForHierarchyRelationshipQuicklistModel.KLASS_TYPE, klassType);
    returnMap.put(IConfigDetailsForHierarchyRelationshipQuicklistModel.CATEGORY_INFO, klassList);
    returnMap.put(IConfigDetailsForHierarchyRelationshipQuicklistModel.KLASSES_IDS, klassesIds);
    returnMap.put(IConfigDetailsForHierarchyRelationshipQuicklistModel.ALLOWED_TYPES, allowedTypes);
    return returnMap;
  }
  
  /**
   * This fill childNodesWithCP with its immidiate children having read
   * permission
   *
   * @author Lokesh
   * @param klassNode
   * @param klassIdsHavingRP
   * @param childNodesWithRP
   * @throws Exception
   */
  private void fillImigiateChildTaxonomiesWithCreatePermission(Vertex klassNode,
      Set<String> klassIdsHavingRP, List<Vertex> childNodesWithRP) throws Exception
  {
    Iterable<Vertex> childKlasses = klassNode.getVertices(Direction.IN,
        RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF);
    for (Vertex child : childKlasses) {
      String childId = UtilClass.getCodeNew(child);
      if (klassIdsHavingRP.contains(childId)) {
        childNodesWithRP.add(child);
        continue;
      }
      else {
        // if child don't have permission check for its children
        fillImigiateChildTaxonomiesWithCreatePermission(child, klassIdsHavingRP, childNodesWithRP);
      }
    }
  }
  
  private void fillKlassesTree(Vertex klassNode, Map<String, Object> klassMap,
      List<String> klassesIds)
  {
    List<Map<String, Object>> klassesList = new ArrayList<>();
    Iterable<Vertex> ChildNodes = klassNode.getVertices(Direction.IN,
        RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF);
    for (Vertex childNode : ChildNodes) {
      Map<String, Object> childMap = UtilClass.getMapFromVertex(fieldsToFetch, childNode);
      klassesIds.add((String) childMap.get(CommonConstants.ID_PROPERTY));
      fillKlassesTree(childNode, childMap, klassesIds);
      klassesList.add(childMap);
    }
    klassMap.put(IConfigEntityTreeInformationModel.CHILDREN, klassesList);
  }
}
