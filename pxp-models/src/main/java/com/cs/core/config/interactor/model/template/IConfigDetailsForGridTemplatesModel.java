package com.cs.core.config.interactor.model.template;

import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface IConfigDetailsForGridTemplatesModel extends IModel {
  
  public static final String REFERENCED_RELATIONSHIPS        = "referencedRelationships";
  public static final String REFERENCED_NATURE_RELATIONSHIPS = "referencedNatureRelationships";
  public static final String REFERENCED_PROPERTY_COLLECTIONS = "referencedPropertyCollections";
  public static final String REFERENCED_CONTEXT              = "referencedContexts";

  
  public Map<String, IIdLabelCodeModel> getReferencedRelationships();
  
  public void setReferencedRelationships(Map<String, IIdLabelCodeModel> referencedRelationships);
  
  public Map<String, IIdLabelCodeModel> getReferencedNatureRelationships();
  
  public void setReferencedNatureRelationships(
      Map<String, IIdLabelCodeModel> referencedNatureRelationships);
  
  public Map<String, IIdLabelCodeModel> getReferencedPropertyCollections();
  
  public void setReferencedPropertyCollections(
      Map<String, IIdLabelCodeModel> referencedPropertyCollections);
  
  public Map<String, IIdLabelCodeModel> getReferencedContexts();
  
  public void setReferencedContexts(Map<String, IIdLabelCodeModel> referencedVariantContexts);

}
