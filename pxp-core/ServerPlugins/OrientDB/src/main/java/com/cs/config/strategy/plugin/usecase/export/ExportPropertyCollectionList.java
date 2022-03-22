package com.cs.config.strategy.plugin.usecase.export;

import com.cs.config.strategy.plugin.usecase.propertycollection.util.PropertyCollectionUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.entity.propertycollection.IPropertyCollection;
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

public class ExportPropertyCollectionList extends AbstractOrientPlugin {
  
  public ExportPropertyCollectionList(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|ExportPropertyCollectionList/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    HashMap<String, Object> responseMap = new HashMap<>();
    List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
    
    List<String> itemCodes = (List<String>) requestMap.get("itemCodes");
    
    Iterable<Vertex> resultIterable = null;
    StringBuilder codeQuery = UtilClass.getTypeQueryWithoutANDOperator(itemCodes,
        IPropertyCollection.CODE);
    
    StringBuilder condition = EntityUtil.getConditionQuery(codeQuery);
    String query = "select from " + VertexLabelConstants.PROPERTY_COLLECTION + condition;
    
    resultIterable = graph.command(new OCommandSQL(query))
        .execute();
    
    for (Vertex propertyCollection : resultIterable) {
      HashMap<String, Object> propertyCollectionMap = new HashMap<String, Object>();
      propertyCollectionMap = (HashMap<String, Object>) PropertyCollectionUtils
          .prepareExportPropertyCollection(propertyCollection);
      list.add(propertyCollectionMap);
    }
    
    responseMap.put("list", list);
    
    return responseMap;
  }
}
