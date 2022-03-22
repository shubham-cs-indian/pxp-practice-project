package com.cs.core.bgprocess.dto;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.bgprocess.idto.ITaxonomyInheritanceOnTypeSwitchDTO;
import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.technical.exception.CSFormatException;

public class TaxonomyInheritanceOnTypeSwitchDTO extends InitializeBGProcessDTO implements ITaxonomyInheritanceOnTypeSwitchDTO {
  
  private static final long serialVersionUID = 1L;
  private Long                sourceEntityIID;
  private String              relationshipId;
  private String              taxonomyInheritanceSetting;
  private List<String>        addedTaxonomyIds = new ArrayList<>();
  private List<String>        removedTaxonomyIds = new ArrayList<>();

  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException
  {
    return JSONBuilder.assembleJSONBuffer(super.toJSONBuffer(),
        JSONBuilder.newJSONField(SOURCE_ENTITY_IID, sourceEntityIID),
        JSONBuilder.newJSONField(RELATIONSHIP_ID, relationshipId),
        JSONBuilder.newJSONField(TAXONOMY_INHERITANCE_SETTING, taxonomyInheritanceSetting),
        JSONBuilder.newJSONStringArray(ADDED_TAXONOMY_IDS, addedTaxonomyIds),
        JSONBuilder.newJSONStringArray(REMOVED_TAXONOMY_IDS, removedTaxonomyIds));
  }

  @Override
  public void fromJSON(JSONContentParser json) throws CSFormatException
  {
    super.fromJSON(json);
    sourceEntityIID = json.getLong(SOURCE_ENTITY_IID);
    relationshipId = json.getString(RELATIONSHIP_ID);
    taxonomyInheritanceSetting = json.getString(TAXONOMY_INHERITANCE_SETTING);
    addedTaxonomyIds.clear();
    for (Object jsonV : json.getJSONArray(ADDED_TAXONOMY_IDS)) {
      addedTaxonomyIds.add(jsonV.toString());
    }
    removedTaxonomyIds.clear();
    for (Object jsonV : json.getJSONArray(REMOVED_TAXONOMY_IDS)) {
      removedTaxonomyIds.add(jsonV.toString());
    }
  }

  @Override
  public Long getSourceEntityIID()
  {
    return sourceEntityIID;
  }
  
  @Override
  public void setSourceEntityIID(Long sourceEntityIID)
  {
    this.sourceEntityIID = sourceEntityIID;
  }

  @Override
  public String getRelationshipId()
  {
    return relationshipId;
  }

  @Override
  public void setRelationshipId(String relationshipId)
  {
    this.relationshipId = relationshipId;
  }

  @Override
  public String getTaxonomyInheritanceSetting()
  {
    return taxonomyInheritanceSetting;
  }

  @Override
  public void setTaxonomyInheritanceSetting(String taxonomyInheritanceSetting)
  {
    this.taxonomyInheritanceSetting = taxonomyInheritanceSetting;
  }

  @Override
  public List<String> getAddedTaxonomyIds()
  {
    return addedTaxonomyIds;
  }

  @Override
  public void setAddedTaxonomyIds(List<String> addedTaxonomyIds)
  {
    this.addedTaxonomyIds = addedTaxonomyIds;
  }

  @Override
  public List<String> getRemovedTaxonomyIds()
  {
    return removedTaxonomyIds;
  }

  @Override
  public void setRemovedTaxonomyIds(List<String> removedTaxonomyIds)
  {
    this.removedTaxonomyIds = removedTaxonomyIds;
  }
  
  
}
