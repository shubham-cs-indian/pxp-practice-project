package com.cs.config.strategy.plugin.usecase.configdetails.instancetree.abstrct;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.config.strategy.plugin.usecase.globalpermission.util.GlobalPermissionUtils;
import com.cs.config.strategy.plugin.usecase.klass.util.KlassUtils;
import com.cs.config.strategy.plugin.usecase.role.util.RoleUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.constants.Constants;
import com.cs.core.base.interactor.model.IModuleEntitiesWithUserIdModel;
import com.cs.core.config.interactor.entity.configuration.base.ITreeEntity;
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
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsKlassTaxonomyTreeResponseModel;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeIconModel;
import com.cs.core.runtime.interactor.model.datarule.ICategoryInformationModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetKlassTaxonomyTreeResponseModel;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

@SuppressWarnings("unchecked")
public abstract class AbstarctConfigDetailForKlassTaxonomyTree extends AbstractConfigDetailsForNewInstanceTree {
  
  public static final List<String> fieldsToFetch = Arrays.asList(IKlass.ID, IKlass.LABEL, IKlass.CODE, IKlass.TYPE,
      ITaxonomy.TAXONOMY_TYPE, IIdLabelCodeIconModel.ICON);
  
  public AbstarctConfigDetailForKlassTaxonomyTree(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String userId = (String) requestMap.get(IModuleEntitiesWithUserIdModel.USER_ID);
    String clickedTaxonomyId = (String) requestMap.get(IModuleEntitiesWithUserIdModel.CLICKED_TAXONOMY_ID);
    Vertex userInRole = RoleUtils.getRoleFromUser(userId);
    String roleId = UtilClass.getCId(userInRole);
    String moduleId = (String) requestMap.get(IConfigDetailsForGetKlassTaxonomyTreeRequestModel.MODULE_ID);
    Map<String, Object> returnMap = new HashMap<>();
    List<String> allowedEntities = getAllowedEntitiesByUserRole(userInRole);
    prepareAllowedEntities(requestMap, allowedEntities, returnMap);
    List<Map<String, Object>> klassTaxonomyList = new ArrayList<>();
    List<String> searchableAttributes = getSearchableAttributes(allowedEntities);
    List<String> translatableAttributes = getTranslatableAttributes(allowedEntities);
    Set<String> taxonomyIds = GlobalPermissionUtils.getTaxonomyIdsHavingReadPermission(userInRole);
    Set<String> klassesIds = GlobalPermissionUtils.getKlassIdsHavingReadPermission(userInRole);
    List<String> majorTaxonomyIds = GlobalPermissionUtils.getRootMajorTaxonomyIds();
    
    String selectedCategory = (String) requestMap.get(IConfigDetailsForGetKlassTaxonomyTreeRequestModel.SELECTED_CATEGORY);
    int from = (Integer) requestMap.get(CommonConstants.FROM_PROPERTY);
    int size = (Integer) requestMap.get(CommonConstants.SIZE);
    List<String> moduleEntites = (List<String>) requestMap.get(IConfigDetailsForGetKlassTaxonomyTreeRequestModel.MODULE_ENTITIES);
    String baseTypeByEntityType = "";
    
    if(moduleEntites.isEmpty()) {
      baseTypeByEntityType = moduleId.equals(Constants.ALL_MODULE) ? null : EntityUtil.getKlassTypeByBaseType(allowedEntities.get(0));
    }
    else {
      baseTypeByEntityType = EntityUtil.getKlassTypeByBaseType(moduleEntites.get(0));
    }
    Long totalChildrenCount = 0L;
    String searchText = (String) requestMap.get(IConfigDetailsForGetKlassTaxonomyTreeRequestModel.SEARCH_TEXT);
    
    if (selectedCategory.equals(IGetKlassTaxonomyTreeResponseModel.NATURE_KLASSES)) {
      List<Vertex> allNatureKlassNodes = KlassUtils.getAllNatureKlassNodesForPagination(baseTypeByEntityType,
          from, size, searchText);
      totalChildrenCount = KlassUtils.getTotalCountOfKlasses(baseTypeByEntityType, searchText, true);
      fillKlassList(allNatureKlassNodes, klassTaxonomyList);
    }
    else if (selectedCategory.equals(IGetKlassTaxonomyTreeResponseModel.ATTRIBUTION_CLASSES)) {
      List<Vertex> allNonNatureKlassNodes = KlassUtils.getAllNonNatureKlassNodesForPagination(baseTypeByEntityType, from, size,
          searchText);
      totalChildrenCount = KlassUtils.getTotalCountOfKlasses(baseTypeByEntityType, searchText, false);
      fillKlassList(allNonNatureKlassNodes, klassTaxonomyList);
    }
    else if (selectedCategory.equals(IGetKlassTaxonomyTreeResponseModel.TAXONOMIES) && searchText.isEmpty()) {
        fillRootKlassTaxonomy(klassTaxonomyList, roleId, clickedTaxonomyId, from, size);
        totalChildrenCount = KlassUtils.getTotalCountOfMajorTaxonomies(clickedTaxonomyId);
    }
    else if (selectedCategory.equals("all")) {
      // For Nature Class
      List<Map<String, Object>> natureKlassList = new ArrayList<>();
      List<Map<String, Object>> children = new ArrayList<>();
      Long natureClassChildrenCount = 0L;
      List<Vertex> allNatureKlassNodes = KlassUtils
          .getAllNatureKlassNodesForPagination(baseTypeByEntityType, from, size, searchText);
      fillKlassList(allNatureKlassNodes, children);
      natureClassChildrenCount = KlassUtils.getTotalCountOfKlasses(baseTypeByEntityType, searchText,
          true);

      Map<String, Object> map = new HashMap<>();
      map.put(IConfigDetailsKlassTaxonomyTreeResponseModel.TOTAL_CHILDREN_COUNT,
          natureClassChildrenCount);
      map.put(ITreeEntity.CHILDREN, children);
      natureKlassList.add(map);
      returnMap.put(IGetKlassTaxonomyTreeResponseModel.NATURE_KLASSES, natureKlassList);
      
      // For NonNature Class
      List<Map<String, Object>> nonNatureKlassList = new ArrayList<>();
      List<Map<String, Object>> childrenAttribution = new ArrayList<>();
      Long nonNatureKlassChildrenCount = 0L;

      List<Vertex> allNonNatureKlassNodes = KlassUtils
          .getAllNonNatureKlassNodesForPagination(baseTypeByEntityType, from, size, searchText);
      fillKlassList(allNonNatureKlassNodes, childrenAttribution);
      nonNatureKlassChildrenCount = KlassUtils.getTotalCountOfKlasses(baseTypeByEntityType,
          searchText, false);
      
      Map<String, Object> mapAttribution = new HashMap<>();
      mapAttribution.put(IConfigDetailsKlassTaxonomyTreeResponseModel.TOTAL_CHILDREN_COUNT,
          nonNatureKlassChildrenCount);
      mapAttribution.put(ITreeEntity.CHILDREN, childrenAttribution);
      nonNatureKlassList.add(mapAttribution);
      returnMap.put(IGetKlassTaxonomyTreeResponseModel.ATTRIBUTION_CLASSES, nonNatureKlassList);
    }
    
    returnMap.put(IConfigDetailsKlassTaxonomyTreeResponseModel.KLASS_IDS_HAVING_RP, klassesIds);
    returnMap.put(IConfigDetailsKlassTaxonomyTreeResponseModel.TAXONOMY_IDS_HAVING_RP, taxonomyIds);
    if(!selectedCategory.equals("all")) {
      returnMap.put(selectedCategory, klassTaxonomyList);
    }
    returnMap.put(IConfigDetailsKlassTaxonomyTreeResponseModel.ALLOWED_ENTITIES, allowedEntities);
    returnMap.put(IConfigDetailsKlassTaxonomyTreeResponseModel.SEARCHABLE_ATTRIBUTE_IDS, searchableAttributes);
    returnMap.put(IConfigDetailsKlassTaxonomyTreeResponseModel.TRANSLATABLE_ATTRIBUTE_IDS, translatableAttributes);
    returnMap.put(IConfigDetailsKlassTaxonomyTreeResponseModel.MAJOR_TAXONOMY_IDS, majorTaxonomyIds);
    returnMap.put(IConfigDetailsKlassTaxonomyTreeResponseModel.TOTAL_CHILDREN_COUNT, totalChildrenCount);
    
    return returnMap;
  }
  
