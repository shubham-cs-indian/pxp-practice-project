package com.cs.config.strategy.plugin.usecase.configdetails.instancetree;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.configdetails.instancetree.abstrct.AbstarctConfigDetailForKlassTaxonomyTree;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.taxonomy.ITaxonomy;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForGetKlassTaxonomyTreeRequestModel;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;

@SuppressWarnings("unchecked")
public class GetConfigDetailForKlassTaxonomyTree extends AbstarctConfigDetailForKlassTaxonomyTree {
  
  public static final List<String> fieldsToFetch = Arrays.asList(IKlass.ID, IKlass.LABEL, IKlass.CODE, IKlass.TYPE,
      ITaxonomy.TAXONOMY_TYPE);
  
  public GetConfigDetailForKlassTaxonomyTree(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetConfigDetailForKlassTaxonomyTree/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> mapToReturn = (Map<String, Object>) super.execute(requestMap);
    String kpiId = (String) requestMap.get(IConfigDetailsForGetKlassTaxonomyTreeRequestModel.KPI_ID);
    kpiHandling(kpiId, mapToReturn);
    return mapToReturn;
  }
}
