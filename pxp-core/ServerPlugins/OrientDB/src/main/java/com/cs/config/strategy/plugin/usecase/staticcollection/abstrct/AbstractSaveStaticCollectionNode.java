package com.cs.config.strategy.plugin.usecase.staticcollection.abstrct;

import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;

public abstract class AbstractSaveStaticCollectionNode extends AbstractOrientPlugin {
  
  public AbstractSaveStaticCollectionNode(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
}
