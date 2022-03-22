package com.cs.config.strategy.plugin.usecase.tag;

import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class AbstractGetAvailableTaxonomyTags extends AbstractOrientPlugin {
  
  public abstract String getTagType();
  
  public AbstractGetAvailableTaxonomyTags(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  public Object execute(Map<String, Object> map) throws Exception
  {
    
    OrientGraph graph = UtilClass.getGraph();
    UtilClass.setReferencedKlassIdStructureMapping(new HashMap<String, Object>());
    Map<String, Object> returnMap = new HashMap<String, Object>();
    List<Map<String, Object>> tags = new ArrayList<Map<String, Object>>();
    
    Iterable<Vertex> i = graph
        .command(new OCommandSQL(
            "select from " + VertexLabelConstants.ENTITY_TAG + " where " + ITag.TAG_TYPE + " = '"
                + getTagType() + "'" + " AND " + ITag.TYPE + " is not null order by "
                + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " asc"))
        .execute();
    
    for (Vertex tagNode : i) {
      Map<String, Object> tagMap = new HashMap<String, Object>();
      Iterator<Edge> edges = tagNode
          .getEdges(Direction.OUT, RelationshipLabelConstants.LEVEL_TAGGROUP_OF)
          .iterator();
      if (edges.hasNext()) {
        // if any edge with level node exist means it is already added in some
        // taxonomy hence
        // continue
        continue;
      }
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