  private void fillKlassList(List<Vertex> klassNodes, List<Map<String, Object>> klassTaxonomyList) {
    for (Vertex klassNode : klassNodes) {
      Map<String, Object> klassMap = UtilClass.getMapFromVertex(fieldsToFetch, klassNode);
      klassTaxonomyList.add(klassMap);
    }
  }
  
  protected Map<String, Object> getClassTaxonomy() throws Exception
  {
    Vertex classTaxonomyTranslationNode = UtilClass.getVertexByIndexedId(CommonConstants.CLASS_TAXONOMY_UI_TRANSLATION, VertexLabelConstants.UI_TRANSLATIONS);
    
    Map<String, Object> klassInfo = new HashMap<>();
    klassInfo.put(ICategoryInformationModel.CHILDREN, new ArrayList<>());
    klassInfo.put(IConfigEntityInformationModel.ID, "-1");
    klassInfo.put(ICategoryInformationModel.LABEL, UtilClass.getValueByLanguage(classTaxonomyTranslationNode, ICategoryInformationModel.LABEL));
    return klassInfo;
  }

  private List<String> getAllowedEntitiesByUserRole(Vertex userInRole)
  {
    List<String> allowedEntities = (List<String>) userInRole.getProperty(IRole.ENTITIES);
    if (allowedEntities.isEmpty()) {
      allowedEntities.addAll(CommonConstants.MODULE_ENTITIES);
      allowedEntities.add(CommonConstants.FILE_INSTANCE_MODULE_ENTITY);
    }
    return allowedEntities;
  }

