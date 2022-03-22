package com.cs.config.strategy.plugin.usecase.downloadtracker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.interactor.constants.application.Seperators;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.dam.config.interactor.model.downloadtracker.IGetLabelsByIdsRequestModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

public class GetLabelsByIds extends AbstractOrientPlugin  {
  
  public GetLabelsByIds(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }

  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetLabelsByIds/*" };
  }

  @Override
  @SuppressWarnings("unchecked")
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> returnMap = new HashMap<>();
    List<Map<String, Object>> returnList = new ArrayList<>();
    List<String> ids = (List<String>) requestMap.get(IGetLabelsByIdsRequestModel.IDS);
    String vertexType = (String) requestMap.get(IGetLabelsByIdsRequestModel.ENTITY_TYPE);
    String searchText = (String) requestMap.get(IGetLabelsByIdsRequestModel.SEARCH_TEXT);
    Long from = (Long.valueOf(requestMap.get(IGetLabelsByIdsRequestModel.FROM).toString()));
    Long size = (Long.valueOf(requestMap.get(IGetLabelsByIdsRequestModel.SIZE).toString()));
    String columnName = CommonConstants.LABEL_PROPERTY + Seperators.FIELD_LANG_SEPERATOR  + UtilClass.getLanguage().getUiLanguage();

    StringBuilder query = new StringBuilder("SELECT from " + vertexType + " where code IN "+ EntityUtil.quoteIt(ids));
    if (searchText != null && !searchText.isBlank()) {
      query.append(" and " + columnName + ".toLowerCase() like '%" + searchText.toLowerCase() + "%'");
    }
    query.append(" order by " + columnName + " asc skip " + from + " limit " + size);

    Iterable<Vertex> vertices = UtilClass.getGraph().command(new OCommandSQL(query.toString())).execute();

    vertices.forEach( vertex -> {
      Map<String, Object> vertexMap = new HashMap<>();
      List<String> fieldsToFetch = Arrays.asList(CommonConstants.CODE_PROPERTY, columnName);
      Map<String, Object> mapFromVertex = UtilClass.getMapFromVertex(fieldsToFetch, vertex);
      vertexMap.put(IIdLabelCodeModel.ID, mapFromVertex.get(CommonConstants.CODE_PROPERTY));
      vertexMap.put(IIdLabelCodeModel.CODE, mapFromVertex.get(CommonConstants.CODE_PROPERTY));
      vertexMap.put(IIdLabelCodeModel.LABEL, mapFromVertex.get(columnName));
      returnList.add(vertexMap);
    });

    returnMap.put(IListModel.LIST, returnList);
    return returnMap;
  }

}
