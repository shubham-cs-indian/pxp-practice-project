package com.cs.config.strategy.plugin.usecase.tagtype;

import com.cs.config.strategy.plugin.usecase.tagtype.util.TagTypeUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.Map;

public class GetTagType extends AbstractOrientPlugin {
  
  public GetTagType(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> tagMap) throws Exception
  {
    String id = (String) tagMap.get("id");
    Vertex tagTypeNode = UtilClass.getVertexByIndexedId(id, VertexLabelConstants.ENTITY_TAG_TYPE);
    return TagTypeUtil.getTagTypeMap(tagTypeNode);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetTagType/*" };
  }
}
