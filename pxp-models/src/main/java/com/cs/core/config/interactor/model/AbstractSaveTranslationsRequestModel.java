package com.cs.core.config.interactor.model;

import com.cs.core.config.interactor.model.translations.ISaveTranslationsRequestModel;
import com.cs.core.config.interactor.model.translations.SaveBasicTranslationsRequestModel;
import com.cs.core.config.interactor.model.translations.SaveRelationshipTranslationsRequestModel;
import com.cs.core.config.interactor.model.translations.SaveStandardTranslationsRequestModel;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.ArrayList;
import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY,
    property = "entityType", visible = true)
@JsonSubTypes({ @Type(name = "attribute", value = SaveStandardTranslationsRequestModel.class),
    @Type(name = "role", value = SaveStandardTranslationsRequestModel.class),
    @Type(name = "tag", value = SaveStandardTranslationsRequestModel.class),
    @Type(name = "attributionTaxonomy", value = SaveStandardTranslationsRequestModel.class),
    @Type(name = "event", value = SaveStandardTranslationsRequestModel.class),
    @Type(name = "context", value = SaveStandardTranslationsRequestModel.class),
    @Type(name = "relationship", value = SaveRelationshipTranslationsRequestModel.class),
    @Type(name = "reference", value = SaveRelationshipTranslationsRequestModel.class),
    @Type(name = "organization", value = SaveBasicTranslationsRequestModel.class),
    @Type(name = "task", value = SaveBasicTranslationsRequestModel.class),
    @Type(name = "article", value = SaveBasicTranslationsRequestModel.class),
    @Type(name = "asset", value = SaveBasicTranslationsRequestModel.class),
    @Type(name = "target", value = SaveBasicTranslationsRequestModel.class),
    @Type(name = "promotion", value = SaveBasicTranslationsRequestModel.class),
    @Type(name = "supplier", value = SaveBasicTranslationsRequestModel.class),
    @Type(name = "textAsset", value = SaveBasicTranslationsRequestModel.class),
    @Type(name = "profile", value = SaveBasicTranslationsRequestModel.class),
    @Type(name = "goldenRecords", value = SaveBasicTranslationsRequestModel.class),
    @Type(name = "hierarchyTaxonomy", value = SaveBasicTranslationsRequestModel.class),
    @Type(name = "keyperformanceindex", value = SaveBasicTranslationsRequestModel.class),
    @Type(name = "mapping", value = SaveBasicTranslationsRequestModel.class),
    @Type(name = "process", value = SaveBasicTranslationsRequestModel.class),
    @Type(name = "propertyCollection", value = SaveBasicTranslationsRequestModel.class),
    @Type(name = "ruleList", value = SaveBasicTranslationsRequestModel.class),
    @Type(name = "rule", value = SaveBasicTranslationsRequestModel.class),
    @Type(name = "smartDocumentPreset", value = SaveBasicTranslationsRequestModel.class),
    @Type(name = "smartDocumentTemplate", value = SaveBasicTranslationsRequestModel.class),
    @Type(name = "system", value = SaveBasicTranslationsRequestModel.class),
    @Type(name = "tab", value = SaveBasicTranslationsRequestModel.class),
    @Type(name = "dashboardTab", value = SaveBasicTranslationsRequestModel.class),
    @Type(name = "task", value = SaveBasicTranslationsRequestModel.class),
    @Type(name = "template", value = SaveBasicTranslationsRequestModel.class),
    @Type(name = "language", value = SaveBasicTranslationsRequestModel.class),
    @Type(name = "staticTranslation", value = SaveBasicTranslationsRequestModel.class) })
public abstract class AbstractSaveTranslationsRequestModel
    implements ISaveTranslationsRequestModel {
  
  private static final long serialVersionUID = 1L;
  protected List<String>    languages;
  protected String          entityType;
  
  @Override
  public List<String> getLanguages()
  {
    return languages;
  }
  
  @Override
  public void setLanguages(List<String> languages)
  {
    if (languages == null) {
      languages = new ArrayList<>();
    }
    this.languages = languages;
  }
  
  @Override
  public String getEntityType()
  {
    return entityType;
  }
  
  @Override
  public void setEntityType(String entityType)
  {
    this.entityType = entityType;
  }
}
