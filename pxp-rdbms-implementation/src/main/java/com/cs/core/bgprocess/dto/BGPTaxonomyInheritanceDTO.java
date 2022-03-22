package com.cs.core.bgprocess.dto;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.bgprocess.idto.IBGPTaxonomyInheritanceDTO;
import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.technical.exception.CSFormatException;

public class BGPTaxonomyInheritanceDTO extends InitializeBGProcessDTO implements IBGPTaxonomyInheritanceDTO {
  
  private static final long serialVersionUID   = 1L;
  private List<Long>        addedElementIIDs   = new ArrayList<>();
  private List<Long>        deletedElementIIDs = new ArrayList<>();
  private Long              sourceEntityIID;
  private Long              propertyIID;
  private String            taxonomyInheritanceSetting;
  
  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException
  {
    return JSONBuilder.assembleJSONBuffer(
        super.toJSONBuffer(),
        JSONBuilder.newJSONField(SOURCE_ENTITY_IID, sourceEntityIID),
        JSONBuilder.newJSONField(PROPERTY_IID, propertyIID),
        JSONBuilder.newJSONField(TAXONOMY_INHERITANCE_SETTING, taxonomyInheritanceSetting),
        JSONBuilder.newJSONLongArray(ADDED_ELEMENT_IIDS, addedElementIIDs),
        !deletedElementIIDs.isEmpty() ? JSONBuilder.newJSONLongArray(IBGPTaxonomyInheritanceDTO.DELETED_ELEMENT_IIDS, deletedElementIIDs) : JSONBuilder.VOID_FIELD);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public void fromJSON(JSONContentParser json) throws CSFormatException
  {
    super.fromJSON(json);
    sourceEntityIID = json.getLong(SOURCE_ENTITY_IID);
    propertyIID = json.getLong(PROPERTY_IID);
    taxonomyInheritanceSetting = json.getString(TAXONOMY_INHERITANCE_SETTING);
    addedElementIIDs.clear();
    json.getJSONArray(IBGPTaxonomyInheritanceDTO.ADDED_ELEMENT_IIDS).forEach((iid) -> {
      addedElementIIDs.add((Long) iid);
    });
    deletedElementIIDs.clear();
    json.getJSONArray(IBGPTaxonomyInheritanceDTO.DELETED_ELEMENT_IIDS).forEach((iid) -> {
      deletedElementIIDs.add((Long) iid);
    });
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
  public Long getPropertyIID()
  {
    return propertyIID;
  }
  
  @Override
  public void setPropertyIID(Long propertyIID)
  {
    this.propertyIID = propertyIID;
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
  public List<Long> getAddedElementIIDs()
  {
    return addedElementIIDs;
  }

  @Override
  public void setAddedElementIIDs(List<Long> addedElementIIDs)
  {
    this.addedElementIIDs = addedElementIIDs;
  }
  
  @Override
  public List<Long> getDeletedElementIIDs()
  {
    return deletedElementIIDs;
  }
  
  @Override
  public void setDeletedElementIIDs(List<Long> deletedElementIIDs)
  {
    this.deletedElementIIDs = deletedElementIIDs;
  }
}
