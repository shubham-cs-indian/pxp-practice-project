package com.cs.config.strategy.plugin.usecase.attribute;

import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IAttributeInfoModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetAttributeList extends AbstractOrientPlugin {
  
  public GetAttributeList(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> map) throws Exception
  {
    
    String mode = (String) map.get("mode");
    
    OrientGraph graph = UtilClass.getGraph();
    
    List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
    
    Iterable<Vertex> resultIterable = null;
    
    if (mode.equals("pim")) {
      resultIterable = graph
          .command(new OCommandSQL("select from " + VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE
              + " where forPim = true order by "
              + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " asc"))
          .execute();
    }
    else if (mode.equals("mam")) {
      resultIterable = graph
          .command(new OCommandSQL("select from " + VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE
              + " where forMam = true order by "
              + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " asc"))
          .execute();
    }
    else if (mode.equals("target")) {
      resultIterable = graph
          .command(new OCommandSQL("select from " + VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE
              + " where forTarget = true order by "
              + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " asc"))
          .execute();
    }
    else if (mode.equals("editorial")) {
      resultIterable = graph
          .command(new OCommandSQL("select from attribute where forEditorial = true order by "
              + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " asc"))
          .execute();
    }
    else {
      resultIterable = graph
          .command(new OCommandSQL(
              "select from " + VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE + " order by "
                  + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " asc"))
          .execute();
    }
    
    for (Vertex attributeNode : resultIterable) {
      HashMap<String, Object> attributeIdNameMap = new HashMap<String, Object>();
      attributeIdNameMap.put(CommonConstants.ID_PROPERTY,
          attributeNode.getProperty(CommonConstants.CODE_PROPERTY));
      attributeIdNameMap.put(CommonConstants.LABEL_PROPERTY,
          UtilClass.getValueByLanguage(attributeNode, CommonConstants.LABEL_PROPERTY));
      attributeIdNameMap.put(CommonConstants.TYPE_PROPERTY,
          attributeNode.getProperty(CommonConstants.TYPE_PROPERTY));
      attributeIdNameMap.put(IAttributeInfoModel.DEFAULT_UNIT,
          attributeNode.getProperty(IAttributeInfoModel.DEFAULT_UNIT));
      attributeIdNameMap.put(IAttributeInfoModel.PRECISION,
          attributeNode.getProperty(IAttributeInfoModel.PRECISION));
      attributeIdNameMap.put(IAttributeInfoModel.VALIDATOR,
          attributeNode.getProperty(IAttributeInfoModel.VALIDATOR));
      
      list.add(attributeIdNameMap);
    }
    
    HashMap<String, Object> returnMap = new HashMap<String, Object>();
    returnMap.put("list", list);
    
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetAttributeList/*" };
  }
}