  protected void prepareAllowedEntities(Map<String, Object> requestMap, List<String> allowedEntities,  Map<String, Object> mapToReturn) throws Exception
  {
      List<String> moduleEntities = (List<String>) requestMap.get(IModuleEntitiesWithUserIdModel.ALLOWED_ENTITIES);
      allowedEntities.retainAll(moduleEntities);
  }
  
  private void fillRootKlassTaxonomy(List<Map<String, Object>> taxonomyList, String roleId,
      String clickedTaxonomyId, int from, int size)
      throws NotFoundException, MultipleVertexFoundException, Exception
  {
    if(clickedTaxonomyId != null && !clickedTaxonomyId.isEmpty()){
      Vertex parentTaxonomyVertex = UtilClass.getVertexByIndexedId(clickedTaxonomyId, VertexLabelConstants.ROOT_KLASS_TAXONOMY);
      fillKlassesAndTaxonomies(taxonomyList, parentTaxonomyVertex, true, false, from, size);
    }
    else{
      Iterable<Vertex> taxonomiesVertices = UtilClass.getGraph().command(new OCommandSQL
          ("select from " + VertexLabelConstants.ROOT_KLASS_TAXONOMY
              + " where outE('Child_Of').size() = 0 and " + ITaxonomy.TAXONOMY_TYPE + " not in "
              + EntityUtil.quoteIt(Arrays.asList(CommonConstants.MINOR_TAXONOMY)) + " order by "
              + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " asc " + "SKIP " + from + " LIMIT " + size))
          .execute();
      
      for(Vertex taxonomyVertex : taxonomiesVertices) {
        fillKlassesAndTaxonomies(taxonomyList, taxonomyVertex, false, false , from, size);
      }
    }
  }
  
  protected void fillClickedTaxonomyParent(Vertex clickedTaxonomyNode,
      Map<String, List<Map<String, Object>>> selectedTaxonomyVsChildrenMap, int from, int size) throws Exception
  {
    Iterator<Vertex> iterator = clickedTaxonomyNode.getVertices(Direction.OUT, RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF).iterator();
    if(!iterator.hasNext()) {
      return;
    }
    Vertex taxonomyNode = iterator.next();
    if(CommonConstants.TAG_TYPE.equals(taxonomyNode.getProperty(ITag.TYPE))) {
      if(!iterator.hasNext()) {
        return;
      }
      taxonomyNode = iterator.next();
    }
    selectedTaxonomyVsChildrenMap.put(UtilClass.getCId(taxonomyNode), getChildernData(taxonomyNode, false, selectedTaxonomyVsChildrenMap, from, size));
    fillClickedTaxonomyParent(taxonomyNode, selectedTaxonomyVsChildrenMap, from, size);
  }
  
