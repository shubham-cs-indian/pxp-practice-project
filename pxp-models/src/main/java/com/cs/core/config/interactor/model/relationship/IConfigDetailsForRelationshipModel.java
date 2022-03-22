package com.cs.core.config.interactor.model.relationship;

import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface IConfigDetailsForRelationshipModel extends IModel {
  
  public static final String REFERENCED_ATTRIBUTES = "referencedAttributes";
  public static final String REFERENCED_TAGS       = "referencedTags";
  public static final String REFERENCED_KLASSES    = "referencedKlasses";
  public static final String REFERENCED_CONTEXTS   = "referencedContexts";
  public static final String REFERENCED_TABS       = "referencedTabs";
  
  public Map<String, IConfigEntityInformationModel> getReferencedAttributes();
  
  public void setReferencedAttributes(
      Map<String, IConfigEntityInformationModel> referencedAttributes);
  
  public Map<String, IConfigEntityInformationModel> getReferencedTags();
  
  public void setReferencedTags(Map<String, IConfigEntityInformationModel> referencedTags);
  
  public Map<String, IConfigEntityInformationModel> getReferencedKlasses();
  
  public void setReferencedKlasses(Map<String, IConfigEntityInformationModel> referencedKlasses);
  
  public Map<String, IConfigEntityInformationModel> getReferencedContexts();
  
  public void setReferencedContexts(Map<String, IConfigEntityInformationModel> referencedContext);
  
  public Map<String, IIdLabelCodeModel> getReferencedTabs();
  
  public void setReferencedTabs(Map<String, IIdLabelCodeModel> referencedTabs);
}
