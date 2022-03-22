package com.cs.config.strategy.plugin.usecase.export;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.relationship.util.RelationshipUtils;
import com.cs.config.strategy.plugin.usecase.tabs.utils.TabUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.model.relationship.IGetAllRelationshipsResponseModel;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class ExportRelationshipList extends AbstractOrientPlugin {
  
  public ExportRelationshipList(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<String> itemCodes = (List<String>) requestMap.get("itemCodes");
    
    StringBuilder codeQuery = UtilClass.getTypeQueryWithoutANDOperator(itemCodes, IAttribute.CODE);
    StringBuilder condition = EntityUtil.getConditionQuery(codeQuery);
    
    String query = "select from " + VertexLabelConstants.ENTITY_TYPE_RELATIONSHIP + condition;
    
    List<Map<String, Object>> list = executeQueryAndPrepareResponse(query);
    
    Map<String, Object> responseMap = new HashMap<>();
    responseMap.put(IGetAllRelationshipsResponseModel.LIST, list);
    
    return responseMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|ExportRelationshipList/*" };
  }
  
  private List<Map<String, Object>> executeQueryAndPrepareResponse(String query) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    Iterable<Vertex> searchResults = graph.command(new OCommandSQL(query))
        .execute();
    List<Map<String, Object>> relationshipsToReturn = new ArrayList<>();
    for (Vertex relationshipNode : searchResults) {
      Map<String, Object> relationshipMap = RelationshipUtils
          .getRelationshipMapWithContext(relationshipNode);
      
      Map<String, Object> referencedTab = TabUtils.getMapFromConnectedTabNode(relationshipNode,
          Arrays.asList(CommonConstants.CODE_PROPERTY));
      relationshipMap.put(CommonConstants.TAB, referencedTab.get(CommonConstants.CODE_PROPERTY));
      
      RelationshipUtils.populatePropetiesInfo(relationshipNode, relationshipMap);
      RelationshipUtils.prepareRelationshipSideInfoForExport(relationshipMap);
      relationshipsToReturn.add(relationshipMap);
    }
    return relationshipsToReturn;
  }
}
