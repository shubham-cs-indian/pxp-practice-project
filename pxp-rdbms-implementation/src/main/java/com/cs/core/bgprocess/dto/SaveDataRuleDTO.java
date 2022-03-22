package com.cs.core.bgprocess.dto;

import java.util.HashSet;
import java.util.Set;

import com.cs.core.bgprocess.idto.ISaveDataRuleDTO;
import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.dto.SimpleDTO;
import com.cs.core.technical.exception.CSFormatException;

public class SaveDataRuleDTO extends InitializeBGProcessDTO implements ISaveDataRuleDTO {
  
  public static final String RULE_CODE                  = "ruleCode";
  public static final String RULE_EXPRESSION_ID         = "ruleExpressionId";
  public static final String CHANGED_CLASSIFIER_CODES   = "changedClassifierCodes";
  public static final String CHANGED_CATALOG_IDS        = "changedCatalogIds";
  public static final String CHANGED_ORGANIZATION_IDS = "changedOrganizationIds";
  
  private String             ruleCode;
  private Long               ruleExpressionId;
  private Set<String>        changedClassifierCodes     = new HashSet<>();
  private Set<String>        changedCatalogIds          = new HashSet<>();
  private Set<String>        changedOrganizationIds     = new HashSet<>();

  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException
  {
    return JSONBuilder.assembleJSONBuffer(super.toJSONBuffer(),JSONBuilder.newJSONField(RULE_CODE, ruleCode),
        JSONBuilder.newJSONField(RULE_EXPRESSION_ID, ruleExpressionId),
        JSONBuilder.newJSONStringArray(CHANGED_CLASSIFIER_CODES, changedClassifierCodes),
        JSONBuilder.newJSONStringArray(CHANGED_CATALOG_IDS, changedCatalogIds),
        JSONBuilder.newJSONStringArray(CHANGED_ORGANIZATION_IDS, changedOrganizationIds)
        );
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public void fromJSON(JSONContentParser json) throws CSFormatException
  {
    super.fromJSON(json);
    ruleCode = json.getString(RULE_CODE);
    ruleExpressionId = json.getLong(RULE_EXPRESSION_ID);
    changedClassifierCodes.clear();
    json.getJSONArray(CHANGED_CLASSIFIER_CODES).forEach((changedClassifierCode) -> {
      changedClassifierCodes.add(changedClassifierCode.toString());
    });
    changedCatalogIds.clear();
    json.getJSONArray(CHANGED_CATALOG_IDS)
        .forEach((changedCatalogId) -> {
          changedCatalogIds.add(changedCatalogId.toString());
        });
    changedOrganizationIds.clear();
    json.getJSONArray(CHANGED_ORGANIZATION_IDS)
        .forEach((changedOrganizationCode) -> {
          changedOrganizationIds.add(changedOrganizationCode.toString());
        });
  }

  @Override 
  public void setRuleCode(String ruleCode)
  {
    this.ruleCode = ruleCode;
  }
  
  @Override
  public String getRuleCode()
  {
    return ruleCode;
  }
  
  
  @Override
  public void setChangedClassifierCodes(Set<String> classifierCodes)
  {
    this.changedClassifierCodes.clear();
    this.changedClassifierCodes.addAll(classifierCodes);
  }

  @Override
  public Set<String> getChangedClassifierCodes()
  {
    return changedClassifierCodes;
  }

  @Override
  public void setChangedCatalogIds(Set<String> catalogIds)
  {
    this.changedCatalogIds.clear();
    this.changedCatalogIds.addAll(catalogIds);
  }

  @Override
  public Set<String> getChangedCatalogIds()
  {
    return changedCatalogIds;
  }

  @Override
  public void setChangedOrganizationIds(Set<String> organizationIds)
  {
    this.changedOrganizationIds.clear();
    this.changedOrganizationIds.addAll(organizationIds);    
  }

  @Override
  public Set<String> getChangedOrganizationIds()
  {
    return changedOrganizationIds;
  }

  @Override
  public void setRuleExpressionId(Long ruleExpressionId)
  {
   this.ruleExpressionId =  ruleExpressionId;    
  }

  @Override
  public Long getRuleExpressionId()
  {
    return ruleExpressionId;
  }
}
