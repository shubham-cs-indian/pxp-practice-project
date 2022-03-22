package com.cs.config.strategy.plugin.usecase.tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.tag.util.TagUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class GetAllMasterTags extends AbstractOrientPlugin {
  
  public GetAllMasterTags(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetAllMasterTags/*" };
  }
  
  @Override
  public Object execute(Map<String, Object> map) throws Exception
  {
    
    OrientGraph graph = UtilClass.getGraph();
    UtilClass.setReferencedKlassIdStructureMapping(new HashMap<String, Object>());
    Map<String, Object> returnMap = new HashMap<String, Object>();
    List<Map<String, Object>> tags = new ArrayList<Map<String, Object>>();
    
    Iterable<Vertex> masterTags = graph
        .command(new OCommandSQL("select from " + VertexLabelConstants.ENTITY_TAG + " where "
            + ITag.TAG_TYPE + " = '" + SystemLevelIds.MASTER_TAG_TYPE_ID + "'" + " AND " + ITag.TYPE
            + " is not null order by "
            + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " asc"))
        .execute();
    
    for (Vertex tagNode : masterTags) {
      tags.add(TagUtils.getTagMap(tagNode, true));
    }
    returnMap.put(IListModel.LIST, tags);
    return returnMap;
  }
}
