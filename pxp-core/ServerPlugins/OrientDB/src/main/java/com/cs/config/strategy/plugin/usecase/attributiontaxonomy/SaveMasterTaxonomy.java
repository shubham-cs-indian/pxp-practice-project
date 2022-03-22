package com.cs.config.strategy.plugin.usecase.attributiontaxonomy;

import java.util.Map;

import com.cs.config.strategy.plugin.usecase.base.attributiontaxonomy.AbstractSaveTagTaxonomy;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.exception.taxonomy.ParentKlassTaxonomyNotFoundException;
import com.cs.core.config.interactor.exception.taxonomyarticle.ParentArticleTaxonomyNotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;

public class SaveMasterTaxonomy extends AbstractSaveTagTaxonomy {
  
  public SaveMasterTaxonomy(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|SaveMasterTaxonomy/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    try {
      return super.execute(requestMap);
    }
    catch (ParentKlassTaxonomyNotFoundException e) {
      throw new ParentArticleTaxonomyNotFoundException(e);
    }
  }
  
  public String getVertexType()
  {
    return VertexLabelConstants.ATTRIBUTION_TAXONOMY;
  }
  
  public String getTaxonomyLevelType()
  {
    return VertexLabelConstants.ATTRIBUTION_TAXONOMY_LEVEL;
  }
  
  public String getTagType()
  {
    return SystemLevelIds.MASTER_TAG_TYPE_ID;
  }
  
  @Override
  public void validateTaxonomy(Map<String, Object> requestMap) throws Exception
  {
  }
  
  @Override
  public void updateDefaultDataLanguage(Map<String, Object> requestMap)
  {
  }
  
  @Override
  public void updateDefaultUserInterfaceLanguage(Map<String, Object> requestMap) throws Exception
  {
  }
}
