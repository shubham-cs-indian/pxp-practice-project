package com.cs.config.strategy.plugin.usecase.tag;

import com.cs.constants.SystemLevelIds;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;

public class GetAvailableLanguageTags extends AbstractGetAvailableTaxonomyTags {
  
  public GetAvailableLanguageTags(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetAvailableLanguageTags/*" };
  }
  
  public String getTagType()
  {
    return SystemLevelIds.LANGUAGE_TAG_TYPE_ID;
  }
}
