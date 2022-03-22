package com.cs.core.config.interactor.model.articleimportcomponent;

import com.cs.core.runtime.interactor.model.klassinstance.GetKlassInstanceTreeStrategyModel;
import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstanceTreeStrategyModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class KlassInstanceExportAllRequestModel implements IKlassInstanceExportAllRequestModel {
  
  private static final long                    serialVersionUID = 1L;
  protected List<String>                       ids;
  protected int                                size;
  protected int                                from;
  protected List<String>                       types;
  protected List<String>                       taxonomyIds;
  protected String                             endpointId;
  protected String                             docType;
  protected String                             organizationId;
  protected String                             physicalCatalogId;
  protected String                             natureType       = "";
  protected List<String>                       nonNatureTypes   = new ArrayList<>();
  protected IGetKlassInstanceTreeStrategyModel filterInfo;
  protected Boolean                            isFilterApplied;
  
  public IGetKlassInstanceTreeStrategyModel getFilterInfo()
  {
    return filterInfo;
  }
  
  @JsonDeserialize(as = GetKlassInstanceTreeStrategyModel.class)
  public void setFilterInfo(IGetKlassInstanceTreeStrategyModel filterInfo)
  {
    this.filterInfo = filterInfo;
  }
  
  @Override
  public List<String> getIds()
  {
    if (ids == null) {
      return new ArrayList<>();
    }
    return ids;
  }
  
  @Override
  public void setIds(List<String> ids)
  {
    this.ids = ids;
  }
  
  @Override
  public int getSize()
  {
    return size;
  }
  
  @Override
  public void setSize(int size)
  {
    this.size = size;
  }
  
  @Override
  public int getFrom()
  {
    return from;
  }
  
  @Override
  public void setFrom(int from)
  {
    this.from = from;
  }
  
  @Override
  public List<String> getTypes()
  {
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
    return taxonomyIds;
  }
  
  @Override
  public void setTaxonomyIds(List<String> taxonomyIds)
  {
    this.taxonomyIds = taxonomyIds;
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
  
  @Override
  public String getDocType()
  {
    return docType;
  }
  
  @Override
  public void setDocType(String docType)
  {
    this.docType = docType;
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
  public String getOrganizationId()
  {
    return organizationId;
  }
  
  @Override
  public void setOrganizationId(String organizationId)
  {
    this.organizationId = organizationId;
  }
  
  @Override
  public String getNatureType()
  {
    return natureType;
  }
  
  @Override
  public void setNatureType(String natureType)
  {
    this.natureType = natureType == null ? "" : natureType;
  }
  
  @Override
  public List<String> getNonNatureTypes()
  {
    return nonNatureTypes;
  }
  
  @Override
  public void setNonNatureTypes(List<String> nonNatureTypes)
  {
    this.nonNatureTypes = nonNatureTypes == null ? new ArrayList<>() : nonNatureTypes;
  }
  
  public Boolean getIsFilterApplied()
  {
    return isFilterApplied;
  }
  
  public void setIsFilterApplied(Boolean isFilteredExport)
  {
    this.isFilterApplied = isFilteredExport;
  }
}
