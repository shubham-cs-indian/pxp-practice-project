package com.cs.core.config.interactor.model.customtemplate;

import java.util.Map;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;
import com.cs.core.config.interactor.entity.customtemplate.ICustomTemplate;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelModel;

public interface IGetCustomTemplateModel extends IModel, IConfigResponseWithAuditLogModel {
  
  public static final String TEMPLATE                        = "template";
  public static final String REFERENCED_RELATIONSHIPS        = "referencedRelationships";
  public static final String REFERENCED_NATURE_RELATIONSHIPS = "referencedNatureRelationships";
  public static final String REFERENCED_PROPERTY_COLLECTIONS = "referencedPropertyCollections";
  public static final String REFERENCED_CONTEXT              = "referencedContexts";
  
  public ICustomTemplate getTemplate();
  
  public void setTemplate(ICustomTemplate template);
  
  public Map<String, IIdLabelModel> getReferencedRelationships();
  
  public void setReferencedRelationships(Map<String, IIdLabelModel> referencedRelationships);
  
  public Map<String, IIdLabelModel> getReferencedNatureRelationships();
  
  public void setReferencedNatureRelationships(
      Map<String, IIdLabelModel> referencedNatureRelationships);
  
  public Map<String, IIdLabelModel> getReferencedPropertyCollections();
  
  public void setReferencedPropertyCollections(
      Map<String, IIdLabelModel> referencedPropertyCollections);
  
  public Map<String, IIdLabelModel> getReferencedContexts();
  
  public void setReferencedContexts(Map<String, IIdLabelModel> referencedVariantContexts);
}
