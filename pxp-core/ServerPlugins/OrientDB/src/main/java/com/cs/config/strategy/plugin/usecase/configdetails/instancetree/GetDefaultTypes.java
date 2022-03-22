package com.cs.config.strategy.plugin.usecase.configdetails.instancetree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.config.strategy.plugin.usecase.globalpermission.util.GlobalPermissionUtils;
import com.cs.config.strategy.plugin.usecase.role.util.RoleUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.configuration.base.IConfigMasterEntity;
import com.cs.core.config.interactor.entity.globalpermissions.IGlobalPermission;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.instancetree.IDefaultTypesRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetDefaultTypesResponseModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

@SuppressWarnings("unchecked")
public class GetDefaultTypes extends AbstractOrientPlugin {
  
  protected final static List<String> FIELDS_TO_FETCH = Arrays.asList(
      IGetDefaultTypesResponseModel.ICON, IGetDefaultTypesResponseModel.LABEL,
      IGetDefaultTypesResponseModel.TYPE, IGetDefaultTypesResponseModel.CODE,
      IGetDefaultTypesResponseModel.ID, IGetDefaultTypesResponseModel.NATURE_TYPE);
  
  public GetDefaultTypes(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetDefaultTypes/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<Map<String, Object>> childrenList = new ArrayList<>();
    
    fillKlassChildrensWithPermission(requestMap, childrenList);
    
    Map<String, Object> returnMap = new HashMap<>();
    returnMap.put("list", childrenList);
    return returnMap;
  }
  
  private Set<String> managePermissionDetailsForInstanceTree(String userId,
      Set<String> defaultKlassIds) throws Exception
  {
    Vertex roleNode = RoleUtils.getRoleFromUser(userId);
    
    Set<String> klassIdsHavingRP = GlobalPermissionUtils.getKlassIdsHavingReadPermission(roleNode);
    Set<String> klassIdsHavingCP = getKlassIdsHavingCreatePermission(klassIdsHavingRP, roleNode,
        defaultKlassIds);
    
    return klassIdsHavingCP;
  }
  
  private Set<String> getKlassIdsHavingCreatePermission(Set<String> klassIdsHavingRP,
      Vertex roleNode, Set<String> defaultKlassIds) throws Exception
  {
    Set<String> klassIdsHavingCP = new HashSet<String>();
    String roleId = UtilClass.getCId(roleNode);
    
    if (klassIdsHavingRP.isEmpty()) {
      klassIdsHavingRP = defaultKlassIds;
    }
    else {
      klassIdsHavingRP.retainAll(defaultKlassIds);
    }
    
    for (String klassId : klassIdsHavingRP) {
      Map<String, Object> klassAndTaxonomyPermission = GlobalPermissionUtils
          .getKlassAndTaxonomyPermission(klassId, roleId);
      Boolean canCreate = (Boolean) klassAndTaxonomyPermission.get(IGlobalPermission.CAN_CREATE);
      if (canCreate) {
        klassIdsHavingCP.add(klassId);
      }
    }
    return klassIdsHavingCP;
  }
  
  private void fillKlassChildrensWithPermission(Map<String, Object> requestMap,
      List<Map<String, Object>> childrenList) throws Exception
  {
    String userId = (String) requestMap.get(IDefaultTypesRequestModel.USER_ID);
    
    Set<String> defaultNatureKlassIds = getDefaultNatureKlasses(requestMap);
    
    Set<String> klassIdsHavingCP = managePermissionDetailsForInstanceTree(userId,
        defaultNatureKlassIds);
    
    if(!klassIdsHavingCP.isEmpty()) {
      List<String> standardKlassIds = (List<String>) requestMap
          .get(IDefaultTypesRequestModel.KLASS_IDS);
      String vertexType = getVertexType(standardKlassIds);
      String queryToAppend = getQueryParametersInString(requestMap);
      String query = "select from " + vertexType + " where " +
          CommonConstants.CODE_PROPERTY + " in " + EntityUtil.quoteIt(klassIdsHavingCP) + "";
      query += queryToAppend;

      Iterable<Vertex> resultIterable = UtilClass.getGraph()
          .command(new OCommandSQL(query))
          .execute();
      for (Vertex klassNode : resultIterable) {
        childrenList.add(UtilClass.getMapFromVertex(FIELDS_TO_FETCH, klassNode));
      }
    }
  }
  
