package com.cs.config.strategy.plugin.usecase.attribute;

import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.model.configdetails.IGetFilterAndSortDataRequestModel;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.process.IGetTranslatableAttributesModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GetTranslatableAttributes extends AbstractOrientPlugin {
  
  public GetTranslatableAttributes(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetTranslatableAttributes/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String moduleId = (String) requestMap.get(IGetFilterAndSortDataRequestModel.MODULE_ID);
    String systemLevelId = moduleId != null ? EntityUtil.getSystemLevelIdByModuleId(moduleId)
        : null;
    String availabilityQuery = " AND ( '" + systemLevelId
        + "' in availability Or availability.size() = 0 )";
    Set<String> translatableAttributes = new HashSet<>();
    Set<String> attributes = new HashSet<>();
    attributes.addAll((Collection<? extends String>) requestMap
        .get(IGetTranslatableAttributesModel.ATTRIBUTE_IDS));
    String onlyNecessaryAttributesQuery = " AND " + CommonConstants.CODE_PROPERTY + " in "
        + EntityUtil.quoteIt(attributes);
    
    String query = " select " + CommonConstants.CODE_PROPERTY + " from "
        + VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE + " where " + IAttribute.IS_TRANSLATABLE
        + " = true ";
    if (systemLevelId != null && !systemLevelId.trim()
        .equals("")) {
      query += availabilityQuery;
    }
    query += onlyNecessaryAttributesQuery;
    Iterable<Vertex> translatableAttributesResult = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    for (Vertex translatableAttribute : translatableAttributesResult) {
      translatableAttributes.add(UtilClass.getCodeNew(translatableAttribute));
    }
    Map<String, Object> responseMap = new HashMap<String, Object>();
    responseMap.put(IIdsListParameterModel.IDS, translatableAttributes);
    return responseMap;
  }
}
