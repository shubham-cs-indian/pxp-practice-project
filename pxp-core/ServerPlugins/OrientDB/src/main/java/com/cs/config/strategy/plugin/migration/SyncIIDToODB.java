package com.cs.config.strategy.plugin.migration;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

import java.util.List;
import java.util.Map;

public class SyncIIDToODB extends AbstractOrientPlugin {
  
  private static final String VERTEX_TYPE = "vertexType";
  private static final String LIST        = "list";
  private static final String IID         = "iid";
  private static final String CODE        = "code";
  
  public SyncIIDToODB(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|SyncIIDToODB/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String vertexType = (String) requestMap.get(VERTEX_TYPE);
    List<Map<String, Object>> list = (List<Map<String, Object>>) requestMap.get(LIST);
    
    switch (vertexType) {
      case VertexLabelConstants.ENTITY_TAG:
      case VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE:
      case VertexLabelConstants.ENTITY_TYPE_RELATIONSHIP:
      case VertexLabelConstants.NATURE_RELATIONSHIP:
        updateIID(list, vertexType, "propertyIID");
        break;
      case VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS:
      case VertexLabelConstants.ROOT_KLASS_TAXONOMY:
        updateIID(list, vertexType, "classifierIID");
        break;
      case VertexLabelConstants.ENTITY_TYPE_USER:
        updateIID(list, vertexType, "userIID");
        break;
      case VertexLabelConstants.GOVERNANCE_RULES:
        updateCode(list, vertexType);
        break;
      default:
        break;
    }
    
    return null;
  }

  private void updateCode(List<Map<String, Object>> list, String vertexType) throws Exception
  {
    for(Map<String,Object> governanceRule : list) {
      String code = (String) governanceRule.get("code");
      Number iid = (Number) governanceRule.get("iid");

      Vertex governanceRuleVertex = UtilClass.getVertexByCode(code, vertexType);

      governanceRuleVertex.setProperty(CommonConstants.CODE_PROPERTY, "KPI" + iid.toString());
    }
  }

  private void updateIID(List<Map<String, Object>> list, String vertexType, String iidTag)
  {
    
    for (Map<String, Object> item : list) {
      String code = (String) item.get(CODE);
      long iid = Long.parseLong(String.valueOf(item.get(IID)));
      String query = "UPDATE " + vertexType + " SET " + iidTag + " = " + iid + " WHERE code = '" + code + "'";
      
      UtilClass.getGraph().command(new OCommandSQL(query)).execute();
    }
  }
  
}
