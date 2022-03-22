package com.cs.core.config.interactor.model.configdetails;

import java.util.Map;

import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IConfigModelForBoarding extends IModel {
  
  public static final String ATTRIBUTES          = "attributes";
  public static final String TAGS                = "tags";
  public static final String TAXONOMY            = "taxonomy";
  public static final String KLASSES             = "klasses";
  public static final String PROPERTYCOLLECTIONS = "propertyCollections";
  public static final String RELATIONSHIPS       = "relationships";
  public static final String CONTEXTS            = "contexts";
  
  public Map<String, IConfigEntityInformationModel> getAttributes();
  
  public void setAttributes(Map<String, IConfigEntityInformationModel> attributes);
  
  public Map<String, ITag> getTags();
  
  public void setTags(Map<String, ITag> tags);
  
  public Map<String, IConfigEntityInformationModel> getTaxonomy();
  
  public void setTaxonomy(Map<String, IConfigEntityInformationModel> taxonomy);
  
  public Map<String, IConfigEntityInformationModel> getKlasses();
  
  public void setKlasses(Map<String, IConfigEntityInformationModel> klasses);
  
  public Map<String, IConfigEntityInformationModel> getPropertyCollections();
  
  public void setPropertyCollections(
      Map<String, IConfigEntityInformationModel> propertyCollections);
  
 public Map<String, IConfigEntityInformationModel> getRelationships();
  
  public void setRelationships(
      Map<String, IConfigEntityInformationModel> relationships);
  
  public Map<String, IConfigEntityInformationModel> getContexts();
  
  public void setContexts(Map<String, IConfigEntityInformationModel> contexts);
}
