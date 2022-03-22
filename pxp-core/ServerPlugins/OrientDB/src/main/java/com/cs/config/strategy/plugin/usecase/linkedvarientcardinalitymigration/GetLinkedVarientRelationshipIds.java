package com.cs.config.strategy.plugin.usecase.linkedvarientcardinalitymigration;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetLinkedVarientRelationshipIds extends AbstractOrientPlugin {
  
  public GetLinkedVarientRelationshipIds(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetLinkedVarientRelationshipIds/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> returnMap = new HashMap<String, Object>();
    String linkedVarientNatureRelationshipQuery = getAllNatureRelationshipOfLVQuery();
    Iterable<Vertex> linkedVarientNatureRelationshipVertexs = UtilClass.getGraph()
        .command(new OCommandSQL(linkedVarientNatureRelationshipQuery))
        .execute();
    List<String> relationshipIds = new ArrayList<String>();
    for (Vertex linkedVarientNatureRelationshipVertex : linkedVarientNatureRelationshipVertexs) {
      Map<String, Object> relationshipMap = linkedVarientNatureRelationshipVertex
          .getProperty(CommonConstants.RELATIONSHIP_SIDE_2);
      String elementId = (String) relationshipMap.get("elementId");
      relationshipIds.add(elementId);
    }
    returnMap.put(IIdsListParameterModel.IDS, relationshipIds);
    return returnMap;
  }
  
  protected String getAllNatureRelationshipOfLVQuery()
  {
    String natureRelationshipOfLV = "select from " + VertexLabelConstants.NATURE_RELATIONSHIP
        + " where relationshipType in '" + CommonConstants.PRODUCT_VARIANT_RELATIONSHIP + "'";
    return natureRelationshipOfLV;
  }
}
