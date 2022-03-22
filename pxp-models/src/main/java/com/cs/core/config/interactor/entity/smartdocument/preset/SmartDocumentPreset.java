package com.cs.core.config.interactor.entity.smartdocument.preset;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class SmartDocumentPreset implements ISmartDocumentPreset {
  
  private static final long                                  serialVersionUID = 1L;
  protected String                                           label;
  protected String                                           icon;
  protected String                                           iconKey;
  protected String                                           type;
  protected String                                           code;
  protected String                                           id;
  protected Long                                             versionId;
  protected Long                                             versionTimestamp;
  protected String                                           lastModifiedBy;
  protected String                                           smartDocumentTemplateId;
  protected ISmartDocumentPresetPdfConfiguration             smartDocumentPresetPdfConfiguration;
  protected Boolean                                          splitDocument    = false;
  protected String                                           languageCode     = "";
  protected Boolean                                          showPreview      = false;
  protected Boolean                                          saveDocument     = false;
  protected List<String>                                     attributeIds     = new ArrayList<>();
  protected List<String>                                     tagIds           = new ArrayList<>();
  protected List<String>                                     klassIds         = new ArrayList<>();
  protected List<String>                                     taxonomyIds      = new ArrayList<>();
  protected List<ISmartDocumentPresetRuleIntermediateEntity> attributes       = new ArrayList<>();
  protected List<ISmartDocumentPresetRuleTags>               tags             = new ArrayList<>();
  
  @Override
  public String getLabel()
  {
    return label;
  }
  
  @Override
  public void setLabel(String label)
  {
    this.label = label;
  }
  
  @Override
  public String getIcon()
  {
    return icon;
  }
  
  @Override
  public void setIcon(String icon)
  {
    this.icon = icon;
  }
  
  @Override
  public String getIconKey()
  {
    return icon;
  }
  
  @Override
  public void setIconKey(String iconKey)
  {
    this.iconKey = iconKey;
  }
  
  @Override
  public String getType()
  {
    return type;
  }
  
  @Override
  public void setType(String type)
  {
    this.type = type;
  }
  
  @Override
  public String getCode()
  {
    return code;
  }
  
  @Override
  public void setCode(String code)
  {
    this.code = code;
  }
  
  @Override
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public Long getVersionId()
  {
    return versionId;
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    this.versionId = versionId;
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    return versionTimestamp;
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    this.versionTimestamp = versionTimestamp;
  }
  
  @Override
  public String getLastModifiedBy()
  {
    return lastModifiedBy;
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    this.lastModifiedBy = lastModifiedBy;
  }
  
  @Override
  public String getSmartDocumentTemplateId()
  {
    return smartDocumentTemplateId;
  }
  
  @Override
  public void setSmartDocumentTemplateId(String smartDocumentTemplateId)
  {
    this.smartDocumentTemplateId = smartDocumentTemplateId;
  }
  
  @Override
  public ISmartDocumentPresetPdfConfiguration getSmartDocumentPresetPdfConfiguration()
  {
    return smartDocumentPresetPdfConfiguration;
  }
  
  @Override
  @JsonDeserialize(as = SmartDocumentPresetPdfConfiguration.class)
  public void setSmartDocumentPresetPdfConfiguration(
      ISmartDocumentPresetPdfConfiguration smartDocumentPresetPdfConfiguration)
  {
    this.smartDocumentPresetPdfConfiguration = smartDocumentPresetPdfConfiguration;
  }
  
  @Override
  public boolean getSplitDocument()
  {
    return splitDocument;
  }
  
  @Override
  public void setSplitDocument(boolean splitDocument)
  {
    this.splitDocument = splitDocument;
  }
  
  @Override
  public String getLanguageCode()
  {
    return languageCode;
  }
  
  @Override
  public void setLanguageCode(String languageCode)
  {
    this.languageCode = languageCode;
  }
  
  @Override
  public boolean getShowPreview()
  {
    return showPreview;
  }
  
  @Override
  public void setShowPreview(boolean showPreview)
  {
    this.showPreview = showPreview;
  }
  
  @Override
  public boolean getSaveDocument()
  {
    return saveDocument;
  }
  
  @Override
  public void setSaveDocument(boolean saveDocument)
  {
    this.saveDocument = saveDocument;
  }
  
  @Override
  public List<String> getAttributeIds()
  {
    return attributeIds;
  }
  
  @Override
  public void setAttributeIds(List<String> attributeIds)
  {
    this.attributeIds = attributeIds;
  }
  
  @Override
  public List<String> getTagIds()
  {
    return tagIds;
  }
  
  @Override
  public void setTagIds(List<String> tagIds)
  {
    this.tagIds = tagIds;
  }
  
  @Override
  public List<String> getKlassIds()
  {
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
    return taxonomyIds;
  }
  
  @Override
  public void setTaxonomyIds(List<String> taxonomyIds)
  {
    this.taxonomyIds = taxonomyIds;
  }
  
  @Override
  public List<ISmartDocumentPresetRuleIntermediateEntity> getAttributes()
  {
    return attributes;
  }
  
  @Override
  @JsonDeserialize(contentAs = SmartDocumentPresetRuleIntermediateEntity.class)
  public void setAttributes(List<ISmartDocumentPresetRuleIntermediateEntity> attributes)
  {
    this.attributes = attributes;
  }
  
  @Override
  public List<ISmartDocumentPresetRuleTags> getTags()
  {
    return tags;
  }
  
  @Override
  @JsonDeserialize(contentAs = SmartDocumentPresetRuleTags.class)
  public void setTags(List<ISmartDocumentPresetRuleTags> tags)
  {
    this.tags = tags;
  }
}
