package com.cs.config.strategy.plugin.usecase.target;

import com.cs.config.strategy.plugin.usecase.klass.util.KlassGetUtils;
import com.cs.config.strategy.plugin.usecase.tag.util.TagUtils;
import com.cs.config.strategy.plugin.usecase.target.util.TargetUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.model.target.ITargetModel;
import com.cs.core.exception.KlassNotFoundException;
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

public class GetTarget extends AbstractOrientPlugin {
  
  public GetTarget(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    String id = (String) requestMap.get("id");
    Map<String, Object> returnMap = new HashMap<String, Object>();
    Map<String, Object> rootNode = new HashMap<>();
    
    if (id.equals("-1")) {
      rootNode.put(CommonConstants.ID_PROPERTY, "-1");
      rootNode.put(CommonConstants.TYPE_PROPERTY, CommonConstants.MARKET_KLASS_TYPE);
      List<Map<String, Object>> childKlasses = new ArrayList<>();
      rootNode.put(CommonConstants.CHILDREN_PROPERTY, childKlasses);
      
      Iterable<Vertex> i = UtilClass.getGraph()
          .command(new OCommandSQL("select from " + VertexLabelConstants.ENTITY_TYPE_TARGET
              + " where outE('Child_Of').size() = 0 order by "
              + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " asc"))
          .execute();
      
      for (Vertex targetNode : i) {
        childKlasses.add(TargetUtils.getTargetEntityMap(targetNode, null));
      }
    }
    else {
      Vertex targetNode = null;
      try {
        targetNode = UtilClass.getVertexByIndexedId(id, VertexLabelConstants.ENTITY_TYPE_TARGET);
      }
      catch (NotFoundException e) {
        throw new KlassNotFoundException();
      }
      rootNode = TargetUtils.getTargetEntityMap(targetNode, null);
      List<String> lifeCycleTags = (List<String>) rootNode.get(IKlass.LIFE_CYCLE_STATUS_TAGS);
      List<Map<String, Object>> referencedTags = new ArrayList<>();
      
      for (String lifeCycleTagId : lifeCycleTags) {
        Vertex linkedTagNode = UtilClass.getVertexById(lifeCycleTagId,
            VertexLabelConstants.ENTITY_TAG);
        Map<String, Object> referencedTag = TagUtils.getTagMap(linkedTagNode, true);
        referencedTags.add(referencedTag);
      }
      rootNode.put(ITargetModel.REFERENCED_TAGS, referencedTags);
      List<Map<String, Object>> variantsList = new ArrayList<>();
      KlassGetUtils.fillTechnicalImageVariantWithAutoCreateEnabled(targetNode, variantsList);
      returnMap.put(ITargetModel.TECHNICAL_IMAGEVARIANT_CONTEXT_WITH_AUTOCREATEENABLE,
          variantsList);
    }
    
    return rootNode;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetTarget/*" };
  }
}
