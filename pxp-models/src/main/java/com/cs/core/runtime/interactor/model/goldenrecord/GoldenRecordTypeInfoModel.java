package com.cs.core.runtime.interactor.model.goldenrecord;

import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceTypeModel;
import com.cs.core.runtime.interactor.model.klassinstance.KlassInstanceTypeModel;

import java.util.ArrayList;
import java.util.List;

public class GoldenRecordTypeInfoModel extends KlassInstanceTypeModel
    implements IGoldenRecordTypeInfoModel {
  
  private static final long serialVersionUID = 1L;
  protected List<String>    addedTypes;
  protected List<String>    addedTaxonomies;
  protected String          ruleId;
  
  public GoldenRecordTypeInfoModel()
  {
  }
  
  public GoldenRecordTypeInfoModel(IKlassInstanceTypeModel typeModel)
  {
    types = typeModel.getTypes();
    taxonomyIds = typeModel.getTaxonomyIds();
    selectedTaxonomyIds = typeModel.getSelectedTaxonomyIds();
    tagIdTagValueIdsMap = typeModel.getTagIdTagValueIdsMap();
    endpointId = typeModel.getEndpointId();
    organizationId = typeModel.getOrganizationId();
    physicalCatalogId = typeModel.getPhysicalCatalogId();
    parentKlassIds = typeModel.getParentKlassIds();
    parentTaxonomyIds = typeModel.getParentTaxonomyIds();
    name = typeModel.getName();
  }
  
  @Override
  public List<String> getAddedTypes()
  {
    if (addedTypes == null) {
      addedTypes = new ArrayList<>();
    }
    return addedTypes;
  }
  
  @Override
  public void setAddedTypes(List<String> addedTypes)
  {
    this.addedTypes = addedTypes;
  }
  
  @Override
  public List<String> getAddedTaxonomies()
  {
    if (addedTaxonomies == null) {
      addedTaxonomies = new ArrayList<>();
    }
    return addedTaxonomies;
  }
  
  @Override
  public void setAddedTaxonomies(List<String> addedTaxonomies)
  {
    this.addedTaxonomies = addedTaxonomies;
  }
  
  @Override
  public String getRuleId()
  {
    return ruleId;
  }
  
  @Override
  public void setRuleId(String ruleId)
  {
    this.ruleId = ruleId;
  }
}
