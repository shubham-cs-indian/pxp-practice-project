package com.cs.core.bgprocess.dto;

import java.util.HashSet;
import java.util.Set;

import com.cs.core.bgprocess.idto.IDataRuleDeleteDTO;
import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.dto.SimpleDTO;
import com.cs.core.technical.exception.CSFormatException;

public class DataRuleDeleteDTO extends InitializeBGProcessDTO implements IDataRuleDeleteDTO {
  
  public static final String RULE_CODES = "ruleCodes";
  public static final String KLASS_IDS = "klassIds";
  public static final String TAXONOMY_IDS = "taxonomyIds";
  public static final String CATALOG_IDS = "catalogIds";
  
  private Set<String>        ruleCodes  = new HashSet<>();
  private Set<String>        klassIds  = new HashSet<>();
  private Set<String>        taxonomyIds  = new HashSet<>();
  private Set<String>        catalogIds  = new HashSet<>();
  
  
  public void setRuleCodes(Set<String> ruleCodes)
  {
    this.ruleCodes.clear();
    this.ruleCodes.addAll(ruleCodes);
  }
  
  public Set<String> getRuleCodes()
  {
    return ruleCodes;
  }
  
  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException
  {
    return JSONBuilder.assembleJSONBuffer(super.toJSONBuffer(),
        JSONBuilder.newJSONStringArray(RULE_CODES, ruleCodes),
        JSONBuilder.newJSONStringArray(KLASS_IDS, klassIds),
        JSONBuilder.newJSONStringArray(TAXONOMY_IDS, taxonomyIds),
        JSONBuilder.newJSONStringArray(CATALOG_IDS, catalogIds)
        );
  }
  
  @Override
  public void fromJSON(JSONContentParser json) throws CSFormatException
  {
    super.fromJSON(json);
    ruleCodes.clear();
    json.getJSONArray(RULE_CODES).forEach((ruleCode) -> {
      ruleCodes.add(ruleCode.toString());
    });
    klassIds.clear();
    json.getJSONArray(KLASS_IDS)
        .forEach((iid) -> {
          klassIds.add((String) iid);
        });
    taxonomyIds.clear();
    json.getJSONArray(TAXONOMY_IDS)
        .forEach((iid) -> {
          taxonomyIds.add((String) iid);
        });
    catalogIds.clear();
    json.getJSONArray(CATALOG_IDS)
        .forEach((iid) -> {
          catalogIds.add((String) iid);
        });
  }

  @Override
  public void setKlassIds(Set<String> klassIds)
  {
    this.klassIds.clear();
    this.klassIds.addAll(klassIds);
  }

  @Override
  public Set<String> getKlassIds()
  {
    return klassIds;
  }

  @Override
  public void setTaxonomyIds(Set<String> taxonomyIds)
  {
    this.taxonomyIds.clear();
    this.taxonomyIds.addAll(taxonomyIds);    
  }

  @Override
  public Set<String> getTaxonomyIds()
  {
    return taxonomyIds;
  }

  @Override
  public void setCatalogIds(Set<String> catalogIds)
  {
    this.catalogIds.clear();
    this.catalogIds.addAll(catalogIds); 
  }

  @Override
  public Set<String> getCatalogIds()
  {
    return catalogIds;
  }
}
