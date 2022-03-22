package com.cs.core.runtime.interactor.model.goldenrecord;

import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstancesForComparisonModel;
import com.cs.core.runtime.interactor.model.klassinstance.KlassInstancesForComparisonModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GoldenRecordRuleKlassInstancesComparisonModel
    implements IGoldenRecordRuleKlassInstancesComparisonModel {
  
  private static final long                                serialVersionUID = 1L;
  protected Map<String, IKlassInstancesForComparisonModel> klassInstancesDetails;
  protected String                                         ruleId;
  protected List<String>                                   klassInstanceIds;
  protected List<String>                                   klassIds;
  protected List<String>                                   taxonomyIds;
  protected Map<String, List<String>>                      supplierContentsMap;
  protected String                                         goldenRecordId;
  
  @Override
  public Map<String, IKlassInstancesForComparisonModel> getKlassInstancesDetails()
  {
    if (klassInstancesDetails == null) {
      klassInstancesDetails = new HashMap<>();
    }
    return klassInstancesDetails;
  }
  
  @Override
  @JsonDeserialize(contentAs = KlassInstancesForComparisonModel.class)
  public void setKlassInstancesDetails(
      Map<String, IKlassInstancesForComparisonModel> klassInstancesDetails)
  {
    this.klassInstancesDetails = klassInstancesDetails;
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
  
  @Override
  public List<String> getKlassInstanceIds()
  {
    if (klassInstanceIds == null) {
      klassInstanceIds = new ArrayList<>();
    }
    return klassInstanceIds;
  }
  
  @Override
  public void setKlassInstanceIds(List<String> klassInstanceIds)
  {
    this.klassInstanceIds = klassInstanceIds;
  }
  
  @Override
  public List<String> getKlassIds()
  {
    if (klassIds == null) {
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
    if (taxonomyIds == null) {
      taxonomyIds = new ArrayList<>();
    }
    return taxonomyIds;
  }
  
  @Override
  public void setTaxonomyIdsIds(List<String> taxonomyIds)
  {
    this.taxonomyIds = taxonomyIds;
  }
  
  @Override
  public Map<String, List<String>> getSupplierContentsMap()
  {
    if (supplierContentsMap == null) {
      supplierContentsMap = new HashMap<>();
    }
    return supplierContentsMap;
  }
  
  @Override
  public void setSupplierContentsMap(Map<String, List<String>> supplierContentsMap)
  {
    this.supplierContentsMap = supplierContentsMap;
  }
  
  @Override
  public String getGoldenRecordId()
  {
    return goldenRecordId;
  }
  
  @Override
  public void setGoldenRecordId(String goldenRecordId)
  {
    this.goldenRecordId = goldenRecordId;
  }
}
