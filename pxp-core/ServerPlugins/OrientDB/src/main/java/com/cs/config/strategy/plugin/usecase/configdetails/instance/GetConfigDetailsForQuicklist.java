package com.cs.config.strategy.plugin.usecase.configdetails.instance;

import com.cs.config.strategy.plugin.usecase.globalpermission.util.GlobalPermissionUtils;
import com.cs.config.strategy.plugin.usecase.klass.util.ConfigDetailsUtils;
import com.cs.config.strategy.plugin.usecase.klass.util.MultiClassificationUtils;
import com.cs.config.strategy.plugin.usecase.role.util.RoleUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.globalpermissions.IGlobalPermission;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.klass.IKlassNatureRelationship;
import com.cs.core.config.interactor.entity.klass.IKlassRelationshipSide;
import com.cs.core.config.interactor.entity.relationship.IRelationship;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.klass.IReferencedSectionRelationshipModel;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configdetails.IConfigDetailsForQuickListModel;
import com.cs.core.runtime.interactor.model.targetinstance.IGetTargetKlassesModel;
import com.cs.repository.relationship.RelationshipRepository;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unchecked")
public class GetConfigDetailsForQuicklist extends AbstractOrientPlugin {
  
  public GetConfigDetailsForQuicklist(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetConfigDetailsForQuicklist/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String userId = (String) requestMap.get("userId");
    Map<String, Object> mapToReturn = new HashMap<>();
    Vertex userInRole = RoleUtils.getRoleFromUser(userId);
    
    List<String> displayTagIds = getDisplayTagIds();
    List<String> relevanceTagIds = getRelevenceTagIds();
    
    mapToReturn.put(IConfigDetailsForQuickListModel.DISPLAY_TAG_IDS, displayTagIds);
    mapToReturn.put(IConfigDetailsForQuickListModel.RELEVANCE_TAG_IDS, relevanceTagIds);
    
    // TODO: Override for references QuickList. Inside Klass Relationship is
    // used. Override and use
    // klassReferences
    fillTargetAndAllowedTypes((String) requestMap.get(IGetTargetKlassesModel.SIDE_ID), mapToReturn);
    
    fillRelationshipConfig((String) requestMap.get("relationshipId"), mapToReturn);
    
    ConfigDetailsUtils.fillSide2LinkedVariantKrIds(mapToReturn);
    GlobalPermissionUtils.fillEntitiesAndKlassIdsAndTaxonomyIdsHavingReadPermission(userId,
        mapToReturn);
    fillKlassIdsHavingCreatePermission(mapToReturn, userInRole);
    Map<String, Object> xRayConfigDetails = MultiClassificationUtils.getXRayConfigDetails(
        (List<String>) requestMap.get(IGetTargetKlassesModel.X_RAY_ATTRIBUTES),
        (List<String>) requestMap.get(IGetTargetKlassesModel.X_RAY_TAGS));
    String klassId = (String) requestMap.get(IGetTargetKlassesModel.KLASS_ID);
    Vertex klassNode = UtilClass.getVertexByIndexedId(klassId,
        VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
    mapToReturn.put(IConfigDetailsForQuickListModel.X_RAY_CONFIG_DETAILS, xRayConfigDetails);
    return mapToReturn;
  }
  
  private List<String> getRelevenceTagIds()
  {
    OrientGraph graph = UtilClass.getGraph();
    Iterable<Vertex> iterable = graph
        .command(new OCommandSQL(
            "select code from tag where " + ITag.IS_ROOT + " = true and isForRelevance=true"))
        .execute();
    List<String> relevanceTagIds = new ArrayList<>();
    for (Vertex tagNode : iterable) {
      relevanceTagIds.add(tagNode.getProperty(CommonConstants.CODE_PROPERTY));
    }
    return relevanceTagIds;
  }
  
  private List<String> getDisplayTagIds()
  {
    OrientGraph graph = UtilClass.getGraph();
    Iterable<Vertex> i = graph
        .command(new OCommandSQL(
            "select code from tag where " + ITag.IS_ROOT + " = true and shouldDisplay=true"))
        .execute();
    List<String> displayTagIds = new ArrayList<>();
    for (Vertex tagNode : i) {
      displayTagIds.add(tagNode.getProperty(CommonConstants.CODE_PROPERTY));
    }
    return displayTagIds;
  }
  
  protected void fillTargetAndAllowedTypes(String sideId, Map<String, Object> returnMap)
      throws Exception
  {
    Vertex krNode = null;
    krNode = UtilClass.getVertexByIndexedId(sideId, VertexLabelConstants.KLASS_RELATIONSHIP);
    
    Map<String, Object> krMap = UtilClass.getMapFromNode(krNode);
    Map<String, Object> relationshipSide = (Map<String, Object>) krMap
        .get(IReferencedSectionRelationshipModel.RELATIONSHIP_SIDE);
    String targetKlassId = (String) relationshipSide.get(IKlassRelationshipSide.KLASS_ID);
    Vertex targetKlassNode = null;
    try {
      targetKlassNode = UtilClass.getVertexById(targetKlassId,
          VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
    }
    catch (Exception e) {
      throw new KlassNotFoundException();
    }
    String rid = targetKlassNode.getId()
        .toString();
    String type = (String) targetKlassNode.getProperty(CommonConstants.TYPE_PROPERTY);
    Iterable<Vertex> i = UtilClass.getGraph()
        .command(new OCommandSQL(
            "select from(traverse in('Child_Of') from " + rid + " strategy BREADTH_FIRST)"))
        .execute();
    List<String> klassAndChildNodes = new ArrayList<>();
    List<String> klassAndChildNodesCids = new ArrayList<>();
    
    for (Vertex node : i) {
      klassAndChildNodes.add(node.getProperty(CommonConstants.CODE_PROPERTY)
          .toString());
      klassAndChildNodesCids.add(UtilClass.getCodeNew(node));
    }
    Iterable<Vertex> relationshipNodes = krNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_PROPERTY);
    Iterator<Vertex> iterator = relationshipNodes.iterator();
    List<String> klassRelationshipIds = new ArrayList<>();
    if (iterator.hasNext()) {
      String relationshipType = iterator.next()
          .getProperty(IKlassNatureRelationship.RELATIONSHIP_TYPE);
      if (relationshipType != null
          && relationshipType.equals(CommonConstants.PRODUCT_VARIANT_RELATIONSHIP)) {
        String subQuery = "(select from " + VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS
            + " where code in " + EntityUtil.quoteIt(klassAndChildNodesCids) + ")";
        klassRelationshipIds = RelationshipRepository.fetchSide2LinkedVariantKrIds(subQuery);
      }
    }
    returnMap.put(IConfigDetailsForQuickListModel.KLASS_RELATIONSHIP_IDS, klassRelationshipIds);
    returnMap.put(IConfigDetailsForQuickListModel.ALLOWED_TYPES, klassAndChildNodes);
    if (type != null)
      returnMap.put(IConfigDetailsForQuickListModel.KLASS_TYPE, type);
  }
  
  protected void fillRelationshipConfig(String relationshipId, Map<String, Object> returnMap)
      throws Exception
  {
    Vertex relationshipNode = null;
    try {
      relationshipNode = UtilClass.getVertexById(relationshipId,
          VertexLabelConstants.ENTITY_TYPE_RELATIONSHIP);
    }
    catch (NotFoundException e) {
      relationshipNode = UtilClass.getVertexById(relationshipId,
          VertexLabelConstants.NATURE_RELATIONSHIP);
    }
    List<String> relationshipFieldToFetch = Arrays.asList(IRelationship.ID,
        IRelationship.PROPERTY_IID, IRelationship.CODE, IRelationship.IS_NATURE,
        IRelationship.TYPE, IRelationship.SIDE1, IRelationship.SIDE2);
    Map<String, Object> referencedElement = UtilClass.getMapFromVertex(relationshipFieldToFetch,
        relationshipNode);
    returnMap.put("relationshipConfig", referencedElement);
  }
  
  private void fillKlassIdsHavingCreatePermission(Map<String, Object> mapToReturn, Vertex roleNode)
      throws Exception
  {
    Set<String> klassIdsHavingRP = (Set<String>) mapToReturn
        .get(IConfigDetailsForQuickListModel.KLASS_IDS_HAVING_RP);
    Set<String> klassIdsHavingCP = new HashSet<>();
    String roleId = UtilClass.getCodeNew(roleNode);
    List<String> klassIds = new ArrayList<>(klassIdsHavingRP);
    if (klassIds.isEmpty()) {
      String query = "SELECT FROM " + VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS + " where "
          + IKlass.IS_NATURE + " is not null and " + IKlass.IS_NATURE + " = true and "
          + IKlass.IS_DEFAULT_CHILD + " = true";
      
      Iterable<Vertex> vertices = UtilClass.getGraph()
          .command(new OCommandSQL(query))
          .execute();
      for (Vertex klassNode : vertices) {
        klassIds.add(UtilClass.getCodeNew(klassNode));
      }
    }
    for (String klassId : klassIds) {
      Map<String, Object> klassAndTaxonomyPermission = GlobalPermissionUtils
          .getKlassAndTaxonomyPermission(klassId, roleId);
      Boolean canCreate = (Boolean) klassAndTaxonomyPermission.get(IGlobalPermission.CAN_CREATE);
      if (canCreate) {
        klassIdsHavingCP.add(klassId);
      }
    }
    mapToReturn.put(IConfigDetailsForQuickListModel.KLASS_IDS_HAVING_CP, klassIdsHavingCP);
  }
}
