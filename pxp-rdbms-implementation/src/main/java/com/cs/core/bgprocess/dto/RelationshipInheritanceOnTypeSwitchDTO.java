package com.cs.core.bgprocess.dto;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;

import com.cs.core.bgprocess.idto.IRelationshipInheritanceOnTypeSwitchDTO;
import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.dto.SimpleDTO;
import com.cs.core.technical.exception.CSFormatException;

public class RelationshipInheritanceOnTypeSwitchDTO extends InitializeBGProcessDTO implements IRelationshipInheritanceOnTypeSwitchDTO {

 private static final long  serialVersionUID       = 1L;
  
  public static final String CONTENT_ID          = "contentId";
  public static final String EXISTING_TYPES      = "existingTypes";
  public static final String EXISTING_TAXONOMIES = "existingTaxonomies";
  public static final String ADDED_TYPES         = "addedTypes";
  public static final String ADDED_TAXONOMIES    = "addedTaxonomies";
  public static final String REMOVED_TAXONOMIES  = "removedTaxonomies";
  public static final String REMOVED_TYPES       = "removedTypes";
  
  private Long               contentId;
  private List<String>         existingTypes       = new ArrayList<>();
  private List<String>         existingTaxonomies  = new ArrayList<>();
  private List<String>         addedTypes          = new ArrayList<>();
  private List<String>         addedTaxonomies     = new ArrayList<>();
  private List<String>         removedTaxonomies   = new ArrayList<>();
  private List<String>         removedTypes        = new ArrayList<>();
  
  
  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException
  {
    return JSONBuilder.assembleJSONBuffer(super.toJSONBuffer(),JSONBuilder.newJSONField(CONTENT_ID, contentId),
        JSONBuilder.newJSONStringArray(EXISTING_TYPES, existingTypes),
        JSONBuilder.newJSONStringArray(EXISTING_TAXONOMIES, existingTaxonomies),
        JSONBuilder.newJSONStringArray(ADDED_TYPES, addedTypes),
        JSONBuilder.newJSONStringArray(ADDED_TAXONOMIES, addedTaxonomies),
        JSONBuilder.newJSONStringArray(REMOVED_TAXONOMIES, removedTaxonomies),
        JSONBuilder.newJSONStringArray(REMOVED_TYPES, removedTypes));
  }

  @Override
  public Long getContentId()
  {
    return contentId;
  }

  @Override
  public void setContentId(Long contentId)
  {
    this.contentId = contentId;
  }

  @Override
  public List<String> getExistingTypes()
  {
    return existingTypes;
  }

  @Override
  public void setExistingTypes(List<String> existingTypes)
  {
    this.existingTypes = existingTypes;
  }

  @Override
  public List<String> getExistingTaxonomies()
  {
    return existingTaxonomies;
  }

  @Override
  public void setExistingTaxonomies(List<String> existingTaxonomies)
  {
    this.existingTaxonomies = existingTaxonomies;
  }

  @Override
  public List<String> getAddedTypes()
  {
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
    return addedTaxonomies;
  }

  @Override
  public void setAddedTaxonomies(List<String> addedTaxonomies)
  {
    this.addedTaxonomies = addedTaxonomies;
  }

  @Override
  public List<String> getRemovedTypes()
  {
    return removedTypes;
  }

  @Override
  public void setRemovedTypes(List<String> removedTypes)
  {
    this.removedTypes = removedTypes;
  }

  @Override
  public List<String> getRemovedTaxonomies()
  {
    return removedTaxonomies;
  }

  @Override
  public void setRemovedTaxonomies(List<String> removedTaxonomies)
  {
    this.removedTaxonomies = removedTaxonomies;
  }

  @Override
  public void fromJSON(JSONContentParser json) throws CSFormatException
  {
    super.fromJSON(json);
    existingTypes.clear();
    JSONArray existingTypesArray = json.getJSONArray(EXISTING_TYPES);
    for (Object jsonV : existingTypesArray) {
      existingTypes.add( jsonV.toString());
    }
    existingTaxonomies.clear();
    JSONArray existingTaxonomiesArray = json.getJSONArray(EXISTING_TAXONOMIES);
    for (Object jsonV : existingTaxonomiesArray) {
      existingTaxonomies.add(jsonV.toString());
    }
    addedTypes.clear();
    JSONArray addedTypesArray = json.getJSONArray(ADDED_TYPES);
    for (Object jsonV : addedTypesArray) {
      addedTypes.add(jsonV.toString());
    }
    addedTaxonomies.clear();
    JSONArray addedTaxonomiesArray = json.getJSONArray(ADDED_TAXONOMIES);
    for (Object jsonV : addedTaxonomiesArray) {
      addedTaxonomies.add(jsonV.toString());
    }
    removedTaxonomies.clear();
    JSONArray removedTaxonomiesArray = json.getJSONArray(REMOVED_TAXONOMIES);
    for (Object jsonV : removedTaxonomiesArray) {
      removedTaxonomies.add(jsonV.toString());
    }
    removedTypes.clear();
    JSONArray removedTypesArray = json.getJSONArray(REMOVED_TYPES);
    for (Object jsonV : removedTypesArray) {
      removedTypes.add(jsonV.toString());
    }
    contentId = json.getLong(CONTENT_ID);
  }
  
}
