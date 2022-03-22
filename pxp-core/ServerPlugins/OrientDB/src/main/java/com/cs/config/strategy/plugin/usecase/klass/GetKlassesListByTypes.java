package com.cs.config.strategy.plugin.usecase.klass;

import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataEntityResponseModel;
import com.cs.core.config.interactor.model.grid.IGetKlassesListRequestModel;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class GetKlassesListByTypes extends AbstractOrientPlugin {
  
  public GetKlassesListByTypes(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetKlassesListByTypes/*" };
  }
  
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    Long from = Long.valueOf(requestMap.get(IGetKlassesListRequestModel.FROM)
        .toString());
    Long size = Long.valueOf(requestMap.get(IGetKlassesListRequestModel.SIZE)
        .toString());
    String searchText = requestMap.get(IGetKlassesListRequestModel.SEARCH_TEXT)
        .toString();
    String searchColumn = requestMap.get(IGetKlassesListRequestModel.SEARCH_COLUMN)
        .toString();
    String baseType = (String) requestMap.get(IGetKlassesListRequestModel.BASETYPE);
    
    StringBuilder searchQuery = EntityUtil.getSearchQuery(searchText, searchColumn);
    Map<String, List<String>> typesToFilter = (Map<String, List<String>>) requestMap
        .get(IGetKlassesListRequestModel.TYPES_TO_FILTER);
    StringBuilder typeQuery = new StringBuilder();
    for (String filterField : typesToFilter.keySet()) {
      List<String> valuesToFilter = typesToFilter.get(filterField);
      if (typeQuery.length() == 0) {
        typeQuery.append(UtilClass.getTypeQueryWithoutANDOperator(valuesToFilter, filterField));
      }
      else {
        typeQuery.append(UtilClass.getTypeQuery(valuesToFilter, filterField));
      }
    }
    
    OrientGraph graph = UtilClass.getGraph();
    Map<String, Object> returnMap = new HashMap<String, Object>();
    List<Map<String, Object>> klassesList = new ArrayList<Map<String, Object>>();
    StringBuilder baseTypeQuery = new StringBuilder();
    if (baseType != null) {
      baseTypeQuery.append(IKlass.TYPE + " = " + EntityUtil.quoteIt(baseType));
    }
    
    StringBuilder conditionQuery = EntityUtil.getConditionQuery(searchQuery, typeQuery,
        baseTypeQuery);
    String query = "select from " + VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS + conditionQuery
        + " order by " + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY)
        + " asc skip " + from + " limit " + size;
    
    String countQuery = "select count(*) from " + VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS
        + conditionQuery;
    
    Long totalCount = EntityUtil.executeCountQueryToGetTotalCount(countQuery);
    
    Iterable<Vertex> klasses = graph.command(new OCommandSQL(query))
        .execute();
    
    for (Vertex klass : klasses) {
      Map<String, Object> klassEntityMap = new HashMap<String, Object>();
      klassEntityMap.put(IKlass.ID, klass.getProperty(CommonConstants.CODE_PROPERTY));
      klassEntityMap.put(IKlass.ICON, klass.getProperty(IKlass.ICON));
      klassEntityMap.put(IKlass.LABEL, UtilClass.getValueByLanguage(klass, IKlass.LABEL));
      Boolean isNature = klass.getProperty(IKlass.IS_NATURE);
      klassEntityMap.put(IKlass.TYPE,
          isNature ? klass.getProperty(IKlass.NATURE_TYPE) : klass.getProperty(IKlass.TYPE));
      klassEntityMap.put(IConfigEntityInformationModel.CODE, klass.getProperty(IKlass.CODE));
      
      klassesList.add(klassEntityMap);
    }
    returnMap.put(IGetConfigDataEntityResponseModel.LIST, klassesList);
    returnMap.put(IGetConfigDataEntityResponseModel.FROM, from);
    returnMap.put(IGetConfigDataEntityResponseModel.SIZE, size);
    returnMap.put(IGetConfigDataEntityResponseModel.TOTAL_COUNT, totalCount);
    return returnMap;
  }
}
