package com.cs.core.config.interactor.exception.tag;

import com.cs.core.exception.PluginException;

public class TagLinkedToTaxonomyHierarchyCannotBeDeletedException extends PluginException {
  
  private static final long serialVersionUID = 1L;
  
  public TagLinkedToTaxonomyHierarchyCannotBeDeletedException()
  {
    super();
  }
  
  public TagLinkedToTaxonomyHierarchyCannotBeDeletedException(String message)
  {
    super(message);
  }
}
