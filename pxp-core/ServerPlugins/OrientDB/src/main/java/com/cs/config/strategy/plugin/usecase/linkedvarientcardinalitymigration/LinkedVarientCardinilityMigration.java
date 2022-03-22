package com.cs.config.strategy.plugin.usecase.linkedvarientcardinalitymigration;

import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LinkedVarientCardinilityMigration extends AbstractOrientPlugin {
  
  public LinkedVarientCardinilityMigration(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
    // TODO Auto-generated constructor stub
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|LinkedVarientCardinilityMigration/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String linkedVarientNatureRelationshipQuery = getAllNatureRelationshipOfLVQuery();
    Iterable<Vertex> linkedVarientNatureRelationshipVertexs = UtilClass.getGraph()
        .command(new OCommandSQL(linkedVarientNatureRelationshipQuery))
        .execute();
    List<String> relationshipIds = new ArrayList<String>();
    for (Vertex linkedVarientNatureRelationshipVertex : linkedVarientNatureRelationshipVertexs) {
      setCardinality(linkedVarientNatureRelationshipVertex, CommonConstants.RELATIONSHIP_SIDE_1);
      Map<String, Object> relationshipMap = linkedVarientNatureRelationshipVertex
          .getProperty(CommonConstants.RELATIONSHIP_SIDE_1);
      String elementId = (String) relationshipMap.get("elementId");
      elementId = EntityUtil.quoteIt(elementId);
      relationshipIds.add(elementId);
    }
    String klassRelationshipQuery = getKlassRelationshipQuery(relationshipIds);
    Iterable<Vertex> klassRelationshipVertexs = UtilClass.getGraph()
        .command(new OCommandSQL(klassRelationshipQuery))
        .execute();
    for (Vertex klassRelationshipVertex : klassRelationshipVertexs) {
      setCardinality(klassRelationshipVertex, CommonConstants.RELATIONSHIP_SIDE_PROPERTY);
    }
    return requestMap;
  }
  
  protected String getAllNatureRelationshipOfLVQuery()
  {
    String natureRelationshipOfLV = "select from " + VertexLabelConstants.NATURE_RELATIONSHIP
        + " where relationshipType in '" + CommonConstants.PRODUCT_VARIANT_RELATIONSHIP + "'";
    return natureRelationshipOfLV;
  }
  
  protected void setCardinality(Vertex relationship, String key) throws Exception
  {
    Map<String, Object> relationshipMap = relationship.getProperty(key);
    relationshipMap.put(CommonConstants.CARDINALITY, CommonConstants.CARDINALITY_ONE);
    relationship.setProperty(key, relationshipMap);
  }
  
  protected String getKlassRelationshipQuery(List<String> relationshipIds)
  {
    String klassRelationshipsQuery = "select from " + VertexLabelConstants.KLASS_RELATIONSHIP
        + " where code in " + relationshipIds;
    return klassRelationshipsQuery;
  }
}
