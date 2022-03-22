package com.cs.core.config.interactor.exception.tag;

import com.cs.core.exception.PluginException;

public class AttributionTaxonomyTagCanNotBeDeleted extends PluginException {
  
  private static final long serialVersionUID = 1L;
  
  public AttributionTaxonomyTagCanNotBeDeleted()
  {
    super();
  }
  
  public AttributionTaxonomyTagCanNotBeDeleted(String message)
  {
    super(message);
  }
}
