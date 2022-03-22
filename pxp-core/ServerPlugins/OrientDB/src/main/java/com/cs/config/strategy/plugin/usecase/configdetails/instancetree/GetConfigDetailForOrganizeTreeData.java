package com.cs.config.strategy.plugin.usecase.configdetails.instancetree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.config.strategy.plugin.usecase.configdetails.instancetree.abstrct.AbstractConfigDetailsForNewInstanceTree;
import com.cs.config.strategy.plugin.usecase.globalpermission.util.GlobalPermissionUtils;
import com.cs.config.strategy.plugin.usecase.role.util.RoleUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.base.interactor.model.IModuleEntitiesWithUserIdModel;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.role.IRole;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.taxonomy.ITaxonomy;
import com.cs.core.config.interactor.model.configdetails.IConfigEntityTreeInformationModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.exception.configuration.MultipleVertexFoundException;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForGetKlassTaxonomyTreeRequestModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForRelationshipQuicklistRequestModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsOrganizeTreeDataResponseModel;
import com.cs.core.runtime.interactor.model.datarule.ICategoryInformationModel;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class GetConfigDetailForOrganizeTreeData
    extends AbstractConfigDetailsForNewInstanceTree {
  
  public static final List<String> fieldsToFetch = Arrays.asList(IKlass.ID, IKlass.LABEL,
      IKlass.CODE, IKlass.TYPE, ITaxonomy.TAXONOMY_TYPE);
  
  public GetConfigDetailForOrganizeTreeData(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetConfigDetailForOrganizeTreeData/*" };
  }
  
  @SuppressWarnings("unchecked")
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String userId = (String) requestMap.get(IModuleEntitiesWithUserIdModel.USER_ID);
    String parentTaxonomyId = (String) requestMap
        .get(IConfigDetailsForGetKlassTaxonomyTreeRequestModel.PARENT_TAXONOMY_ID);
    String clickedTaxonomyId = (String) requestMap
        .get(IModuleEntitiesWithUserIdModel.CLICKED_TAXONOMY_ID);
    Vertex userInRole = RoleUtils.getRoleFromUser(userId);
    String roleId = UtilClass.getCId(userInRole);
    
    Map<String, Object> returnMap = new HashMap<>();
    List<String> allowedEntities = getAllowedEntitiesByUserRole(userInRole);
    List<String> parentKlassIds = getParentKlassIds(requestMap, allowedEntities, returnMap);
    List<Map<String, Object>> klassTaxonomyList = new ArrayList<>();
    List<String> searchableAttributes = getSearchableAttributes(allowedEntities);
    List<String> translatableAttributes = getTranslatableAttributes(allowedEntities);
    List<String> selectedTaxonomyIds = (List<String>) requestMap
        .get(IConfigDetailsForGetKlassTaxonomyTreeRequestModel.SELECTED_TAXONOMY_IDS);
    
    Set<String> taxonomyIds = GlobalPermissionUtils.getTaxonomyIdsHavingReadPermission(userInRole);
    Set<String> klassesIds = GlobalPermissionUtils.getKlassIdsHavingReadPermission(userInRole);
    List<String> majorTaxonomyIds = GlobalPermissionUtils.getRootMajorTaxonomyIds();
    
    if (clickedTaxonomyId != null && clickedTaxonomyId.equals("-1")) {
      fillKlassDetails(parentKlassIds, roleId, klassTaxonomyList);
    }
    else {
      fillKlassDetails(parentKlassIds, roleId, klassTaxonomyList);
      fillRootKlassTaxonomy(klassTaxonomyList, roleId, parentTaxonomyId, clickedTaxonomyId,
          selectedTaxonomyIds);
    }
    
    returnMap.put(IConfigDetailsOrganizeTreeDataResponseModel.KLASS_IDS_HAVING_RP,
        klassesIds);
    returnMap.put(IConfigDetailsOrganizeTreeDataResponseModel.TAXONOMY_IDS_HAVING_RP,
        taxonomyIds);
    returnMap.put(IConfigDetailsOrganizeTreeDataResponseModel.KLASS_TAXONOMY_INFO,
        klassTaxonomyList);
    returnMap.put(IConfigDetailsOrganizeTreeDataResponseModel.ALLOWED_ENTITIES,
        allowedEntities);
    returnMap.put(IConfigDetailsOrganizeTreeDataResponseModel.SEARCHABLE_ATTRIBUTE_IDS,
        searchableAttributes);
    returnMap.put(IConfigDetailsOrganizeTreeDataResponseModel.TRANSLATABLE_ATTRIBUTE_IDS,
        translatableAttributes);
    returnMap.put(IConfigDetailsOrganizeTreeDataResponseModel.MAJOR_TAXONOMY_IDS,
        majorTaxonomyIds);
    
    String kpiId = (String) requestMap
        .get(IConfigDetailsForGetKlassTaxonomyTreeRequestModel.KPI_ID);
    kpiHandling(kpiId, returnMap);
    
    return returnMap;
  }
  
  protected Map<String, Object> getClassTaxonomy() throws Exception
  {
    Vertex classTaxonomyTranslationNode = UtilClass.getVertexByIndexedId(
        CommonConstants.CLASS_TAXONOMY_UI_TRANSLATION, VertexLabelConstants.UI_TRANSLATIONS);
    
    Map<String, Object> klassInfo = new HashMap<>();
    klassInfo.put(ICategoryInformationModel.CHILDREN, new ArrayList<>());
    klassInfo.put(IConfigEntityInformationModel.ID, "-1");
    klassInfo.put(ICategoryInformationModel.LABEL, UtilClass
        .getValueByLanguage(classTaxonomyTranslationNode, ICategoryInformationModel.LABEL));
    return klassInfo;
  }
  
  @SuppressWarnings("unchecked")
  private List<String> getAllowedEntitiesByUserRole(Vertex userInRole)
  {
    List<String> allowedEntities = (List<String>) userInRole.getProperty(IRole.ENTITIES);
    if (allowedEntities.isEmpty()) {
      allowedEntities.addAll(CommonConstants.MODULE_ENTITIES);
      allowedEntities.add(CommonConstants.FILE_INSTANCE_MODULE_ENTITY);
    }
    return allowedEntities;
  }
  
  @SuppressWarnings("unchecked")
  protected List<String> getParentKlassIds(Map<String, Object> requestMap,
      List<String> allowedEntities, Map<String, Object> mapToReturn) throws Exception
  {
    List<String> moduleEntities = (List<String>) requestMap
        .get(IModuleEntitiesWithUserIdModel.ALLOWED_ENTITIES);
    allowedEntities.retainAll(moduleEntities);
    return EntityUtil.getStandardKlassIds(allowedEntities);
  }
  
  @SuppressWarnings("unchecked")
  private void fillKlassDetails(List<String> parentKlassIds, String roleId,
      List<Map<String, Object>> klassTaxonomyList) throws Exception
  {
    Map<String, Object> klassInfo = getClassTaxonomy();
    List<Map<String, Object>> klassList = (List<Map<String, Object>>) klassInfo
        .get(ICategoryInformationModel.CHILDREN);
    klassTaxonomyList.add(klassInfo);
    
    Iterable<Vertex> vertices = UtilClass.getVerticesByIndexedIds(parentKlassIds,
        VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
    for (Vertex klassNode : vertices) {
      fillKlassesAndTaxonomies(klassList, klassNode, true, true, null);
    }
  }
  
  private void fillRootKlassTaxonomy(List<Map<String, Object>> taxonomyList, String roleId,
      String parentTaxonomyId, String clickedTaxonomyId, List<String> selectedTaxonomyIds)
      throws NotFoundException, MultipleVertexFoundException, Exception
  {
    if (parentTaxonomyId != null && !parentTaxonomyId.isEmpty()) {
      Vertex parentTaxonomyVertex = UtilClass.getVertexByIndexedId(clickedTaxonomyId,
          VertexLabelConstants.ROOT_KLASS_TAXONOMY);
      fillKlassesAndTaxonomies(taxonomyList, parentTaxonomyVertex, true, false, null);
    }
    else {
      Map<String, List<Map<String, Object>>> selectedTaxonomyVsChildrenMap = getChildrenUpToSelectedTaxonomy(
          selectedTaxonomyIds);
      Iterable<Vertex> taxonomiesVertices = UtilClass.getGraph()
          .command(new OCommandSQL("select from " + VertexLabelConstants.ROOT_KLASS_TAXONOMY
              + " where outE('Child_Of').size() = 0 and " + ITaxonomy.TAXONOMY_TYPE + " not in "
              + EntityUtil.quoteIt(Arrays.asList(CommonConstants.MINOR_TAXONOMY)) + " order by "
              + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " asc"))
          .execute();
      
      for (Vertex taxonomyVertex : taxonomiesVertices) {
        String taxonomyId = UtilClass.getCId(taxonomyVertex);
        fillKlassesAndTaxonomies(taxonomyList, taxonomyVertex, false, false,
            selectedTaxonomyVsChildrenMap.get(taxonomyId));
      }
    }
  }
  
  private Map<String, List<Map<String, Object>>> getChildrenUpToSelectedTaxonomy(
      List<String> selectedTaxonomyIds) throws Exception
  {
    Map<String, List<Map<String, Object>>> selectedTaxonomyVsChildrenMap = new HashMap<>();
    
    for (String selectedTaxonomyId : selectedTaxonomyIds) {
      Vertex selectedTaxonomyNode = UtilClass.getVertexByIndexedId(selectedTaxonomyId,
          VertexLabelConstants.ROOT_KLASS_TAXONOMY);
      
      selectedTaxonomyVsChildrenMap.put(selectedTaxonomyId,
          getChildernData(selectedTaxonomyNode, true, new HashMap<>()));
      
      fillClickedTaxonomyParent(selectedTaxonomyNode, selectedTaxonomyVsChildrenMap);
    }
    return selectedTaxonomyVsChildrenMap;
  }
  
  protected void fillClickedTaxonomyParent(Vertex clickedTaxonomyNode,
      Map<String, List<Map<String, Object>>> selectedTaxonomyVsChildrenMap) throws Exception
  {
    Iterator<Vertex> iterator = clickedTaxonomyNode
        .getVertices(Direction.OUT, RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF)
        .iterator();
    if (!iterator.hasNext()) {
      return;
    }
    Vertex taxonomyNode = iterator.next();
    if (CommonConstants.TAG_TYPE.equals(taxonomyNode.getProperty(ITag.TYPE))) {
      if (!iterator.hasNext()) {
        return;
      }
      taxonomyNode = iterator.next();
    }
    selectedTaxonomyVsChildrenMap.put(UtilClass.getCId(taxonomyNode),
        getChildernData(taxonomyNode, false, selectedTaxonomyVsChildrenMap));
    fillClickedTaxonomyParent(taxonomyNode, selectedTaxonomyVsChildrenMap);
  }
  
  /**
   * 
   * @param roleId
   * @param klassesList
   * @param klassNode
   * @param shouldFetchChildren
   *          : if children needs to be fetched then true, else false
   * @param getAllChildren
   *          : if only immediate children needs to be fetched then false, else
   *          true
   * @throws Exception
   */
  private void fillKlassesAndTaxonomies(List<Map<String, Object>> klassesList, Vertex klassNode,
      Boolean shouldFetchChildren, Boolean getAllChildren, List<Map<String, Object>> children)
      throws Exception
  {
    Map<String, Object> klassMap = UtilClass.getMapFromVertex(fieldsToFetch, klassNode);
    klassesList.add(klassMap);
    if (shouldFetchChildren && (children == null || children.isEmpty())) {
      children = getChildernData(klassNode, getAllChildren, new HashMap<>());
    }
    klassMap.put(IConfigEntityTreeInformationModel.CHILDREN, children);
  }
  
  private List<Map<String, Object>> getChildernData(Vertex klassNode, Boolean getAllChildren,
      Map<String, List<Map<String, Object>>> taxonomyIdVsChildrenList) throws Exception
  {
    List<Map<String, Object>> klassesList = new ArrayList<>();
    OrientGraph graph = UtilClass.getGraph();
    
    Iterable<Vertex> ChildNodes = graph
        .command(new OCommandSQL(
            "select expand(in('" + RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF
                + "')) from " + klassNode.getId() + " order by "
                + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " asc"))
        .execute();
    for (Vertex childNode : ChildNodes) {
      String childId = UtilClass.getCId(childNode);
      fillKlassesAndTaxonomies(klassesList, childNode, getAllChildren, getAllChildren,
          taxonomyIdVsChildrenList.get(childId));
    }
    return klassesList;
  }
  
  protected List<String> fillTargetIdsForRelationship(Map<String, Object> requestMap,
      String targetId) throws Exception
  {
    String sideId = (String) requestMap
        .get(IConfigDetailsForRelationshipQuicklistRequestModel.SIDE_ID);
    List<String> targetIds = new ArrayList<>();
    String relationshipId = (String) requestMap
        .get(IConfigDetailsForRelationshipQuicklistRequestModel.RELATIONSHIP_ID);
    Vertex relationshipNode = UtilClass.getVertexByIndexedId(relationshipId,
        VertexLabelConstants.ROOT_RELATIONSHIP);
    Iterable<Vertex> kRNodes = relationshipNode.getVertices(Direction.IN,
        RelationshipLabelConstants.HAS_PROPERTY);
    for (Vertex kRNode : kRNodes) {
      if (sideId != null && sideId.equals(UtilClass.getCId(kRNode))) {
        continue; // self side
      }
      Boolean foundTarget = false;
      Iterable<Vertex> klassNodes = kRNode.getVertices(Direction.IN,
          RelationshipLabelConstants.HAS_KLASS_PROPERTY);
      for (Vertex klassNode : klassNodes) {
        String klassId = UtilClass.getCId(klassNode);
        targetIds.add(klassId);
        if (klassId.equals(targetId)) {
          foundTarget = true;
        }
      }
      if (foundTarget) {
        break;
      }
      targetIds.clear();
    }
    return targetIds;
  }
  
}
