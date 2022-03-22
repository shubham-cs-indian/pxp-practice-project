package com.cs.config.strategy.plugin.usecase.base.attributiontaxonomy;

import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.taxonomy.ITaxonomy;
import com.cs.core.config.interactor.model.attributiontaxonomy.IGetAttributionTaxonomyModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public abstract class AbstractGetTaxonomiesForQuicklist extends AbstractOrientPlugin {
  
  public AbstractGetTaxonomiesForQuicklist(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  public abstract String getVertexType();
  
  protected static final List<String> FIELDS_TO_FETCH_FOR_CHILD_KLASS_TAXONOMY = Arrays
      .asList(ITaxonomy.LABEL, CommonConstants.CODE_PROPERTY);
  
  public abstract String getKlassVertexType();
  
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> mapToReturn = new HashMap<>();
    List<String> ids = (List<String>) requestMap.get(IIdsListParameterModel.IDS);
    List<Map<String, Object>> children = new ArrayList<>();
    Iterable<Vertex> vertices = UtilClass.getGraph()
        .command(new OCommandSQL("select from " + getVertexType()
            + " where outE('Child_Of').size() = 0  AND code in " + EntityUtil.quoteIt(ids)))
        .execute();
    for (Vertex childKlassTaxonomy : vertices) {
      Map<String, Object> childTaxonomy = UtilClass
          .getMapFromVertex(FIELDS_TO_FETCH_FOR_CHILD_KLASS_TAXONOMY, childKlassTaxonomy);
      children.add(childTaxonomy);
    }
    mapToReturn.put(IGetAttributionTaxonomyModel.CHILDREN, children);
    
    return mapToReturn;
  }
}
