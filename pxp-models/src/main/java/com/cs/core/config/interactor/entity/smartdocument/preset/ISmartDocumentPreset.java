package com.cs.core.config.interactor.entity.smartdocument.preset;

import com.cs.core.config.interactor.entity.configuration.base.IConfigMasterEntity;

import java.util.List;

public interface ISmartDocumentPreset extends IConfigMasterEntity {
  
  public static final String SMART_DOCUMENT_TEMPLATE_ID              = "smartDocumentTemplateId";
  public static final String SMART_DOCUMENT_PRESET_PDF_CONFIGURATION = "smartDocumentPresetPdfConfiguration";
  public static final String SPLIT_DOCUMENT                          = "splitDocument";
  public static final String LANGUAGE_CODE                           = "languageCode";
  public static final String SHOW_PREVIEW                            = "showPreview";
  public static final String SAVE_DOCUMENT                           = "saveDocument";
  public static final String ATTRIBUTE_IDS                           = "attributeIds";
  public static final String TAG_IDS                                 = "tagIds";
  public static final String KLASS_IDS                               = "klassIds";
  public static final String TAXONOMY_IDS                            = "taxonomyIds";
  public static final String ATTRIBUTES                              = "attributes";
  public static final String TAGS                                    = "tags";
  
  public String getSmartDocumentTemplateId();
  
  public void setSmartDocumentTemplateId(String smartDocumentTemplateId);
  
  public ISmartDocumentPresetPdfConfiguration getSmartDocumentPresetPdfConfiguration();
  
  public void setSmartDocumentPresetPdfConfiguration(
      ISmartDocumentPresetPdfConfiguration smartDocumentPresetPdfConfiguration);
  
  public boolean getSplitDocument();
  
  public void setSplitDocument(boolean splitDocument);
  
  public String getLanguageCode();
  
  public void setLanguageCode(String languageCode);
  
  public boolean getShowPreview();
  
  public void setShowPreview(boolean showPreview);
  
  public boolean getSaveDocument();
  
  public void setSaveDocument(boolean saveDocument);
  
  public List<String> getAttributeIds();
  
  public void setAttributeIds(List<String> attributeIds);
  
  public List<String> getTagIds();
  
  public void setTagIds(List<String> tagIds);
  
  public List<String> getKlassIds();
  
  public void setKlassIds(List<String> klassIds);
  
  public List<String> getTaxonomyIds();
  
  public void setTaxonomyIds(List<String> taxonomyIds);
  
  public List<ISmartDocumentPresetRuleIntermediateEntity> getAttributes();
  
  public void setAttributes(List<ISmartDocumentPresetRuleIntermediateEntity> attributes);
  
  public List<ISmartDocumentPresetRuleTags> getTags();
  
  public void setTags(List<ISmartDocumentPresetRuleTags> tags);
}
