package com.cs.core.config.interactor.model.datarule;

import java.util.Map;

import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.organization.IOrganization;
import com.cs.core.config.interactor.entity.role.IRole;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.language.ILanguageModel;
import com.cs.core.config.interactor.model.organization.IReferencedEndpointModel;
import com.cs.core.config.interactor.model.taxonomy.IReferencedArticleTaxonomyModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelNatureTypeModel;

public interface IConfigDetailsForDataRuleModel extends IModel {
  
  public static final String REFERENCED_ATTRIBUTES   = "referencedAttributes";
  public static final String REFERENCED_TAGS         = "referencedTags";
  public static final String REFERENCED_ROLES        = "referencedRoles";
  public static final String REFERENCED_KLASSES      = "referencedKlasses";
  public static final String REFERENCED_TAXONOMIES   = "referencedTaxonomies";
  public static final String REFERENCED_ORANIZATIONS = "referencedOraganizations";
  public static final String REFERENCED_ENDPOINTS    = "referencedEndpoints";
  public static final String REFERENCED_LANGUAGES    = "referencedLanguages";
  
  public Map<String, IAttribute> getReferencedAttributes();
  
  public void setReferencedAttributes(Map<String, IAttribute> referencedAttributes);
  
  public Map<String, ITag> getReferencedTags();
  
  public void setReferencedTags(Map<String, ITag> referencedTags);
  
  public Map<String, IRole> getReferencedRoles();
  
  public void setReferencedRoles(Map<String, IRole> referencedRoles);
  
  public Map<String, IIdLabelNatureTypeModel> getReferencedKlasses();
  public void setReferencedKlasses(Map<String, IIdLabelNatureTypeModel> referencedKlasses);

  public Map<String, IReferencedArticleTaxonomyModel> getReferencedTaxonomies();
  
  public void setReferencedTaxonomies(
      Map<String, IReferencedArticleTaxonomyModel> referencedTaxonomies);
  
  public Map<String, IOrganization> getReferencedOraganizations();
  
  public void setReferencedOraganizations(Map<String, IOrganization> referencedOraganizations);
  
  public Map<String, IReferencedEndpointModel> getReferencedEndpoints();
  
  public void setReferencedEndpoints(Map<String, IReferencedEndpointModel> referencedEndpoints);
  
  public Map<String, ILanguageModel> getReferencedLanguages();
  
  public void setReferencedLanguages(Map<String, ILanguageModel> referencedLanguages);
}
