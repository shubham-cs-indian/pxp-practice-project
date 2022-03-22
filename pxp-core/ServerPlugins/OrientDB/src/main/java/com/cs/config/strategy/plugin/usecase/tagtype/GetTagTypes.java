package com.cs.config.strategy.plugin.usecase.tagtype;

import com.cs.config.strategy.plugin.usecase.tagtype.util.TagTypeUtil;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetTagTypes extends AbstractOrientPlugin {
  
  public GetTagTypes(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String query = "select from " + VertexLabelConstants.ENTITY_TAG_TYPE + " order by "
        + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " asc";
    Iterable<Vertex> tagTypeNodes = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    
    List<Map<String, Object>> tagTypeList = new ArrayList<Map<String, Object>>();
    for (Vertex tagTypeNode : tagTypeNodes) {
      tagTypeList.add(TagTypeUtil.getTagTypeMap(tagTypeNode));
    }
    Map<String, Object> response = new HashMap<String, Object>();
    response.put("list", tagTypeList);
    
    return response;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetTagTypes/*" };
  }
}
