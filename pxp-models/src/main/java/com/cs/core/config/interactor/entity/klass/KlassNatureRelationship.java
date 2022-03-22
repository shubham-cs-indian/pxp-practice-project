package com.cs.core.config.interactor.entity.klass;

import java.util.List;

import com.cs.core.config.interactor.entity.propertycollection.IPropertyCollection;
import com.cs.core.config.interactor.entity.propertycollection.PropertyCollection;
import com.cs.core.config.interactor.entity.relationship.Relationship;
import com.cs.core.runtime.interactor.entity.datarule.ITaxonomyConflict.Setting;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class KlassNatureRelationship extends Relationship implements IKlassNatureRelationship {
  
  private static final long     serialVersionUID   = 1L;
  protected int                 maxNoOfItems;
  protected IPropertyCollection propertyCollection;
  protected String              relationshipType;
  protected List<String>        contextTags;
  protected String              rhythm;
  protected Boolean             autoCreateSettings = false;
  protected Setting             taxonomyInheritanceSetting;

  @Override
  public int getMaxNoOfItems()
  {
    
    return maxNoOfItems;
  }
  
  @Override
  public void setMaxNoOfItems(int maxNoOfItems)
  {
    this.maxNoOfItems = maxNoOfItems;
  }
  
  @Override
  public IPropertyCollection getPropertyCollection()
  {
    
    return propertyCollection;
  }
  
  @Override
  @JsonDeserialize(as = PropertyCollection.class)
  public void setPropertyCollection(IPropertyCollection propertyCollection)
  {
    this.propertyCollection = propertyCollection;
  }
  
  @Override
  public String getRelationshipType()
  {
    
    return relationshipType;
  }
  
  @Override
  public void setRelationshipType(String relationshipType)
  {
    this.relationshipType = relationshipType;
  }
  
  @Override
  public List<String> getContextTags()
  {
    return contextTags;
  }
  
  @Override
  public void setContextTags(List<String> contextTags)
  {
    this.contextTags = contextTags;
  }
  
  @Override
  public String getRhythm()
  {
    return rhythm;
  }
  
  @Override
  public void setRhythm(String rhythm)
  {
    this.rhythm = rhythm;
  }
  
  @Override
  public Boolean getAutoCreateSettings()
  {
    return autoCreateSettings;
  }
  
  @Override
  public void setAutoCreateSettings(Boolean autoCreateSettings)
  {
    this.autoCreateSettings = autoCreateSettings;
  }

  @Override
  public Setting getTaxonomyInheritanceSetting()
  {
    if(taxonomyInheritanceSetting == null) {
      taxonomyInheritanceSetting = Setting.off;
    }
    return taxonomyInheritanceSetting;
  }

  @Override
  public void setTaxonomyInheritanceSetting(Setting taxonomyInheritanceSetting)
  {
    this.taxonomyInheritanceSetting = taxonomyInheritanceSetting;
  }
}
