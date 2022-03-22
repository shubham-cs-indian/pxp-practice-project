package com.cs.core.runtime.interactor.model.klassinstance;

import java.util.ArrayList;
import java.util.List;

public class KlassInstanceInfoModel implements IKlassInstanceInfoModel {
  
  private static final long serialVersionUID = 1L;
  protected String          klassInstanceId;
  protected String          basetype;
  protected String          organisationId;
  protected String          physicalCatalogId;
  protected String          endpointId;
  protected List<String>    types;
  protected List<String>    taxonomyIds;
  protected List<String>    selectedTaxonomyIds;
  
  @Override
  public String getKlassInstanceId()
  {
    return klassInstanceId;
  }
  
  @Override
  public void setKlassInstanceId(String klassInstanceId)
  {
    this.klassInstanceId = klassInstanceId;
  }
  
  @Override
  public String getBasetype()
  {
    return basetype;
  }
  
  @Override
  public void setBasetype(String basetype)
  {
    this.basetype = basetype;
  }
  
  @Override
  public List<String> getTypes()
  {
    if (types == null) {
      types = new ArrayList<>();
    }
    return types;
  }
  
  @Override
  public void setTypes(List<String> types)
  {
    this.types = types;
  }
  
  @Override
  public List<String> getTaxonomyIds()
  {
    if (taxonomyIds == null) {
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
  public List<String> getSelectedTaxonomyIds()
  {
    if (selectedTaxonomyIds == null) {
      selectedTaxonomyIds = new ArrayList<>();
    }
    return selectedTaxonomyIds;
  }
  
  @Override
  public void setSelectedTaxonomyIds(List<String> selectedTaxonomyIds)
  {
    this.selectedTaxonomyIds = selectedTaxonomyIds;
  }
  
  @Override
  public String getOrganisationId()
  {
    return organisationId;
  }
  
  @Override
  public void setOrganisationId(String organisationId)
  {
    this.organisationId = organisationId;
  }
  
  @Override
  public String getPhysicalCatalogId()
  {
    return physicalCatalogId;
  }
  
  @Override
  public void setPhysicalCatalogId(String physicalCatalogId)
  {
    this.physicalCatalogId = physicalCatalogId;
  }
  
  @Override
  public String getEndpointId()
  {
    return endpointId;
  }
  
  @Override
  public void setEndpointId(String endpointId)
  {
    this.endpointId = endpointId;
  }
}
