package com.cs.core.config.interactor.model.globalpermissions;

import com.cs.core.config.interactor.entity.globalpermissions.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class SaveGlobalPermissionsForRoleModel extends GlobalPermissionsForRole
    implements ISaveGlobalPermissionsForRoleModel {
  
  private static final long                      serialVersionUID = 1L;
  
  protected IGlobalPermissionsForRole            entity;
  protected List<IKlassTaxonomyPermissions>      addedTreePermissions;
  protected List<IKlassTaxonomyPermissions>      modifiedTreePermissions;
  protected List<IPropertyCollectionPermissions> addedPropertyCollectionPermissions;
  protected List<IPropertyCollectionPermissions> modifiedPropertyCollectionPermissions;
  protected List<IPropertyPermissions>           addedPropertyPermissions;
  protected List<IPropertyPermissions>           modifiedPropertyPermissions;
  protected List<String>                         selectedKlassIds;
  protected List<String>                         selectedTaxonomyIds;
  protected String                               selectedPropertyCollectionId;
  
  @Override
  public List<IKlassTaxonomyPermissions> getAddedTreePermissions()
  {
    
    return addedTreePermissions;
  }
  
  @Override
  @JsonDeserialize(contentAs = KlassTaxonomyPermissions.class)
  public void setAddedTreePermissions(List<IKlassTaxonomyPermissions> addedTreePermissions)
  {
    this.addedTreePermissions = addedTreePermissions;
  }
  
  @Override
  public List<IKlassTaxonomyPermissions> getModifiedTreePermissions()
  {
    
    return modifiedTreePermissions;
  }
  
  @Override
  @JsonDeserialize(contentAs = KlassTaxonomyPermissions.class)
  public void setModifiedTreePermissions(List<IKlassTaxonomyPermissions> modifiedTreePermissions)
  {
    this.modifiedTreePermissions = modifiedTreePermissions;
  }
  
  @Override
  public List<IPropertyCollectionPermissions> getAddedPropertyCollectionPermissions()
  {
    
    return addedPropertyCollectionPermissions;
  }
  
  @Override
  @JsonDeserialize(contentAs = PropertyCollectionPermissions.class)
  public void setAddedPropertyCollectionPermissions(
      List<IPropertyCollectionPermissions> addedPropertyCollectionPermissions)
  {
    this.addedPropertyCollectionPermissions = addedPropertyCollectionPermissions;
  }
  
  @Override
  public List<IPropertyCollectionPermissions> getModifiedPropertyCollectionPermissions()
  {
    
    return modifiedPropertyCollectionPermissions;
  }
  
  @Override
  @JsonDeserialize(contentAs = PropertyCollectionPermissions.class)
  public void setModifiedPropertyCollectionPermissions(
      List<IPropertyCollectionPermissions> modifiedPropertyCollectionPermissions)
  {
    this.modifiedPropertyCollectionPermissions = modifiedPropertyCollectionPermissions;
  }
  
  @Override
  public List<IPropertyPermissions> getAddedPropertyPermissions()
  {
    
    return addedPropertyPermissions;
  }
  
  @Override
  @JsonDeserialize(contentAs = PropertyPermissions.class)
  public void setAddedPropertyPermissions(List<IPropertyPermissions> addedPropertyPermissions)
  {
    this.addedPropertyPermissions = addedPropertyPermissions;
  }
  
  @Override
  public List<IPropertyPermissions> getModifiedPropertyPermissions()
  {
    
    return modifiedPropertyPermissions;
  }
  
  @Override
  @JsonDeserialize(contentAs = PropertyPermissions.class)
  public void setModifiedPropertyPermissions(List<IPropertyPermissions> modifiedPropertyPermissions)
  {
    this.modifiedPropertyPermissions = modifiedPropertyPermissions;
  }
  
  @Override
  public List<String> getSelectedKlassIds()
  {
    
    return selectedKlassIds;
  }
  
  @Override
  public void setSelectedKlassIds(List<String> selectedKlassIds)
  {
    this.selectedKlassIds = selectedKlassIds;
  }
  
  @Override
  public List<String> getSelectedTaxonomyIds()
  {
    
    return selectedTaxonomyIds;
  }
  
  @Override
  public void setSelectedTaxonomyIds(List<String> selectedTaxonomyIds)
  {
    this.selectedTaxonomyIds = selectedTaxonomyIds;
  }
  
  @Override
  public String getSelectedPropertyCollectionId()
  {
    
    return selectedPropertyCollectionId;
  }
  
  @Override
  public void setSelectedPropertyCollectionId(String selectedPropertyCollectionId)
  {
    this.selectedPropertyCollectionId = selectedPropertyCollectionId;
  }
}
