package com.cs.core.runtime.interactor.model.configdetails.instancetree;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.core.config.interactor.entity.attribute.AbstractAttribute;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.relationship.IRelationship;
import com.cs.core.config.interactor.entity.relationship.Relationship;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.tag.Tag;
import com.cs.core.runtime.interactor.model.instancetree.AppliedSortModel;
import com.cs.core.runtime.interactor.model.instancetree.IAppliedSortModel;
import com.cs.core.runtime.interactor.model.instancetree.INewApplicableFilterModel;
import com.cs.core.runtime.interactor.model.instancetree.NewApplicableFilterModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class ConfigDetailsForRelationshipQuicklistResponseModel
    implements IConfigDetailsForRelationshipQuicklistResponseModel {

  private static final long serialVersionUID = 1L;
  protected List<String>                    allowedEntites;
  protected Set<String>                     klassIdsWithRP;
  protected Set<String>                     taxonomyIdsHavingRP;
  protected List<String>                    searchableAttributes;
  protected List<String>                    translatableAttributes;
  protected Map<String, IAttribute>         referencedAttributes;
  protected Map<String, ITag>               referencedTags;
  protected Boolean                         isTargetTaxonomy = false;
  protected List<String>                    targetIds;
  protected List<IAppliedSortModel>         sortData;
  protected List<INewApplicableFilterModel> filterData;
  protected List<String>                    side2LinkedVariantKrIds;
  protected IRelationship                   relationshipConfig;
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
  public Boolean getIsTargetTaxonomy()
  {
    return isTargetTaxonomy;
  }
  
  @Override
  public void setIsTargetTaxonomy(Boolean isTargetTaxonomy)
  {
    this.isTargetTaxonomy = isTargetTaxonomy;
  }
  
  @Override
  public List<String> getTargetIds()
  {
    if(targetIds == null) {
      targetIds = new ArrayList<>();
    }
    return targetIds;
  }
  
  @Override
  public void setTargetIds(List<String> targetIds)
  {
    this.targetIds = targetIds;
  }
  
  @Override
  public List<IAppliedSortModel> getSortData()
  {
    return this.sortData;
  }
  
  @Override
  @JsonDeserialize(contentAs = AppliedSortModel.class)
  public void setSortData(List<IAppliedSortModel> sortData)
  {
    this.sortData = sortData;
  }
  
  @Override
  public List<INewApplicableFilterModel> getFilterData()
  {
    return this.filterData;
  }
  
  @Override
  @JsonDeserialize(contentAs = NewApplicableFilterModel.class)
  public void setFilterData(List<INewApplicableFilterModel> filterData)
  {
    this.filterData = filterData;
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
    this.side2LinkedVariantKrIds =side2LinkedVariantKrIds;
  }
  
  @Override
  public IRelationship getRelationshipConfig()
  {
    return this.relationshipConfig;
  }
  
  @JsonDeserialize(as = Relationship.class)
  @Override
  public void setRelationshipConfig(IRelationship relationshipConfig)
  {
    this.relationshipConfig = relationshipConfig;
  }

  @Override
  public List<String> getLinkedVariantCodes()
  {
    return linkedVariantCodes;
  }

  @Override
  public void setLinkedVariantCodes(List<String> linkedVariantCodes)
  {
    this.linkedVariantCodes = linkedVariantCodes;
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
