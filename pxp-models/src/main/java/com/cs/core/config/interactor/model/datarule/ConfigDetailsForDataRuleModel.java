package com.cs.core.config.interactor.model.datarule;

import java.util.Map;

import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.organization.IOrganization;
import com.cs.core.config.interactor.entity.organization.Organization;
import com.cs.core.config.interactor.entity.role.IRole;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.language.ILanguageModel;
import com.cs.core.config.interactor.model.language.LanguageModel;
import com.cs.core.config.interactor.model.organization.IReferencedEndpointModel;
import com.cs.core.config.interactor.model.organization.ReferencedEndpointModel;
import com.cs.core.config.interactor.model.taxonomy.IReferencedArticleTaxonomyModel;
import com.cs.core.config.interactor.model.taxonomy.ReferencedArticleTaxonomyModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelNatureTypeModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IdLabelNatureTypeModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class ConfigDetailsForDataRuleModel implements IConfigDetailsForDataRuleModel {
  
  private static final long                              serialVersionUID = 1L;
  
  protected Map<String, IAttribute>                      referencedAttributes;
  protected Map<String, ITag>                            referencedTags;
  protected Map<String, IRole>                           referencedRoles;
  protected Map<String, IIdLabelNatureTypeModel>         referencedKlasses;
  protected Map<String, IReferencedArticleTaxonomyModel> referencedTaxonomies;
  protected Map<String, IOrganization>                   referencedOraganizations;
  protected Map<String, IReferencedEndpointModel>        referencedEndpoints;
  protected Map<String, ILanguageModel>                  referencedLanguages;
  
  @Override
  public Map<String, IAttribute> getReferencedAttributes()
  {
    return referencedAttributes;
  }
  
  @Override
  public void setReferencedAttributes(Map<String, IAttribute> referencedAttributes)
  {
    this.referencedAttributes = referencedAttributes;
  }
  
  @Override
  public Map<String, ITag> getReferencedTags()
  {
    return referencedTags;
  }
  
  @Override
  public void setReferencedTags(Map<String, ITag> referencedTags)
  {
    this.referencedTags = referencedTags;
  }
  
  @Override
  public Map<String, IRole> getReferencedRoles()
  {
    return referencedRoles;
  }
  
  @Override
  public void setReferencedRoles(Map<String, IRole> referencedRoles)
  {
    this.referencedRoles = referencedRoles;
  }
  
  @Override
  public Map<String, IIdLabelNatureTypeModel> getReferencedKlasses()
  {
    return referencedKlasses;
  }
  
  @JsonDeserialize(contentAs = IdLabelNatureTypeModel.class)
  @Override
  public void setReferencedKlasses(Map<String, IIdLabelNatureTypeModel> referencedKlasses)
  {
    this.referencedKlasses = referencedKlasses;
  }
  
  @Override
  public Map<String, IReferencedArticleTaxonomyModel> getReferencedTaxonomies()
  {
    return referencedTaxonomies;
  }
  
  @JsonDeserialize(contentAs = ReferencedArticleTaxonomyModel.class)
  @Override
  public void setReferencedTaxonomies(
      Map<String, IReferencedArticleTaxonomyModel> referencedTaxonomies)
  {
    this.referencedTaxonomies = referencedTaxonomies;
  }
  
  @Override
  public Map<String, IOrganization> getReferencedOraganizations()
  {
    return referencedOraganizations;
  }
  
  @JsonDeserialize(contentAs = Organization.class)
  @Override
  public void setReferencedOraganizations(Map<String, IOrganization> referencedOraganizations)
  {
    this.referencedOraganizations = referencedOraganizations;
  }
  
  @Override
  public Map<String, IReferencedEndpointModel> getReferencedEndpoints()
  {
    return referencedEndpoints;
  }
  
  @JsonDeserialize(contentAs = ReferencedEndpointModel.class)
  @Override
  public void setReferencedEndpoints(Map<String, IReferencedEndpointModel> referencedEndpoints)
  {
    this.referencedEndpoints = referencedEndpoints;
  }
  
  @Override
  public Map<String, ILanguageModel> getReferencedLanguages()
  {
    return referencedLanguages;
  }
  
  @JsonDeserialize(contentAs = LanguageModel.class)
  @Override
  public void setReferencedLanguages(Map<String, ILanguageModel> referencedLanguages)
  {
    this.referencedLanguages = referencedLanguages;
  }
}