  private String getVertexType(List<String> standardKlassIds) throws Exception
  {
    Vertex rootNode = UtilClass.getVertexByIndexedId(standardKlassIds.get(0), VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
    String vertexType = rootNode.getProperty(CommonConstants.ORIENTDB_CLASS_PROPERTY);
    return vertexType;
  }
  
  private Set<String> getDefaultNatureKlasses(Map<String, Object> requestMap)
      throws Exception
  {
    List<String> standardKlassIds = (List<String>) requestMap
        .get(IDefaultTypesRequestModel.KLASS_IDS);
    List<String> selectedTypes = (List<String>) requestMap.get(IDefaultTypesRequestModel.SELECTED_TYPES);
    String commaSeparatedRootNodeIds = getCommaSeparatedIds(standardKlassIds);
    String query = "select from(traverse in('Child_Of') from [" + commaSeparatedRootNodeIds
        + "] strategy BREADTH_FIRST) where " + IKlass.IS_DEFAULT_CHILD + "= true"
        + " AND (isAbstract = \"false\" or isAbstract is null)";
    
    if(!selectedTypes.isEmpty()) {
      query += " AND " + CommonConstants.CODE_PROPERTY + " in " + EntityUtil.quoteIt(selectedTypes) + "";
    }
    
    Iterable<Vertex> resultIterable = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    Set<String> defaultNatureKlassIds = new HashSet<>();
    for (Vertex klassNode : resultIterable) {
      defaultNatureKlassIds.add(UtilClass.getCodeNew(klassNode));
    }
    return defaultNatureKlassIds;
  }

  private String getCommaSeparatedIds(List<String> ids) throws Exception
  {
    StringBuilder commaSeparatedRootNodeIds = new StringBuilder();
    int idsSize = ids.size();
    for (String id : ids) {
      idsSize--;
      Vertex rootNode = UtilClass.getVertexByIndexedId(id,
          VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
      commaSeparatedRootNodeIds.append(rootNode.getId());
      if(idsSize != 0) {
        commaSeparatedRootNodeIds.append(", ");
      }
    }
    return commaSeparatedRootNodeIds.toString();
  }
  
  private String getQueryParametersInString(Map<String, Object> requestMap)
  {
    Long from = Long.parseLong(requestMap.get(IDefaultTypesRequestModel.FROM).toString());
    Long size = Long.parseLong(requestMap.get(IDefaultTypesRequestModel.SIZE).toString());
    String sortBy = (String) requestMap.get(IDefaultTypesRequestModel.SORT_BY);
    String sortOrder = (String) requestMap.get(IDefaultTypesRequestModel.SORT_ORDER);
    String searchText = (String) requestMap.get(IDefaultTypesRequestModel.SEARCH_TEXT);
    
    String queryParameters = "";
    if (!UtilClass.isStringNullOrEmpty(searchText)) {
      queryParameters += " AND " + EntityUtil.getLanguageConvertedField(IConfigMasterEntity.LABEL)
          + " like '%" + searchText + "%'";
    }
    if (!UtilClass.isStringNullOrEmpty(sortBy)) {
      sortBy = EntityUtil.getLanguageConvertedField(sortBy);
      if (UtilClass.isStringNullOrEmpty(sortOrder)) {
        sortOrder = "asc";
      }
      queryParameters += " order by " + sortBy + " " + sortOrder + " skip " + from;
      if (size != 0) {
        queryParameters += " limit " + size;
      }
    }
    return queryParameters;
  }
}
