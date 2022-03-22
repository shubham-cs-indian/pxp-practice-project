package com.cs.config.strategy.plugin.usecase.klass;

import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataEntityResponseModel;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
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
public class GetKlassesListByNatureType extends AbstractOrientPlugin {
  
  public GetKlassesListByNatureType(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetKlassesListByNatureType/*" };
  }
  
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    Long from = Long.valueOf(requestMap.get(IConfigGetAllRequestModel.FROM)
        .toString());
    Long size = Long.valueOf(requestMap.get(IConfigGetAllRequestModel.SIZE)
        .toString());
    String searchText = requestMap.get(IConfigGetAllRequestModel.SEARCH_TEXT)
        .toString();
    String searchColumn = requestMap.get(IConfigGetAllRequestModel.SEARCH_COLUMN)
        .toString();
    String baseType = (String) requestMap.get(IConfigGetAllRequestModel.BASETYPE);
    
    StringBuilder searchQuery = EntityUtil.getSearchQuery(searchText, searchColumn);
    Boolean isAbstract = (Boolean) requestMap.get(IConfigGetAllRequestModel.IS_ABSTRACT);
    
    List<String> types = (List<String>) requestMap.get(IConfigGetAllRequestModel.TYPES);
    StringBuilder typeQuery = UtilClass.getTypeQueryWithoutANDOperator(types, IKlass.NATURE_TYPE);
    OrientGraph graph = UtilClass.getGraph();
    Map<String, Object> returnMap = new HashMap<String, Object>();
    List<Map<String, Object>> KlassesList = new ArrayList<Map<String, Object>>();
    if (baseType != null) {
      String basetypeQuery = IKlass.TYPE + " = '" + baseType + "' and ";
      typeQuery = typeQuery.insert(0, basetypeQuery);
    }
    StringBuilder isAbstractQuery = new StringBuilder();
    if (isAbstract != null) {
      isAbstractQuery.append(IKlass.IS_ABSTRACT + " = " + isAbstract);
    }
    StringBuilder conditionQuery = EntityUtil.getConditionQuery(searchQuery, typeQuery,
        isAbstractQuery);
    String query = "select from " + VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS + conditionQuery
        + " order by " + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY)
        + " asc skip " + from + " limit " + size;
    
    String countQuery = "select count(*) from " + VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS
        + conditionQuery;
    
    Long totalCount = EntityUtil.executeCountQueryToGetTotalCount(countQuery);
    
    Iterable<Vertex> iterable = graph.command(new OCommandSQL(query))
        .execute();
    
    for (Vertex klassNode : iterable) {
      Map<String, Object> klassEntityMap = new HashMap<String, Object>();
      klassEntityMap.put(IKlass.ID, klassNode.getProperty(CommonConstants.CODE_PROPERTY));
      klassEntityMap.put(IKlass.ICON, klassNode.getProperty(IKlass.ICON));
      klassEntityMap.put(IKlass.LABEL, UtilClass.getValueByLanguage(klassNode, IKlass.LABEL));
      klassEntityMap.put(IKlass.TYPE, klassNode.getProperty(IKlass.NATURE_TYPE));
      klassEntityMap.put(IConfigEntityInformationModel.CODE, klassNode.getProperty(IKlass.CODE));
      
      KlassesList.add(klassEntityMap);
    }
    returnMap.put(IGetConfigDataEntityResponseModel.LIST, KlassesList);
    returnMap.put(IGetConfigDataEntityResponseModel.FROM, from);
    returnMap.put(IGetConfigDataEntityResponseModel.SIZE, size);
    returnMap.put(IGetConfigDataEntityResponseModel.TOTAL_COUNT, totalCount);
    return returnMap;
  }
}
