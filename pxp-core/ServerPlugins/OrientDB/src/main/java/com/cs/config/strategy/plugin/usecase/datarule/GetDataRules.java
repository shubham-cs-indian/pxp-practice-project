package com.cs.config.strategy.plugin.usecase.datarule;

import com.cs.config.strategy.plugin.usecase.datarule.util.GetDataRuleUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.datarule.IBulkSaveDataRuleModel;
import com.cs.core.config.interactor.model.datarule.IDataRule;
import com.cs.core.config.interactor.model.datarule.IGetAllDataRulesResponseModel;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.variantcontext.IGetAllVariantContextsResponseModel;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetDataRules extends AbstractOrientPlugin {
  
  public GetDataRules(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
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
    Long count = new Long(0);
    
    StringBuilder searchQuery = EntityUtil.getSearchQuery(searchText, searchColumn);
    sortBy = EntityUtil.getLanguageConvertedField(sortBy);
    
    StringBuilder conditionQuery = EntityUtil.getConditionQuery(searchQuery);
    
    String query = "select from " + VertexLabelConstants.DATA_RULE + conditionQuery + " order by "
        + sortBy + " " + sortOrder + " skip " + from + " limit " + size;
    List<Map<String, Object>> list = executeQueryAndPrepareResponse(query);
    String countQuery = "select count(*) from " + VertexLabelConstants.DATA_RULE + conditionQuery;
    count = EntityUtil.executeCountQueryToGetTotalCount(countQuery);
    
    Map<String, Object> referencedLanguges = new HashMap<>();
    for (Map<String, Object> language : list) {
      List<String> languageCodes = (List<String>) language.get(IBulkSaveDataRuleModel.LANGUAGES);
      for (String languageCode : languageCodes) {
        if (!referencedLanguges.containsKey(languageCode)) {
          referencedLanguges.put(languageCode,
              GetDataRuleUtils.getReferencedLanguages(languageCode));
        }
      }
    }
    
    Map<String, Object> responseMap = new HashMap<>();
    responseMap.put(IGetAllVariantContextsResponseModel.LIST, list);
    responseMap.put(IGetAllVariantContextsResponseModel.COUNT, count);
    responseMap.put(IGetAllDataRulesResponseModel.LANGUAGES_INFO, referencedLanguges);
    return responseMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetDataRules/*" };
  }
  
  /**
   * @author Ajit
   * @param query
   * @return
   * @throws Exception
   */
  private List<Map<String, Object>> executeQueryAndPrepareResponse(String query) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    Iterable<Vertex> searchResults = graph.command(new OCommandSQL(query))
        .execute();
    List<Map<String, Object>> dataRuleList = new ArrayList<>();
    for (Vertex ruleNode : searchResults) {
      Map<String, Object> dataRuleMap = UtilClass.getMapFromVertex(
          Arrays.asList(CommonConstants.CODE_PROPERTY, IDataRule.LABEL, IDataRule.IS_STANDARD,
              IDataRule.CODE, IDataRule.TYPE, IDataRule.IS_LANGUAGE_DEPENDENT, IDataRule.LANGUAGES, 
              IDataRule.PHYSICAL_CATALOG_IDS),
          ruleNode);
      dataRuleList.add(dataRuleMap);
      List<String> languages = new ArrayList<>();
      dataRuleMap.put(IBulkSaveDataRuleModel.LANGUAGES, languages);
      GetDataRuleUtils.fillLanguages(ruleNode, languages);
    }
    return dataRuleList;
  }
}
