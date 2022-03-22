package com.cs.core.config.interactor.model.klass;

import java.util.List;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import com.cs.core.config.interactor.model.propertycollection.IAddedTabModel;
import com.cs.core.config.interactor.model.relationship.IModifiedRelationshipPropertyModel;

public interface IModifiedNatureRelationshipModel extends IEntity {
  
  public static final String LABEL                             = "label";
  public static final String MAX_NO_OF_ITEMS                   = "maxNoOfItems";
  public static final String RELATIONSHIP_TYPE                 = "relationshipType";
  public static final String ADDED_PROPERTY_COLLECTION         = "addedPropertyCollection";
  public static final String DELETED_PROPERTY_COLLECTION       = "deletedPropertyCollection";
  public static final String ADDED_ATTRIBUTES                  = "addedAttributes";
  public static final String MODIFIED_ATTRIBUTES               = "modifiedAttributes";
  public static final String DELETED_ATTRIBUTES                = "deletedAttributes";
  public static final String ADDED_TAGS                        = "addedTags";
  public static final String MODIFIED_TAGS                     = "modifiedTags";
  public static final String DELETED_TAGS                      = "deletedTags";
  public static final String ADDED_CONTEXT_TAGS                = "addedContextTags";
  public static final String DELETED_CONTEXT_TAGS              = "deletedContextTags";
  public static final String RHYTHM                            = "rhythm";
  public static final String AUTO_CREATE_SETTINGS              = "autoCreateSettings";
  public static final String ADDED_TAB                         = "addedTab";
  public static final String DELETED_TAB                       = "deletedTab";
  public static final String ADDED_RELATIONSHIP_INHERITANCE    = "addedRelationshipInheritance";
  public static final String MODIFIED_RELATIONSHIP_INHERITANCE = "modifiedRelationshipInheritance";
  public static final String DELETED_RELATIONSHIP_INHERITANCE  = "deletedRelationshipInheritance";
  public static final String TAXONOMY_INHERITANCE_SETTING       = "taxonomyInheritanceSetting";
  public static final String ENABLE_AFTER_SAVE                  = "enableAfterSave";
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public int getMaxNoOfItems();
  
  public void setMaxNoOfItems(int maxNoOfItems);
  
  public String getRelationshipType();
  
  public void setRelationshipType(String relationshipType);
  
  public String getAddedPropertyCollection();
  
  public void setAddedPropertyCollection(String addedPropertyCollection);
  
  public String getDeletedPropertyCollection();
  
  public void setDeletedPropertyCollection(String deletedPropertyCollection);
  
  public List<IModifiedRelationshipPropertyModel> getAddedAttributes();
  
  public void setAddedAttributes(List<IModifiedRelationshipPropertyModel> addedAttributes);
  
  public List<IModifiedRelationshipPropertyModel> getModifiedAttributes();
  
  public void setModifiedAttributes(List<IModifiedRelationshipPropertyModel> modifiedAttributes);
  
  public List<IModifiedRelationshipPropertyModel> getDeletedAttributes();
  
  public void setDeletedAttributes(List<IModifiedRelationshipPropertyModel> deletedAttributes);
  
  public List<IModifiedRelationshipPropertyModel> getAddedTags();
  
  public void setAddedTags(List<IModifiedRelationshipPropertyModel> addedTags);
  
  public List<IModifiedRelationshipPropertyModel> getModifiedTags();
  
  public void setModifiedTags(List<IModifiedRelationshipPropertyModel> modifiedTags);
  
  public List<IModifiedRelationshipPropertyModel> getDeletedTags();
  
  public void setDeletedTags(List<IModifiedRelationshipPropertyModel> deletedTags);
  
  public List<String> getAddedContextTags();
  
  public void setAddedContextTags(List<String> addedContextTags);
  
  public List<String> getDeletedContextTags();
  
  public void setDeletedContextTags(List<String> deletedContextTags);
  
  public String getRhythm();
  
  public void setRhythm(String rhythm);
  
  public Boolean getAutoCreateSettings();
  
  public void setAutoCreateSettings(Boolean autoCreateSettings);
  
  public IAddedTabModel getAddedTab();
  
  public void setAddedTab(IAddedTabModel addedTab);
  
  public String getDeletedTab();
  
  public void setDeletedTab(String deletedTab);
  
  public List<IModifiedRelationshipPropertyModel> getAddedRelationshipInheritance();
  
  public void setAddedRelationshipInheritance(
      List<IModifiedRelationshipPropertyModel> addedRelationshipInheritance);
  
  public List<IModifiedRelationshipPropertyModel> getModifiedRelationshipInheritance();
  
  public void setModifiedRelationshipInheritance(
      List<IModifiedRelationshipPropertyModel> modifiedRelationshipInheritance);
  
  public List<String> getDeletedRelationshipInheritance();
  
  public void setDeletedRelationshipInheritance(List<String> deletedRelationshipInheritance);
  
  public String getTaxonomyInheritanceSetting();

  public void setTaxonomyInheritanceSetting(String taxonomyInheritanceSetting);

  public Boolean getEnableAfterSave();
  
  public void setEnableAfterSave(Boolean enableAfterSave);
}
