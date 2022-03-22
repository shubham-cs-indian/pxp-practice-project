package com.cs.core.config.interactor.model.instancetree;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.map.HashedMap;

import com.cs.core.runtime.interactor.model.instancetree.IReferencedPropertyModel;
import com.cs.core.runtime.interactor.model.instancetree.ReferencedPropertyModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class ConfigDetailsForGetFilterChildrenResponseModel
    implements IConfigDetailsForGetFilterChildrenResponseModel {
  
  private static final long          serialVersionUID = 1L;
  protected List<String>             allowedEntites;
  protected Set<String>              klassIdsWithRP;
  protected Set<String>              taxonomyIdsHavingRP;
  protected List<String>             translatableAttributes;
  protected IReferencedPropertyModel referencedProperty;
  protected List<String>             searchableAttributes;
  protected List<String>             majorTaxonomyIds;
  protected Map<String, String>      ruleViolationsLabels;

  @Override
  public List<String> getAllowedEntities()
  {
    if (allowedEntites == null) {
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
    if (klassIdsWithRP == null) {
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
    if (taxonomyIdsHavingRP == null) {
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
  public IReferencedPropertyModel getReferencedProperty()
  {
    return referencedProperty;
  }
  
  @Override
  @JsonDeserialize(as = ReferencedPropertyModel.class)
  public void setReferencedProperty(IReferencedPropertyModel referencedProperty)
  {
    this.referencedProperty = referencedProperty;
  }
  
  @Override
  public List<String> getSearchableAttributeIds()
  {
    if (searchableAttributes == null) {
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

	@Override
	public Map<String, String> getRuleViolationsLabels() {
	  if (ruleViolationsLabels == null) {
	    ruleViolationsLabels = new HashedMap<String, String>();
    }
		return ruleViolationsLabels;
	}

	@Override
	public void setRuleViolationsLabels(Map<String, String> ruleViolationsLabels) {
		this.ruleViolationsLabels = ruleViolationsLabels;
	}

}
