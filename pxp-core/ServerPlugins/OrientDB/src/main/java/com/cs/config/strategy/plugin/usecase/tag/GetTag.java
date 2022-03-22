package com.cs.config.strategy.plugin.usecase.tag;

import com.cs.config.strategy.plugin.usecase.tag.util.TagUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.exception.tag.TagNotFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetTag extends AbstractOrientPlugin {
  
  public GetTag(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    
    Map<String, Object> returnMap = new HashMap<String, Object>();
    
    if (requestMap.get("id")
        .equals("-1")) {
      Map<String, Object> rootNode = new HashMap<>();
      rootNode.put(CommonConstants.ID_PROPERTY, "-1");
      List<Map<String, Object>> childTags = new ArrayList<>();
      rootNode.put(CommonConstants.CHILDREN_PROPERTY, childTags);
      Iterable<Vertex> i = null;
      String mode = (String) requestMap.get("mode");
      if (mode.equals("pim")) {
        i = UtilClass.getGraph()
            .command(new OCommandSQL("select from " + VertexLabelConstants.ENTITY_TAG
                + " where " + ITag.IS_ROOT + " = true and forPim = true order by "
                + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " asc"))
            .execute();
      }
      else if (mode.equals("mam")) {
        i = UtilClass.getGraph()
            .command(new OCommandSQL("select from " + VertexLabelConstants.ENTITY_TAG
                + " where " + ITag.IS_ROOT + " = true and forMam = true order by "
                + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " asc"))
            .execute();
      }
      else if (mode.equals("target")) {
        i = UtilClass.getGraph()
            .command(new OCommandSQL("select from " + VertexLabelConstants.ENTITY_TAG
                + " where " + ITag.IS_ROOT + " = true and forTarget = true order by "
                + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " asc"))
            .execute();
      }
      else if (mode.equals("editorial")) {
        i = UtilClass.getGraph()
            .command(new OCommandSQL(
                "select from tag where " + ITag.IS_ROOT + " = true and forEditorial = true order by "
                    + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY)
                    + " asc"))
            .execute();
      }
      else {
        i = UtilClass.getGraph()
            .command(new OCommandSQL("select from " + VertexLabelConstants.ENTITY_TAG
                + " where " + ITag.IS_ROOT + " = true order by "
                + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " asc"))
            .execute();
      }
      for (Vertex tagNode : i) {
        HashMap<String, Object> childmap = new HashMap<String, Object>();
        childmap.putAll(TagUtils.getTagMap(tagNode, true));
        childTags.add(childmap);
      }
      
      returnMap = rootNode;
    }
    else {
      
      try {
        Vertex tagNode = UtilClass.getVertexByIndexedId(
            (String) requestMap.get(CommonConstants.ID_PROPERTY), VertexLabelConstants.ENTITY_TAG);
        returnMap = TagUtils.getTagMap(tagNode, false);
      }
      catch (NotFoundException e) {
        throw new TagNotFoundException();
      }
    }
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetTag/*" };
  }
}

/**/

/**/
