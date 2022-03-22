package com.cs.config.strategy.plugin.usecase.base.attributiontaxonomy;

import com.cs.config.strategy.plugin.usecase.base.taxonomy.util.TaxonomyUtil;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.taxonomy.ITaxonomy;
import com.cs.core.config.interactor.exception.taxonomy.KlassTaxonomyNotFoundException;
import com.cs.core.config.interactor.model.attributiontaxonomy.IGetAttributionTaxonomyModel;
import com.cs.core.exception.NotFoundException;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractGetTaxonomies extends AbstractOrientPlugin {
  
  public AbstractGetTaxonomies(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  public abstract String getVertexType();
  
  protected static final List<String> FIELDS_TO_FETCH_FOR_CHILD_KLASS_TAXONOMY = Arrays.asList(
      ITaxonomy.LABEL, CommonConstants.CODE_PROPERTY, ITaxonomy.TAXONOMY_TYPE, ITaxonomy.CODE);
  
  public abstract String getKlassVertexType();
  
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> mapToReturn = new HashMap<>();
    String id = (String) requestMap.get(ITaxonomy.ID);
    Vertex klassTaxonomy = null;
    if (id.equals("-1")) {
      List<Map<String, Object>> children = new ArrayList<>();
      Iterable<Vertex> vertices = UtilClass.getGraph()
          .command(new OCommandSQL("select from " + getVertexType()
              + " where outE('Child_Of').size() = 0 and " + ITaxonomy.TAXONOMY_TYPE + " not in "
              + EntityUtil.quoteIt(Arrays.asList(CommonConstants.MINOR_TAXONOMY)) + " order by "
              + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " asc"))
          .execute();
      
      for (Vertex childKlassTaxonomy : vertices) {
        Map<String, Object> childTaxonomy = UtilClass.getMapFromVertex(FIELDS_TO_FETCH_FOR_CHILD_KLASS_TAXONOMY, childKlassTaxonomy);
        children.add(childTaxonomy);
      }
      
      mapToReturn.put(IGetAttributionTaxonomyModel.CHILDREN, children);
    }
    else {
      try {
        klassTaxonomy = UtilClass.getVertexById(id, getVertexType());
      }
      catch (NotFoundException e) {
        throw new KlassTaxonomyNotFoundException(e);
      }
      mapToReturn = TaxonomyUtil.getKlassTaxonomy(klassTaxonomy);
    }
    TaxonomyUtil.getTaxonomyData(id, mapToReturn, klassTaxonomy, getKlassVertexType());
    
    return mapToReturn;
  }
}
