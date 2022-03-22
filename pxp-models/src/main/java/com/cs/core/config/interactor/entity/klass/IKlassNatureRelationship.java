package com.cs.core.config.interactor.entity.klass;

import java.util.List;

import com.cs.core.config.interactor.entity.propertycollection.IPropertyCollection;
import com.cs.core.config.interactor.entity.relationship.IRelationship;
import com.cs.core.runtime.interactor.entity.datarule.ITaxonomyConflict.Setting;

public interface IKlassNatureRelationship extends IRelationship {
  
  public static final String MAX_NO_OF_ITEMS              = "maxNoOfItems";
  public static final String PROPERTY_COLLECTION          = "propertyCollection";
  public static final String RELATIONSHIP_TYPE            = "relationshipType";
  public static final String CONTEXT_TAGS                 = "contextTags";
  public static final String RHYTHM                       = "rhythm";
  public static final String AUTO_CREATE_SETTINGS         = "autoCreateSettings";
  public static final String TAXONOMY_INHERITANCE_SETTING = "taxonomyInheritanceSetting";

  
  public int getMaxNoOfItems();
  
  public void setMaxNoOfItems(int maxNoOfItems);
  
  public IPropertyCollection getPropertyCollection();
  
  public void setPropertyCollection(IPropertyCollection propertyCollection);
  
  public String getRelationshipType();
  
  public void setRelationshipType(String relationshipType);
  
  public List<String> getContextTags();
  
  public void setContextTags(List<String> contextTags);
  
  public String getRhythm();
  
  public void setRhythm(String rhythm);
  
  public Boolean getAutoCreateSettings();
  
  public void setAutoCreateSettings(Boolean autoCreateSettings);
  
  public Setting getTaxonomyInheritanceSetting();

  public void setTaxonomyInheritanceSetting(Setting taxonomyInheritanceSetting);
  
}
