package com.cs.config.strategy.plugin.usecase.repair;

import com.cs.config.strategy.plugin.usecase.tabs.utils.TabUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.tabs.ITab;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class Orient_Migration_Script_For_Tab_24_08_2018 extends AbstractOrientPlugin {
  
  public Orient_Migration_Script_For_Tab_24_08_2018(
      final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String sequenceListVertexLabel = (String) requestMap.remove("sequenceListVertexLabel");
    String uiLanguage = (String) requestMap.remove("uiLanguage");
    Vertex tabNode = TabUtils.createTabNode(requestMap, new ArrayList<String>());
    tabNode.setProperty(ITab.IS_STANDARD, requestMap.get(ITab.IS_STANDARD));
    tabNode.setProperty(ITab.LABEL + "__" + uiLanguage, requestMap.get(ITab.LABEL));
    List<String> propertySequenceList = (List<String>) requestMap.get(ITab.PROPERTY_SEQUENCE_LIST);
    String query = "select from " + sequenceListVertexLabel + " where "
        + CommonConstants.CODE_PROPERTY + " in " + EntityUtil.quoteIt(propertySequenceList);
    if (propertySequenceList != null && propertySequenceList.size() > 0) {
      Iterable<Vertex> vertices = UtilClass.getGraph()
          .command(new OCommandSQL(query))
          .execute();
      for (Vertex vertex : vertices) {
        vertex.addEdge(RelationshipLabelConstants.HAS_TAB, tabNode);
      }
    }
    UtilClass.getGraph()
        .commit();
    return null;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|Orient_Migration_Script_For_Tab_24_08_2018/*" };
  }
}
