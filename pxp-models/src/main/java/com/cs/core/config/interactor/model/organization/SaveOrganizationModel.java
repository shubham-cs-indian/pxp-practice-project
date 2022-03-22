package com.cs.core.config.interactor.model.organization;

import com.cs.core.config.interactor.entity.organization.IOrganization;
import com.cs.core.config.interactor.entity.organization.Organization;

import java.util.ArrayList;
import java.util.List;

public class SaveOrganizationModel extends OrganizationModel implements ISaveOrganizationModel {
  
  private static final long serialVersionUID = 1L;
  protected List<String>    deletedTaxonomyIds;
  protected List<String>    addedTaxonomyIds;
  protected List<String>    deletedKlassIds;
  protected List<String>    addedKlassIds;
  protected List<String>    deletedEndpointIds;
  protected List<String>    addedEndpointIds;
  protected List<String>    addedSystemIds;
  protected List<String>    deletedSystemIds;
  
  public SaveOrganizationModel()
  {
    organization = new Organization();
  }
  
  public SaveOrganizationModel(IOrganization organization)
  {
    this.organization = organization;
  }
  
  @Override
  public List<String> getDeletedTaxonomyIds()
  {
    if (deletedTaxonomyIds == null) {
      deletedTaxonomyIds = new ArrayList<>();
    }
    return deletedTaxonomyIds;
  }
  
  @Override
  public void setDeletedTaxonomyIds(List<String> deletedTaxonomyIds)
  {
    this.deletedTaxonomyIds = deletedTaxonomyIds;
  }
  
  @Override
  public List<String> getAddedTaxonomyIds()
  {
    if (addedTaxonomyIds == null) {
      addedTaxonomyIds = new ArrayList<>();
    }
    return addedTaxonomyIds;
  }
  
  @Override
  public void setAddedTaxonomyIds(List<String> addedTaxonomyIds)
  {
    this.addedTaxonomyIds = addedTaxonomyIds;
  }
  
  @Override
  public List<String> getDeletedEndpointIds()
  {
    if (deletedEndpointIds == null) {
      deletedEndpointIds = new ArrayList<>();
    }
    return deletedEndpointIds;
  }
  
  @Override
  public void setDeletedEndpointIds(List<String> deletedEndpointIds)
  {
    this.deletedEndpointIds = deletedEndpointIds;
  }
  
  @Override
  public List<String> getAddedEndpointIds()
  {
    if (addedEndpointIds == null) {
      addedEndpointIds = new ArrayList<>();
    }
    return addedEndpointIds;
  }
  
  @Override
  public void getAddedEndpointIds(List<String> addedEndpointIds)
  {
    this.addedEndpointIds = addedEndpointIds;
  }
  
  @Override
  public List<String> getDeletedKlassIds()
  {
    if (deletedKlassIds == null) {
      deletedKlassIds = new ArrayList<>();
    }
    return deletedKlassIds;
  }
  
  @Override
  public void setDeletedKlassIds(List<String> deletedKlassIds)
  {
    this.deletedKlassIds = deletedKlassIds;
  }
  
  @Override
  public List<String> getAddedKlassIds()
  {
    if (addedKlassIds == null) {
      addedKlassIds = new ArrayList<>();
    }
    return addedKlassIds;
  }
  
  @Override
  public void setAddedKlassIds(List<String> addedKlassIds)
  {
    this.addedKlassIds = addedKlassIds;
  }
  
  @Override
  public List<String> getDeletedSystemIds()
  {
    if (deletedSystemIds == null) {
      deletedSystemIds = new ArrayList<>();
    }
    return deletedSystemIds;
  }
  
  @Override
  public void setDeletedSystemIds(List<String> deletedSystemIds)
  {
    this.deletedSystemIds = deletedSystemIds;
  }
  
  @Override
  public List<String> getAddedSystemIds()
  {
    if (addedSystemIds == null) {
      addedSystemIds = new ArrayList<>();
    }
    return addedSystemIds;
  }
  
  @Override
  public void setaddedSystemIds(List<String> addedSystemIds)
  {
    this.addedSystemIds = addedSystemIds;
  }
}
