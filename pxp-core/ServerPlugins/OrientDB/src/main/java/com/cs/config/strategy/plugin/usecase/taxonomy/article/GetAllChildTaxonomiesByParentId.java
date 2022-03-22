package com.cs.config.strategy.plugin.usecase.taxonomy.article;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.entity.taxonomy.ITaxonomy;
import com.cs.core.config.interactor.exception.taxonomy.ParentKlassTaxonomyNotFoundException;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.klass.IGetChildMajorTaxonomiesRequestModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

public class GetAllChildTaxonomiesByParentId extends AbstractOrientPlugin {
  
  public GetAllChildTaxonomiesByParentId(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  protected List<String> fieldsToFetch = Arrays.asList(ITaxonomy.LABEL, ITaxonomy.ID, ITaxonomy.CODE);
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetAllChildTaxonomiesByParentId/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String parentId = (String) requestMap.get(IGetChildMajorTaxonomiesRequestModel.TAXONOMY_ID);
    
    Vertex parentTaxonomy = null;
    try {
      parentTaxonomy = UtilClass.getVertexByIndexedId(parentId, VertexLabelConstants.ROOT_KLASS_TAXONOMY);
    }
    catch (NotFoundException e) {
      throw new ParentKlassTaxonomyNotFoundException(e);
    }
    String rid = parentTaxonomy.getId().toString();
    
    String query = "select from (traverse in('" + RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF + "') from " + rid
        + " strategy BREADTH_FIRST) where code != '" + parentId + "'";
    
    Iterable<Vertex> childTaxonomies = UtilClass.getGraph().command(new OCommandSQL(query)).execute();
    
    List<Map<String, Object>> children = new ArrayList<>();
    for (Vertex child : childTaxonomies) {
      Map<String, Object> childMap = UtilClass.getMapFromVertex(fieldsToFetch, child);
      children.add(childMap);
    }
    
    Map<String, Object> returnMap = new HashMap<>();
    returnMap.put(IListModel.LIST, children);
    
    return returnMap;
  }
}