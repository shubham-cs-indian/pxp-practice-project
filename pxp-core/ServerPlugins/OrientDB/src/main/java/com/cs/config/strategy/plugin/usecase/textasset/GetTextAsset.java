package com.cs.config.strategy.plugin.usecase.textasset;

import com.cs.config.strategy.plugin.usecase.klass.util.KlassGetUtils;
import com.cs.config.strategy.plugin.usecase.tag.util.TagUtils;
import com.cs.config.strategy.plugin.usecase.textasset.util.TextAssetUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.model.textasset.ITextAssetModel;
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

public class GetTextAsset extends AbstractOrientPlugin {
  
  public GetTextAsset(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    
    Map<String, Object> rootNode = new HashMap<>();
    Map<String, Object> returnMap = null;
    if (requestMap.get("id")
        .equals("-1")) {
      rootNode.put(CommonConstants.ID_PROPERTY, "-1");
      rootNode.put(CommonConstants.TYPE_PROPERTY, CommonConstants.TEXT_ASSET_KLASS_TYPE);
      List<Map<String, Object>> childKlasses = new ArrayList<>();
      rootNode.put(CommonConstants.CHILDREN_PROPERTY, childKlasses);
      
      Iterable<Vertex> i = UtilClass.getGraph()
          .command(new OCommandSQL("select from " + VertexLabelConstants.ENTITY_TYPE_TEXT_ASSET
              + " where outE('Child_Of').size() = 0 order by "
              + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " asc"))
          .execute();
      
      for (Vertex klassNode : i) {
        HashMap<String, Object> klassEntityMap = new HashMap<String, Object>();
        klassEntityMap.putAll(UtilClass.getMapFromNode(klassNode));
        childKlasses.add(klassEntityMap);
      }
      returnMap = new HashMap<String, Object>();
      returnMap.put("klass", rootNode);
    }
    else {
      try {
        Vertex textAssetNode = UtilClass.getVertexByIndexedId((String) requestMap.get("id"),
            VertexLabelConstants.ENTITY_TYPE_TEXT_ASSET);
        returnMap = TextAssetUtils.getTextAssetEntityMap(textAssetNode, null);
        List<String> lifeCycleTags = (List<String>) returnMap.get(IKlass.LIFE_CYCLE_STATUS_TAGS);
        List<Map<String, Object>> referencedTags = new ArrayList<>();
        
        for (String lifeCycleTagId : lifeCycleTags) {
          Vertex linkedTagNode = UtilClass.getVertexById(lifeCycleTagId,
              VertexLabelConstants.ENTITY_TAG);
          Map<String, Object> referencedTag = TagUtils.getTagMap(linkedTagNode, true);
          referencedTags.add(referencedTag);
        }
        returnMap.put(ITextAssetModel.REFERENCED_TAGS, referencedTags);
        List<Map<String, Object>> variantsList = new ArrayList<>();
        KlassGetUtils.fillTechnicalImageVariantWithAutoCreateEnabled(textAssetNode, variantsList);
        returnMap.put(ITextAssetModel.TECHNICAL_IMAGEVARIANT_CONTEXT_WITH_AUTOCREATEENABLE,
            variantsList);
      }
      catch (NotFoundException e) {
        throw new KlassNotFoundException();
      }
    }
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetTextAsset/*" };
  }
}
