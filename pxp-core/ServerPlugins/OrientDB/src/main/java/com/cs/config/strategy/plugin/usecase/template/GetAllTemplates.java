package com.cs.config.strategy.plugin.usecase.template;

import com.cs.config.strategy.plugin.usecase.template.util.CustomTemplateUtil;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.model.customtemplate.IGetCustomTemplateModel;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.template.IGetGridTemplatesResponseModel;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetAllTemplates extends AbstractOrientPlugin {
  
  public GetAllTemplates(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Map<String, Object> execute(Map<String, Object> requestMap) throws Exception
  {
    Long from = Long.valueOf(requestMap.get(IConfigGetAllRequestModel.FROM)
        .toString());
    Long size = Long.valueOf(requestMap.get(IConfigGetAllRequestModel.SIZE)
        .toString());
    String sortBy = requestMap.get(IConfigGetAllRequestModel.SORT_BY)
        .toString();
    String sortOrder = requestMap.get(IConfigGetAllRequestModel.SORT_ORDER)
        .toString();
    String searchText = requestMap.get(IConfigGetAllRequestModel.SEARCH_TEXT)
        .toString();
    String searchColumn = requestMap.get(IConfigGetAllRequestModel.SEARCH_COLUMN)
        .toString();
    
    searchColumn = EntityUtil.getLanguageConvertedField(searchColumn);
    sortBy = EntityUtil.getLanguageConvertedField(sortBy);
    
    StringBuilder searchQuery = EntityUtil.getSearchQuery(searchText, searchColumn);
    StringBuilder conditionQuery = EntityUtil.getConditionQuery(searchQuery);
    
    Long count = new Long(0);
    String query = "select from " + VertexLabelConstants.TEMPLATE + conditionQuery + " order by "
        + sortBy + " " + sortOrder + " skip " + from + " limit " + size;
    
    List<Map<String, Object>> list = new ArrayList<>();
    Map<String, Object> configDetails = new HashMap<>();
    list = executeQueryAndPrepareResponse(query, list, configDetails);
    String countQuery = "select count(*) from " + VertexLabelConstants.TEMPLATE + conditionQuery;
    count = EntityUtil.executeCountQueryToGetTotalCount(countQuery);
    
    Map<String, Object> responseMap = new HashMap<>();
    responseMap.put(IGetGridTemplatesResponseModel.LIST, list);
    responseMap.put(IGetGridTemplatesResponseModel.COUNT, count);
    responseMap.put(IGetGridTemplatesResponseModel.CONFIG_DETAILS, configDetails);
    return responseMap;
  }
  
  /**
   * @author Ajit
   * @param query
   * @param configDetails2
   * @return
   * @throws Exception
   */
  private List<Map<String, Object>> executeQueryAndPrepareResponse(String query,
      List<Map<String, Object>> templatesToReturn, Map<String, Object> configDetails)
      throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    Iterable<Vertex> searchResults = graph.command(new OCommandSQL(query))
        .execute();
    
    Map<String, Object> referencedPCs = new HashMap<>();
    Map<String, Object> referencedRelationships = new HashMap<>();
    Map<String, Object> referencedNatureRelationships = new HashMap<>();
    Map<String, Object> referencedContexts = new HashMap<>();
    configDetails.put(IGetCustomTemplateModel.REFERENCED_PROPERTY_COLLECTIONS, referencedPCs);
    configDetails.put(IGetCustomTemplateModel.REFERENCED_RELATIONSHIPS, referencedRelationships);
    configDetails.put(IGetCustomTemplateModel.REFERENCED_NATURE_RELATIONSHIPS,
        referencedNatureRelationships);
    configDetails.put(IGetCustomTemplateModel.REFERENCED_CONTEXT, referencedContexts);
    for (Vertex templateNode : searchResults) {
      CustomTemplateUtil.prepareTemplateResponseForGridTemplatesMap(templateNode, configDetails,
          templatesToReturn);
    }
    return templatesToReturn;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetAllTemplates/*" };
  }
}
