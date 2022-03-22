package com.cs.config.strategy.plugin.usecase.klass.gridview;

import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.configuration.base.IConfigMasterPropertyEntity;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetContentGridPropertiesList extends AbstractOrientPlugin {
  
  public GetContentGridPropertiesList(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetContentGridPropertiesList/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> responseMap = new HashMap<>();
    fillGridEdiableProperties(responseMap, requestMap);
    return responseMap;
  }
  
  private void fillGridEdiableProperties(Map<String, Object> responseMap,
      Map<String, Object> requestMap)
  {
    List<Map<String, Object>> allowedProperties = new ArrayList<>();
    String searchText = requestMap.get(IConfigGetAllRequestModel.SEARCH_TEXT)
        .toString();
    String searchColumn = requestMap.get(IConfigGetAllRequestModel.SEARCH_COLUMN)
        .toString();
    
    String from = requestMap.get(IConfigGetAllRequestModel.FROM)
        .toString();
    String size = requestMap.get(IConfigGetAllRequestModel.SIZE)
        .toString();
    
    String sortBy = requestMap.get(IConfigGetAllRequestModel.SORT_BY)
        .toString();
    String sortOrder = requestMap.get(IConfigGetAllRequestModel.SORT_ORDER)
        .toString();
    
    StringBuilder isGridEditableQuery = new StringBuilder(IAttribute.IS_GRID_EDITABLE + " = true ");
    StringBuilder searchQuery = EntityUtil.getSearchQuery(searchText, searchColumn);
    
    StringBuilder conditionQuery = EntityUtil.getConditionQuery(isGridEditableQuery, searchQuery);
    
    String query = "SELECT FROM " + EntityUtil.getVertexLabelByEntityType(CommonConstants.PROPERTY)
        + conditionQuery + " ORDER BY " + EntityUtil.getLanguageConvertedField(sortBy) + " "
        + sortOrder + " SKIP " + from + " LIMIT " + size;
    
    Iterable<Vertex> vertices = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    for (Vertex entityNode : vertices) {
      String entityId = UtilClass.getCodeNew(entityNode);
      if (CommonConstants.PROPERTIES_TO_EXCLUDE_FOR_GRID.contains(entityId)) {
        continue;
      }
      
      String entityType = ((String) entityNode.getProperty("@class")).toLowerCase();
      
      Map<String, Object> entityMap = new HashMap<>();
      entityMap.put(IConfigEntityInformationModel.ID, entityId);
      entityMap.put(IConfigEntityInformationModel.CODE,
          (String) entityNode.getProperty(CommonConstants.CODE_PROPERTY));
      entityMap.put(IConfigEntityInformationModel.LABEL,
          UtilClass.getValueByLanguage(entityNode, IConfigMasterPropertyEntity.LABEL));
      entityMap.put(IConfigEntityInformationModel.TYPE, entityType);
      allowedProperties.add(entityMap);
    }
    responseMap.put(IListModel.LIST, allowedProperties);
  }
}
