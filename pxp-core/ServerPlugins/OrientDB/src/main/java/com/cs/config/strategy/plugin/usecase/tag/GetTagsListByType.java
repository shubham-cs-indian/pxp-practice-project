package com.cs.config.strategy.plugin.usecase.tag;

import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetTagsListByType extends AbstractOrientPlugin {
  
  public GetTagsListByType(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetTagsListByType/*" };
  }
  
  @Override
  public Object execute(Map<String, Object> map) throws Exception
  {
    
    OrientGraph graph = UtilClass.getGraph();
    UtilClass.setReferencedKlassIdStructureMapping(new HashMap<String, Object>());
    Map<String, Object> returnMap = new HashMap<String, Object>();
    List<Map<String, Object>> tags = new ArrayList<Map<String, Object>>();
    
    String type = (String) map.get(IIdParameterModel.TYPE);
    Iterable<Vertex> i = graph
        .command(new OCommandSQL("select from " + VertexLabelConstants.ENTITY_TAG
            + " where " + ITag.IS_ROOT + " = true AND " + ITag.TAG_TYPE + " = '" + type
            + "' order by " + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY)
            + " asc"))
        .execute();
    
    for (Vertex tagNode : i) {
      Map<String, Object> tagMap = new HashMap<String, Object>();
      tagMap.put(ITag.ID, tagNode.getProperty(CommonConstants.CODE_PROPERTY));
      tagMap.put(ITag.ICON, tagNode.getProperty(ITag.ICON));
      tagMap.put(ITag.LABEL, UtilClass.getValueByLanguage(tagNode, ITag.LABEL));
      tagMap.put(ITag.TYPE, tagNode.getProperty(ITag.TYPE));
      
      tags.add(tagMap);
    }
    returnMap.put(IListModel.LIST, tags);
    return returnMap;
  }
}