  /**
   * 
   * @param roleId
   * @param klassesList
   * @param klassNode
   * @param shouldFetchChildren : if children needs to be fetched then true, else false
   * @param getAllChildren : if only immediate children needs to be fetched then false, else true
   * @param selectedType 
   * @param selectedType 
   * @param size 
   * @param from 
   * @throws Exception
   */
  private void fillKlassesAndTaxonomies(List<Map<String, Object>> klassesList, Vertex klassNode, 
      Boolean shouldFetchChildren, Boolean getAllChildren, int from, int size) throws Exception
  {
    List<Map<String, Object>> children = new ArrayList<Map<String,Object>>();
    Map<String, Object> klassMap = UtilClass.getMapFromVertex(fieldsToFetch, klassNode);
    Iterable<Edge> edges = klassNode.getEdges(Direction.IN,
        RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF);
    Boolean isChildPresent = edges.iterator().hasNext();
    klassMap.put(ICategoryInformationModel.IS_LAST_NODE, !isChildPresent);
    klassesList.add(klassMap);
    if(isChildPresent && shouldFetchChildren) {
      children = getChildernData(klassNode, getAllChildren, new HashMap<>(), from, size);
    }
    klassMap.put(IConfigEntityTreeInformationModel.CHILDREN, children);
  }
  
  private List<Map<String, Object>> getChildernData(Vertex klassNode, Boolean getAllChildren,
      Map<String, List<Map<String, Object>>> taxonomyIdVsChildrenList, int from, int size) throws Exception
  {
    List<Map<String, Object>> klassesList = new ArrayList<>();
    OrientGraph graph = UtilClass.getGraph();
    
    Iterable<Vertex> ChildNodes = graph
        .command(new OCommandSQL(
            "select expand(in('" + RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF
                + "')) from " + klassNode.getId() + " order by "
                + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " asc "  + "SKIP " + from + " LIMIT " + size))
        .execute();
    for (Vertex childNode : ChildNodes) {
      fillKlassesAndTaxonomies(klassesList, childNode, getAllChildren, getAllChildren, from, size);
    }
    return klassesList;
  }
  
  /*protected List<String> fillTargetIdsForRelationship(Map<String, Object> requestMap, String targetId)
  -      throws Exception
  -  {
  -    String sideId = (String) requestMap.get(IConfigDetailsForRelationshipQuicklistRequestModel.SIDE_ID);
  -    List<String> targetIds = new ArrayList<>();
  -    String relationshipId = (String) requestMap.get(IConfigDetailsForRelationshipQuicklistRequestModel.RELATIONSHIP_ID);
  -    Vertex relationshipNode = UtilClass.getVertexByIndexedId(relationshipId, VertexLabelConstants.ROOT_RELATIONSHIP);
  -    Iterable<Vertex> kRNodes = relationshipNode.getVertices(Direction.IN, RelationshipLabelConstants.HAS_PROPERTY);
  -    for (Vertex kRNode : kRNodes) {
  -      if(sideId!=null && sideId.equals(UtilClass.getCId(kRNode))) {
  -        continue; //self side
  -      }
  -      Boolean foundTarget = false;
  -      Iterable<Vertex> klassNodes = kRNode.getVertices(Direction.IN, RelationshipLabelConstants.HAS_KLASS_PROPERTY);
  -      for (Vertex klassNode : klassNodes) {
  -        String klassId = UtilClass.getCId(klassNode);
  -        targetIds.add(klassId);
  -        if(klassId.equals(targetId)) {
  -          foundTarget = true;
  -        }
  -      }
  -      if(foundTarget) {
  -        break;
  -      }
  -      targetIds.clear();
  -    }
  -    return targetIds;
  -  }*/
}
