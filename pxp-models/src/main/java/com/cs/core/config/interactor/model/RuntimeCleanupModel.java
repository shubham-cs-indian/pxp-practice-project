package com.cs.core.config.interactor.model;


import java.util.ArrayList;
import java.util.List;

public class RuntimeCleanupModel implements IRuntimeCleanupModel {
  
  private static final long serialVersionUID = 1L;
  
  protected String                    id;
  protected String                    baseType;
  protected List<String>              klassIds;
  protected List<String>              taxonomyIds;
  protected List<String>              removedKlassIds;
  protected List<String>              removedTaxonomyIds;
  protected List<String>              languageCodes;
  protected List<String>              deletedPropertyInstanceIds;
  
  @Override
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public String getBaseType()
  {
    return baseType;
  }
  
  @Override
  public void setBaseType(String baseType)
  {
    this.baseType = baseType;
  }
  
  @Override
  public List<String> getKlassIds()
  {
    if(klassIds == null) {
      klassIds = new ArrayList<>();
    }
    return klassIds;
  }
  
  @Override
  public void setKlassIds(List<String> klassIds)
  {
    this.klassIds = klassIds;
  }
  
  @Override
  public List<String> getTaxonomyIds()
  {
    if(taxonomyIds == null) {
      taxonomyIds = new ArrayList<>();
    }
    return taxonomyIds;
  }
  
  @Override
  public void setTaxonomyIds(List<String> taxonomyIds)
  {
    this.taxonomyIds = taxonomyIds;
  }

  @Override
  public List<String> getRemovedKlassIds()
  {
    if(removedKlassIds == null) {
      removedKlassIds = new ArrayList<>();
    }
    return removedKlassIds;
  }

  @Override
  public void setRemovedKlassIds(List<String> removedKlassIds)
  {
    this.removedKlassIds = removedKlassIds;
  }

  @Override
  public List<String> getRemovedTaxonomyIds()
  {
    if(removedTaxonomyIds == null) {
      removedTaxonomyIds = new ArrayList<>();
    }
    return removedTaxonomyIds;
  }

  @Override
  public void setRemovedTaxonomyIds(List<String> removedTaxonomyIds)
  {
    this.removedTaxonomyIds = removedTaxonomyIds;
  }

  @Override
  public List<String> getLanguageCodes()
  {
    if(languageCodes == null) {
      languageCodes = new ArrayList<String>();
    }
    return languageCodes;
  }

  @Override
  public void setLanguageCodes(List<String> languageCodes)
  {
    this.languageCodes = languageCodes;
  }
  
  @Override
  public List<String> getDeletedPropertyInstanceIds()
  {
    if (deletedPropertyInstanceIds == null) {
      deletedPropertyInstanceIds = new ArrayList<>();
    }
    return deletedPropertyInstanceIds;
  }
  
  @Override
  public void setDeletedPropertyInstanceIds(List<String> deletedPropertyInstanceIds)
  {
    this.deletedPropertyInstanceIds = deletedPropertyInstanceIds;
  }
  
}