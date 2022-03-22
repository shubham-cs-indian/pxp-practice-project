package com.cs.config.strategy.plugin.usecase.tag;

import com.cs.constants.SystemLevelIds;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;

public class GetAvailableMasterTags extends AbstractGetAvailableTaxonomyTags {
  
  public GetAvailableMasterTags(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetAvailableMasterTags/*" };
  }
  
  @Override
  public String getTagType()
  {
    return SystemLevelIds.MASTER_TAG_TYPE_ID;
  }
}
