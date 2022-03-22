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
import com.cs.core.runtime.interactor.model.instancetree.AppliedSortModel;
import com.cs.core.runtime.interactor.model.instancetree.IAppliedSortModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetNewFilterAndSortDataResponseModel;
import com.cs.core.runtime.interactor.model.instancetree.INewApplicableFilterModel;
import com.cs.core.runtime.interactor.model.instancetree.IReferencedKlassOrTaxonomyModel;
import com.cs.core.runtime.interactor.model.instancetree.NewApplicableFilterModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetNewFilterAndSortDataResponseModel implements IGetNewFilterAndSortDataResponseModel {
  
  private static final long                 serialVersionUID = 1L;
  
  protected List<String>                                 allowedEntites;
  protected Set<String>                                  klassIdsWithRP;
  protected Set<String>                                  taxonomyIdsHavingRP;
  protected List<String>                                 searchableAttributes;
  protected List<String>                                 translatableAttributes;
  protected Map<String, IAttribute>                      referencedAttributes;
  protected Map<String, ITag>                            referencedTags;
  protected Integer                                      count;
  protected List<IAppliedSortModel>                      sortData;
  protected List<INewApplicableFilterModel>              filterData;
  protected Map<String, IReferencedKlassOrTaxonomyModel> referencedTaxonomies;
  protected Map<String, IReferencedKlassOrTaxonomyModel> referencedKlasses;
  protected List<String>                                 majorTaxonomyIds;
  
  @Override
  @JsonIgnore
  public List<String> getAllowedEntities()
  {
    if (allowedEntites == null) {
      allowedEntites = new ArrayList<>();
    }
    return allowedEntites;
  }
  
  @Override
  @JsonProperty
  public void setAllowedEntities(List<String> allowedEntites)
  {
    this.allowedEntites = allowedEntites;
  }
  
  @Override
  @JsonIgnore
  public Set<String> getKlassIdsHavingRP()
  {
    if (klassIdsWithRP == null) {
      klassIdsWithRP = new HashSet<>();
    }
    return klassIdsWithRP;
  }
  
  @Override
  @JsonProperty
  public void setKlassIdsHavingRP(Set<String> klassIdsWithRP)
  {
    this.klassIdsWithRP = klassIdsWithRP;
  }
  
  @Override
  @JsonIgnore
  public Set<String> getTaxonomyIdsHavingRP()
  {
    if (taxonomyIdsHavingRP == null) {
      taxonomyIdsHavingRP = new HashSet<>();
    }
    return taxonomyIdsHavingRP;
  }
  
  @Override
  @JsonProperty
  public void setTaxonomyIdsHavingRP(Set<String> taxonomyIdsHavingRP)
  {
    this.taxonomyIdsHavingRP = taxonomyIdsHavingRP;
  }
  
  @Override
  @JsonIgnore
  public List<String> getSearchableAttributeIds()
  {
    if (searchableAttributes == null) {
      searchableAttributes = new ArrayList<>();
    }
    return searchableAttributes;
  }
  
  @Override
  @JsonProperty
  public void setSearchableAttributeIds(List<String> searchableAttributes)
  {
    this.searchableAttributes = searchableAttributes;
  }
  
  @Override
  public List<String> getTranslatableAttributeIds()
  {
    if (translatableAttributes == null) {
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
  public Integer getCount()
  {
    return count;
  }
  
  @Override
  public void setCount(Integer count)
  {
    this.count = count;
  }
  
  @Override
  @JsonDeserialize(contentAs = AppliedSortModel.class)
  public void setSortData(List<IAppliedSortModel> sortData)
  {
    this.sortData = sortData;
  }
  
  @Override
  public List<IAppliedSortModel> getSortData()
  {
    return this.sortData;
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
  @JsonDeserialize(contentAs = ReferencedKlassOrTaxonomyModel.class)
  public void setReferencedTaxonomies(Map<String, IReferencedKlassOrTaxonomyModel> referencedTaxonomies)
  {
    this.referencedTaxonomies = referencedTaxonomies;
  }

  @Override
  public Map<String, IReferencedKlassOrTaxonomyModel> getReferencedTaxonomies()
  {
    return referencedTaxonomies;
  }

  @Override
  @JsonDeserialize(contentAs = ReferencedKlassOrTaxonomyModel.class)
  public void setReferencedKlasses(Map<String, IReferencedKlassOrTaxonomyModel> referencedKlasses)
  {
    this.referencedKlasses = referencedKlasses;
  }
  
  @Override
  public Map<String, IReferencedKlassOrTaxonomyModel> getReferencedKlasses()
  {
    return this.referencedKlasses;
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
