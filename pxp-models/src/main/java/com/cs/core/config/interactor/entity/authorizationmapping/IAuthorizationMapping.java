package com.cs.core.config.interactor.entity.authorizationmapping;

import com.cs.core.config.interactor.entity.configuration.base.IConfigMasterEntity;

public interface IAuthorizationMapping extends IConfigMasterEntity {
  
  public static final String IS_ALL_ATTRIBUTES_SELECTED              = "isAllAttributesSelected";
  public static final String IS_ALL_TAGS_SELECTED                    = "isAllTagsSelected";
  public static final String IS_ALL_CLASSES_SELECTED                 = "isAllClassesSelected";
  public static final String IS_ALL_CONTEXTS_SELECTED                = "isAllContextsSelected";
  public static final String IS_ALL_TAXONOMIES_SELECTED              = "isAllTaxonomiesSelected";
  public static final String IS_ALL_RELATIONSHIPS_SELECTED           = "isAllRelationshipsSelected";
  
  public static final String BLANK_VALUES_ACCEPTED_FOR_ATTRIBUTES    = "isBlankValueAcceptedForAttributes";
  public static final String BLANK_VALUES_ACCEPTED_FOR_TAGS          = "isBlankValueAcceptedForTags";
  public static final String BLANK_VALUES_ACCEPTED_FOR_CLASSES       = "isBlankValueAcceptedForClasses";
  public static final String BLANK_VALUES_ACCEPTED_FOR_CONTEXTS      = "isBlankValueAcceptedForContexts";
  public static final String BLANK_VALUES_ACCEPTED_FOR_TAXONOMIES    = "isBlankValueAcceptedForTaxonomies";
  public static final String BLANK_VALUES_ACCEPTED_FOR_RELATIONSHIPS = "isBlankValueAcceptedForRelationships";
  
  public Boolean getIsAllAttributesSelected();
  
  public void setIsAllAttributesSelected(Boolean isAllAttributesSelected);
  
  public Boolean getIsAllTagsSelected();
  
  public void setIsAllTagsSelected(Boolean isAllTagsSelected);
  
  public Boolean getIsAllClassesSelected();
  
  public void setIsAllClassesSelected(Boolean isAllClassesSelected);
  
  public Boolean getIsAllContextsSelected();
  
  public void setIsAllContextsSelected(Boolean isAllContextsSelected);
  
  public Boolean getIsAllTaxonomiesSelected();
  
  public void setIsAllTaxonomiesSelected(Boolean isAllTaxonomiesSelected);
  
  public Boolean getIsAllRelationshipsSelected();
  
  public void setIsAllRelationshipsSelected(Boolean isAllRelationshipsSelected);
  
  public Boolean getIsBlankValueAcceptedForAttributes();
  
  public void setIsBlankValueAcceptedForAttributes(Boolean isBlankValueAcceptedForAttributes);
  
  public Boolean getIsBlankValueAcceptedForTags();
  
  public void setIsBlankValueAcceptedForTags(Boolean isBlankValueAcceptedForTags);
  
  public Boolean getIsBlankValueAcceptedForClasses();
  
  public void setIsBlankValueAcceptedForClasses(Boolean isBlankValueAcceptedForClasses);
  
  public Boolean getIsBlankValueAcceptedForContexts();
  
  public void setIsBlankValueAcceptedForContexts(Boolean isBlankValueAcceptedForContexts);
  
  public Boolean getIsBlankValueAcceptedForTaxonomies();
  
  public void setIsBlankValueAcceptedForTaxonomies(Boolean isBlankValueAcceptedForTaxonomies);
  
  public Boolean getIsBlankValueAcceptedForRelationships();
  
  public void setIsBlankValueAcceptedForRelationships(Boolean isBlankValueAcceptedForRelationships);
}
