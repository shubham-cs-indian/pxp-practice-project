package com.cs.config.strategy.plugin.usecase.taxonomy.article;

import com.cs.config.strategy.plugin.usecase.base.taxonomy.util.TaxonomyUtil;
import com.cs.core.runtime.interactor.model.taxonomy.IGetTaxonomyTreeLeafIdsStrategyModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class GetTaxonomyTreeByLeafIds extends AbstractOrientPlugin {
  
  public GetTaxonomyTreeByLeafIds(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetTaxonomyTreeByLeafIds/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String parentTaxonomyId = (String) requestMap
        .get(IGetTaxonomyTreeLeafIdsStrategyModel.PARENT_TAXONOMY_ID);
    List<String> leafTaxonomyIds = (List<String>) requestMap
        .get(IGetTaxonomyTreeLeafIdsStrategyModel.LEAF_IDS);
    return TaxonomyUtil.getTaxonomyTreeByRootTaxonomyIdAndLeafIds(parentTaxonomyId,
        leafTaxonomyIds);
  }
}
