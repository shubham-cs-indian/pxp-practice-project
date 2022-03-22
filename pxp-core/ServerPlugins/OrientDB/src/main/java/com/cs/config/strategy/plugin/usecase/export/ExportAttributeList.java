package com.cs.config.strategy.plugin.usecase.export;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.attribute.util.AttributeUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IAttributeInfoModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class ExportAttributeList extends AbstractOrientPlugin {
  
  public static final String ALLOWED_RTE_ICONS = "allowedRTEIcons";
  public static final String ALLOWED_STYLES    = "allowedStyles";
  
  public ExportAttributeList(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    
    OrientGraph graph = UtilClass.getGraph();
    HashMap<String, Object> responseMap = new HashMap<>();
    List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
    
    List<String> itemCodes = (List<String>) requestMap.get("itemCodes");
    
    Iterable<Vertex> resultIterable = null;
    StringBuilder codeQuery = UtilClass.getTypeQueryWithoutANDOperator(itemCodes, IAttribute.CODE);
    
    StringBuilder condition = EntityUtil.getConditionQuery(codeQuery);
    String query = "select from " + VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE + condition
        + " order by " + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY)
        + " asc";
    
    resultIterable = graph.command(new OCommandSQL(query))
        .execute();
    
    for (Vertex attributeNode : resultIterable) {
      HashMap<String, Object> attributeMap = new HashMap<String, Object>();
      attributeMap = (HashMap<String, Object>) AttributeUtils.getAttributeMap(attributeNode);
      prepareAttributeDataFromValidator(attributeMap);
      list.add(attributeMap);
    }
    
    responseMap.put("list", list);
    
    return responseMap;
  }
  
  private void prepareAttributeDataFromValidator(HashMap<String, Object> attributeMap)
  {
    Map<String, Object> validator = (Map<String, Object>) attributeMap.get(IAttributeInfoModel.VALIDATOR);
    if(validator != null) {
      List<String> allowedStyles = (List<String>) validator.get(ALLOWED_RTE_ICONS);
      if(allowedStyles == null)
        allowedStyles = new ArrayList<>();
      attributeMap.put("allowedStyles", allowedStyles);
    }
  }

  @Override
  public String[] getNames()
  {
    return new String[] { "POST|ExportAttributeList/*" };
  }
}
