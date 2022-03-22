package com.cs.di.config.model.authorization;

import com.cs.core.config.interactor.model.configdetails.ConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.HashMap;
import java.util.Map;

public class ConfigModelForPartnerAuthorization implements IConfigModelForPartnerAuthorization {
  
  private static final long                            serialVersionUID        = 1L;
  
  protected Map<String, IConfigEntityInformationModel> referencedTaxonomies    = new HashMap<>();
  protected Map<String, IConfigEntityInformationModel> referencedKlasses       = new HashMap<>();
  protected Map<String, IConfigEntityInformationModel> referencedAttributes    = new HashMap<>();
  protected Map<String, IConfigEntityInformationModel> referencedTags          = new HashMap<>();
  protected Map<String, IConfigEntityInformationModel> referencedContexts      = new HashMap<>();
  protected Map<String, IConfigEntityInformationModel> referencedRelationships = new HashMap<>();
  
  @Override
  public Map<String, IConfigEntityInformationModel> getReferencedTaxonomies()
  {
    return referencedTaxonomies;
  }
  
  @Override
  @JsonDeserialize(contentAs = ConfigEntityInformationModel.class)
  public void setReferencedTaxonomies(
      Map<String, IConfigEntityInformationModel> referencedTaxonomies)
  {
    this.referencedTaxonomies = referencedTaxonomies;
  }
  
  @Override
  public Map<String, IConfigEntityInformationModel> getReferencedKlasses()
  {
    return referencedKlasses;
  }
  
  @Override
  @JsonDeserialize(contentAs = ConfigEntityInformationModel.class)
  public void setReferencedKlasses(Map<String, IConfigEntityInformationModel> referencedKlasses)
  {
    this.referencedKlasses = referencedKlasses;
  }
  
  @Override
  public Map<String, IConfigEntityInformationModel> getReferencedAttributes()
  {
    return referencedAttributes;
  }
  
  @Override
  @JsonDeserialize(contentAs = ConfigEntityInformationModel.class)
  public void setReferencedAttributes(
      Map<String, IConfigEntityInformationModel> referencedAttributes)
  {
    this.referencedAttributes = referencedAttributes;
  }
  
  @Override
  public Map<String, IConfigEntityInformationModel> getReferencedTags()
  {
    return referencedTags;
  }
  
  @Override
  @JsonDeserialize(contentAs = ConfigEntityInformationModel.class)
  public void setReferencedTags(Map<String, IConfigEntityInformationModel> referencedTags)
  {
    this.referencedTags = referencedTags;
  }
  
  @Override
  public Map<String, IConfigEntityInformationModel> getReferencedContexts()
  {
    return referencedContexts;
  }
  
  @Override
  @JsonDeserialize(contentAs = ConfigEntityInformationModel.class)
  public void setReferencedContexts(Map<String, IConfigEntityInformationModel> referencedContexts)
  {
    this.referencedContexts = referencedContexts;
  }
  
  @Override
  public Map<String, IConfigEntityInformationModel> getReferencedRelationships()
  {
    return referencedRelationships;
  }
  
  @Override
  @JsonDeserialize(contentAs = ConfigEntityInformationModel.class)
  public void setReferencedRelationships(
      Map<String, IConfigEntityInformationModel> referencedRelationships)
  {
    this.referencedRelationships = referencedRelationships;
  }
}
