package com.cs.core.runtime.interactor.model.configdetails.instancetree;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.core.config.interactor.entity.attribute.AbstractAttribute;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.tag.Tag;
import com.cs.core.config.interactor.model.xray.XRayConfigDetailsModel;
import com.cs.core.runtime.interactor.model.configdetails.IXRayConfigDetailsModel;
import com.cs.core.runtime.interactor.model.instancetree.INewApplicableFilterModel;
import com.cs.core.runtime.interactor.model.instancetree.NewApplicableFilterModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;


public class ConfigDetailsForGetNewInstanceTreeModel implements IConfigDetailsForGetNewInstanceTreeModel {
  
  private static final long                 serialVersionUID = 1L;
  protected List<String>                    allowedEntites;
  protected Set<String>                     klassIdsWithRP;
  protected Set<String>                     taxonomyIdsHavingRP;
  protected IXRayConfigDetailsModel         xRayConfigDetails;
  protected List<String>                    klassIdsForKPI;
  protected List<String>                    taxonomyIdsForKPI;
  protected List<String>                    side2LinkedVariantKrIds;
  protected List<String>                    searchableAttributes;
  protected List<String>                    translatableAttributes;
  protected Map<String, IAttribute>         referencedAttributes;
  protected Map<String, ITag>               referencedTags;
  protected List<INewApplicableFilterModel> filterData;
  protected List<String>                    linkedVariantCodes;
  protected List<String>                    majorTaxonomyIds;
  
  @Override
  public List<String> getAllowedEntities()
  {
    if(allowedEntites == null) {
      allowedEntites = new ArrayList<>();
    }
    return allowedEntites;
  }
  
  @Override
  public void setAllowedEntities(List<String> allowedEntites)
  {
    this.allowedEntites = allowedEntites;
  }
  
  @Override
  public Set<String> getKlassIdsHavingRP()
  {
    if(klassIdsWithRP == null) {
      klassIdsWithRP = new HashSet<>();
    }
    return klassIdsWithRP;
  }
  
  @Override
  public void setKlassIdsHavingRP(Set<String> klassIdsWithRP)
  {
    this.klassIdsWithRP = klassIdsWithRP;
  }
  
  @Override
  public Set<String> getTaxonomyIdsHavingRP()
  {
    if(taxonomyIdsHavingRP == null) {
      taxonomyIdsHavingRP = new HashSet<>();
    }
    return taxonomyIdsHavingRP;
  }
  
  @Override
  public void setTaxonomyIdsHavingRP(Set<String> taxonomyIdsHavingRP)
  {
    this.taxonomyIdsHavingRP = taxonomyIdsHavingRP;
  }
  
  @Override
  public List<String> getKlassIdsForKPI()
  {
    if(klassIdsForKPI == null) {
      klassIdsForKPI = new ArrayList<>();
    }
    return klassIdsForKPI;
  }
  
  @Override
  public void setKlassIdsForKPI(List<String> klassIdsForKPI)
  {
    this.klassIdsForKPI = klassIdsForKPI;
  }
  
  @Override
  public List<String> getTaxonomyIdsForKPI()
  {
    if(taxonomyIdsForKPI == null) {
      taxonomyIdsForKPI = new ArrayList<>();
    }
    return taxonomyIdsForKPI;
  }
  
  @Override
  public void setTaxonomyIdsForKPI(List<String> taxonomyIdsForKPI)
  {
    this.taxonomyIdsForKPI = taxonomyIdsForKPI;
  }
  
  @Override
  public List<String> getSide2LinkedVariantKrIds()
  {
    if(side2LinkedVariantKrIds == null) {
      side2LinkedVariantKrIds = new ArrayList<>();
    }
    return side2LinkedVariantKrIds;
  }
  
  @Override
  public void setSide2LinkedVariantKrIds(List<String> side2LinkedVariantKrIds)
  {
    this.side2LinkedVariantKrIds = side2LinkedVariantKrIds;
  }
  
  @Override
  public List<String> getSearchableAttributeIds()
  {
    if(searchableAttributes == null) {
      searchableAttributes = new ArrayList<>();
    }
    return searchableAttributes;
  }
  
  @Override
  public void setSearchableAttributeIds(List<String> searchableAttributes)
  {
    this.searchableAttributes = searchableAttributes;
  }
  
  @Override
  public List<String> getTranslatableAttributeIds()
  {
    if(translatableAttributes == null) {
      translatableAttributes = new ArrayList<>();
    }
    return translatableAttributes;
  }
  
  @Override
  public void setTranslatableAttributeIds(List<String> translatableAttributes)
  {
    this.translatableAttributes = translatableAttributes;
  }
  
  @Override
  public Map<String, IAttribute> getReferencedAttributes()
  {
    return referencedAttributes;
  }
  
  @Override
  @JsonDeserialize(contentAs = AbstractAttribute.class)
  public void setReferencedAttributes(Map<String, IAttribute> referencedAttributes)
  {
    this.referencedAttributes = referencedAttributes;
  }
  
  @Override
  public Map<String, ITag> getReferencedTags()
  {
    return referencedTags;
  }
  
  @Override
  @JsonDeserialize(contentAs = Tag.class)
  public void setReferencedTags(Map<String, ITag> referencedTags)
  {
    this.referencedTags = referencedTags;
  }

  @Override
  @JsonDeserialize(contentAs = NewApplicableFilterModel.class)
  public void setFilterData(List<INewApplicableFilterModel> filterData)
  {
    this.filterData = filterData;
  }
  
  @Override
  public List<INewApplicableFilterModel> getFilterData()
  {
    return this.filterData;
  }

  @Override
	public List<String> getLinkedVariantCodes() 
	{
    if(linkedVariantCodes == null) {
      linkedVariantCodes = new ArrayList<>();
    }
		return linkedVariantCodes;
	}

  @Override
	public void setLinkedVariantCodes(List<String> linkedVariantCodes) 
	{
		this.linkedVariantCodes = linkedVariantCodes;
	}

  @Override
  public IXRayConfigDetailsModel getXRayConfigDetails()
  {
    return xRayConfigDetails;
  }
  
  @JsonDeserialize(as = XRayConfigDetailsModel.class)
  @Override
  public void setXRayConfigDetails(IXRayConfigDetailsModel xRayConfigDetails)
  {
    this.xRayConfigDetails = xRayConfigDetails;
  }
  
  @Override
  public List<String> getMajorTaxonomyIds() {
  if(majorTaxonomyIds == null) {
     majorTaxonomyIds = new ArrayList<String>();
  }
  return majorTaxonomyIds;
  }

  @Override
  public void setMajorTaxonomyIds(List<String> majorTaxonomyIds) {
  this.majorTaxonomyIds = majorTaxonomyIds;
  }
}
