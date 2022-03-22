package com.cs.config.strategy.plugin.usecase.klass;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.base.klass.AbstractGetKlassesTree;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.taxonomy.ITaxonomy;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

public class GetTaxonomiesTree extends AbstractGetKlassesTree {
  
  public GetTaxonomiesTree(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetTaxonomiesTree/*" };
  }
  
  @Override
  public String getTypeKlass()
  {
    return VertexLabelConstants.ROOT_KLASS_TAXONOMY;
  }
  
  @Override
  public Map<String, Object> executeInternal(Map<String, Object> requestMap) throws Exception
  {
    try {
      return super.executeInternal(requestMap);
    }
    catch (KlassNotFoundException e) {
      throw new KlassNotFoundException(e);
    }
  }
  
  @Override
  public List<String> getParentIds(Map<String, Object> requestMap)
  {
    List<String> ids = (List<String>) requestMap.get(IIdsListParameterModel.IDS);
    if(ids.isEmpty()) {
      Iterable<Vertex> vertices = UtilClass.getGraph()
          .command(new OCommandSQL("select from " + getTypeKlass()
              + " where outE('Child_Of').size() = 0 and " + ITaxonomy.TAXONOMY_TYPE + " not in "
              + EntityUtil.quoteIt(Arrays.asList(CommonConstants.MINOR_TAXONOMY)) + " order by "
              + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " asc"))
          .execute();
      
      for (Vertex childKlassTaxonomy : vertices) {
        Map<String, Object> childTaxonomy = UtilClass.getMapFromVertex(getFieldsToFetch(), childKlassTaxonomy);
        ids.add((String) childTaxonomy.get("code"));
      }
    }
    return (List<String>) ids;
  }
  
  public List<String> getFieldsToFetch()
  {
    return Arrays.asList(
        ITaxonomy.LABEL, ITaxonomy.TYPE, ITaxonomy.CODE);
  }
}
