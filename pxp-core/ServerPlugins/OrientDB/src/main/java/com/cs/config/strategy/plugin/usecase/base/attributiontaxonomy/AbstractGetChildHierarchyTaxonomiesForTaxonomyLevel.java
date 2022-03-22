package com.cs.config.strategy.plugin.usecase.base.attributiontaxonomy;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.exception.tag.TagNotFoundException;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractGetChildHierarchyTaxonomiesForTaxonomyLevel
    extends AbstractOrientPlugin {
  
  public AbstractGetChildHierarchyTaxonomiesForTaxonomyLevel(
      OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
    // TODO Auto-generated constructor stub
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String tagGroupId = (String) requestMap.get(IIdParameterModel.ID);
    
    List<String> klassTaxonomies = new ArrayList<>();
    try {
      UtilClass.getVertexByIndexedId(tagGroupId, VertexLabelConstants.ENTITY_TAG);
    }
    catch (NotFoundException e) {
      throw new TagNotFoundException();
    }
    String query = "select expand(in('Child_Of').in('Has_Master_Tag')) from Tag where code = '"
        + tagGroupId + "'";
    
    Iterable<Vertex> unlinkedKlassTaxonomies = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    for (Vertex klassTaxonomy : unlinkedKlassTaxonomies) {
      klassTaxonomies.add(UtilClass.getCodeNew(klassTaxonomy));
    }
    
    Map<String, Object> result = new HashMap<>();
    result.put(IListModel.LIST, klassTaxonomies);
    return result;
  }
}
