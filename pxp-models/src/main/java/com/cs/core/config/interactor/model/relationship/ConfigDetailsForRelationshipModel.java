package com.cs.core.config.interactor.model.relationship;

import com.cs.core.config.interactor.model.configdetails.ConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.core.runtime.interactor.model.configuration.IdLabelCodeModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Map;

public class ConfigDetailsForRelationshipModel implements IConfigDetailsForRelationshipModel {
  
  private static final long                            serialVersionUID = 1L;
  
  protected Map<String, IConfigEntityInformationModel> referencedAttributes;
  protected Map<String, IConfigEntityInformationModel> referencedTags;
  protected Map<String, IConfigEntityInformationModel> referencedKlasses;
  protected Map<String, IConfigEntityInformationModel> referencedContexts;
  protected Map<String, IIdLabelCodeModel>             referencedTabs;
  
  @Override
  public Map<String, IConfigEntityInformationModel> getReferencedAttributes()
  {
    return referencedAttributes;
  }
  
  @JsonDeserialize(contentAs = ConfigEntityInformationModel.class)
  @Override
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
  
  @JsonDeserialize(contentAs = ConfigEntityInformationModel.class)
  @Override
  public void setReferencedTags(Map<String, IConfigEntityInformationModel> referencedTags)
  {
    this.referencedTags = referencedTags;
  }
  
  @Override
  public Map<String, IConfigEntityInformationModel> getReferencedKlasses()
  {
    return referencedKlasses;
  }
  
  @JsonDeserialize(contentAs = ConfigEntityInformationModel.class)
  @Override
  public void setReferencedKlasses(Map<String, IConfigEntityInformationModel> referencedKlasses)
  {
    this.referencedKlasses = referencedKlasses;
  }
  
  @Override
  public Map<String, IConfigEntityInformationModel> getReferencedContexts()
  {
    return referencedContexts;
  }
  
  @JsonDeserialize(contentAs = ConfigEntityInformationModel.class)
  @Override
  public void setReferencedContexts(Map<String, IConfigEntityInformationModel> referencedContexts)
  {
    this.referencedContexts = referencedContexts;
  }
  
  @Override
  public Map<String, IIdLabelCodeModel> getReferencedTabs()
  {
    return referencedTabs;
  }
  
  @Override
  @JsonDeserialize(contentAs = IdLabelCodeModel.class)
  public void setReferencedTabs(Map<String, IIdLabelCodeModel> referencedTabs)
  {
    this.referencedTabs = referencedTabs;
  }
}
