package com.cs.config.strategy.plugin.usecase.base.attributiontaxonomy;

import com.cs.config.strategy.plugin.usecase.attributiontaxonomy.util.AttributionTaxonomyUtil;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.attributiontaxonomy.IMasterTaxonomy;
import com.cs.core.config.interactor.exception.taxonomy.KlassTaxonomyNotFoundException;
import com.cs.core.config.interactor.model.attributiontaxonomy.IGetAttributionTaxonomyModel;
import com.cs.core.config.interactor.model.attributiontaxonomy.IGetMasterTaxonomyWithoutKPModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractGetKlassAndAttributionTaxonomy extends AbstractOrientPlugin {
  
  public AbstractGetKlassAndAttributionTaxonomy(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  public abstract String getVertexType();
  
  public abstract String getTaxonomyLevelType();
  
  protected static final List<String> FIELDS_TO_FETCH_FOR_CHILD_KLASS_TAXONOMY = Arrays.asList(
      IMasterTaxonomy.LABEL, CommonConstants.CODE_PROPERTY, IMasterTaxonomy.TAXONOMY_TYPE);
  
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> mapToReturn = new HashMap<>();
    String id = (String) requestMap.get(IMasterTaxonomy.ID);
    Vertex klassTaxonomy = null;
    if (id.equals("-1")) {
      List<Map<String, Object>> children = new ArrayList<>();
      Iterable<Vertex> vertices = UtilClass.getGraph()
          .command(new OCommandSQL("select from " + VertexLabelConstants.ROOT_KLASS_TAXONOMY
              + " where outE('Child_Of').size() = 0 order by "
              + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " asc"))
          .execute();
      
      for (Vertex childKlassTaxonomy : vertices) {
        Map<String, Object> childTaxonomy = UtilClass
            .getMapFromVertex(FIELDS_TO_FETCH_FOR_CHILD_KLASS_TAXONOMY, childKlassTaxonomy);
        children.add(childTaxonomy);
      }
      Map<String, Object> rootTaxonomyMap = new HashMap<>();
      rootTaxonomyMap.put(IGetAttributionTaxonomyModel.CHILDREN, children);
      mapToReturn.put(IGetMasterTaxonomyWithoutKPModel.ENTITY, rootTaxonomyMap);
    }
    else {
      
      try {
        klassTaxonomy = UtilClass.getVertexById(id, getVertexType());
      }
      catch (NotFoundException e) {
        throw new KlassTaxonomyNotFoundException(e);
      }
      Map<String, Object> klassTaxonomyMap = AttributionTaxonomyUtil.getAttributionTaxonomy(klassTaxonomy, getTaxonomyLevelType());
      mapToReturn.put(IGetMasterTaxonomyWithoutKPModel.ENTITY, klassTaxonomyMap);
      AttributionTaxonomyUtil.fillAttributionTaxonomyData(id, mapToReturn, klassTaxonomy, false, false);
    }
    return mapToReturn;
  }
}
