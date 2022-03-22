package com.cs.config.strategy.plugin.usecase.tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.datarule.IGetAllowedMasterTagsForHierarchyTaxonomyRequestModel;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class GetAvailableMasterTagsForArticleTaxonomy extends AbstractOrientPlugin {
  
  public GetAvailableMasterTagsForArticleTaxonomy(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetAvailableMasterTagsForArticleTaxonomy/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String taxonomyId = (String) requestMap
        .get(IGetAllowedMasterTagsForHierarchyTaxonomyRequestModel.ID);
    String searchText = (String) requestMap
        .get(IGetAllowedMasterTagsForHierarchyTaxonomyRequestModel.SEARCH_TEXT);
    Long from = Long
        .valueOf(requestMap.get(IGetAllowedMasterTagsForHierarchyTaxonomyRequestModel.FROM)
            .toString());
    Long size = Long
        .valueOf(requestMap.get(IGetAllowedMasterTagsForHierarchyTaxonomyRequestModel.SIZE)
            .toString());
    String sortOrder = requestMap
        .get(IGetAllowedMasterTagsForHierarchyTaxonomyRequestModel.SORT_ORDER)
        .toString();
    
    OrientGraph graph = UtilClass.getGraph();
    UtilClass.setReferencedKlassIdStructureMapping(new HashMap<String, Object>());
    Map<String, Object> returnMap = new HashMap<String, Object>();
    List<Map<String, Object>> tags = new ArrayList<Map<String, Object>>();
    
    String query = "select from " + VertexLabelConstants.ENTITY_TAG + " where " + ITag.TAG_TYPE
        + " = '" + SystemLevelIds.MASTER_TAG_TYPE_ID + "'" + " and in('"
        + RelationshipLabelConstants.TAXONOMY_LEVEL + "').code not in "
        + EntityUtil.quoteIt(taxonomyId) + " and out('"
        + RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF + "').size() = 0 and "
        + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " like '%"
        + searchText + "%'" + " order by "
        + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " " + sortOrder
        + " skip " + from + " limit " + size;
    Iterable<Vertex> i = graph.command(new OCommandSQL(query))
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
