package com.cs.config.strategy.plugin.usecase.entity.hidden;

import com.cs.config.strategy.plugin.usecase.translations.utils.TranslationsUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.translations.IGetTranslationsRequestModel;
import com.cs.core.config.interactor.model.translations.IGetTranslationsResponseModel;
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

public class ExportSystemStaticTranslation extends AbstractOrientPlugin {
  
  public ExportSystemStaticTranslation(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|ExportSystemStaticTranslation/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Long from = Long.valueOf(requestMap.get(IGetTranslationsRequestModel.FROM)
        .toString());
    Long size = Long.valueOf(requestMap.get(IGetTranslationsRequestModel.SIZE)
        .toString());
    String sortBy = (String) requestMap.get(IGetTranslationsRequestModel.SORT_BY);
    String sortOrder = (String) requestMap.get(IGetTranslationsRequestModel.SORT_ORDER);
    String sortLang = (String) requestMap.get(IGetTranslationsRequestModel.SORT_LANGUAGE);
    
    sortOrder = TranslationsUtils.checkAndGetSortOrder(sortOrder);
    
    sortBy = TranslationsUtils.prepareSearchOrSortField(sortBy, sortLang);
    
    String query = "select from " + VertexLabelConstants.UI_TRANSLATIONS + " order by " + sortBy
        + " " + sortOrder + " skip " + from + " limit " + size;
    List<Map<String, Object>> list = executeQueryAndPrepareResponse(query);
    
    String countQuery = "select count(code) from " + VertexLabelConstants.UI_TRANSLATIONS;
    
    Long count = EntityUtil.executeCountQueryToGetTotalCount(countQuery);
    
    Map<String, Object> responseMap = new HashMap<>();
    responseMap.put(IGetTranslationsResponseModel.DATA, list);
    responseMap.put(IGetTranslationsResponseModel.COUNT, count);
    return responseMap;
  }
  
  private List<Map<String, Object>> executeQueryAndPrepareResponse(String query) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    Iterable<Vertex> vertices = graph.command(new OCommandSQL(query))
        .execute();
    List<Map<String, Object>> returnList = new ArrayList<>();
    for (Vertex node : vertices) {
      returnList.add(getMapFromNode(node));
    }
    
    return returnList;
  }
  
  public static Map<String, Object> getMapFromNode(Vertex node)
  {
    Map<String, Object> returnMap = new HashMap<String, Object>();
    for (String key : node.getPropertyKeys()) {
      Object value = node.getProperty(key);
      
      if (key.equals(CommonConstants.CODE_PROPERTY)) {
        returnMap.put(CommonConstants.ID_PROPERTY, value);
        continue;
      }
      else {
        returnMap.put(key, value);
      }
    }
    
    return returnMap;
  }
}
