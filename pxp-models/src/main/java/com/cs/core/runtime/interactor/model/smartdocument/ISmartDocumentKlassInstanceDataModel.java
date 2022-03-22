package com.cs.core.runtime.interactor.model.smartdocument;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Map;

public interface ISmartDocumentKlassInstanceDataModel extends IModel {
  
  public static final String ID                       = "id";
  public static final String LABEL                    = "label";
  public static final String IMAGE_URL                = "imageUrl";
  public static final String PREVIEW_IMAGE_URL        = "previewImageUrl";
  public static final String ATTRIBUTES               = "attributes";
  public static final String ATTRIBUTE_VARIANTS       = "attributeVariants";
  public static final String TAGS                     = "tags";
  public static final String UNITS                    = "units";
  public static final String EMBEDDED                 = "embedded";
  public static final String RELATIONSHIPS            = "relationships";
  public static final String KLASS_TYPE               = "klassType";
  public static final String NATURE_KLASS_LABEL       = "natureKlassLabel";
  public static final String NON_NATURE_KLASS_LABELS  = "nonNatureKlassLabels";
  public static final String TAXONOMY_LABELS          = "taxonomyLabels";
  public static final String SELECTED_TAXONOMY_LABELS = "selectedTaxonomyLabels";
  public static final String CONTEXT                  = "context";
  public static final String PRESET_ID                = "presetId";
  
  public String getId();
  
  public void setId(String id);
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public String getImageUrl();
  
  public void setImageUrl(String imageUrl);
  
  public String getPreviewImageUrl();
  
  public void setPreviewImageUrl(String previewImageUrl);
  
  public Map<String, ISmartDocumentAttributeInstanceDataModel> getAttributes();
  
  public void setAttributes(Map<String, ISmartDocumentAttributeInstanceDataModel> attributes);
  
  public Map<String, ISmartDocumentTagInstanceDataModel> getTags();
  
  public void setTags(Map<String, ISmartDocumentTagInstanceDataModel> tags);
  
  public String getKlassType();
  
  public void setKlassType(String klassType);
  
  public String getNatureKlassLabel();
  
  public void setNatureKlassLabel(String natureKlassLabel);
  
  public List<String> getNonNatureKlassLabels();
  
  public void setNonNatureKlassLabels(List<String> nonNatureKlassLabels);
  
  public List<List<String>> getTaxonomyLabels();
  
  public void setTaxonomyLabels(List<List<String>> taxonomyLabels);
  
  public List<String> getSelectedTaxonomyLabels();
  
  public void setSelectedTaxonomyLabels(List<String> selectedTaxonomyLabels);
  
  public ISmartDocumentContextDataModel getContext();
  public void setContext(ISmartDocumentContextDataModel context);
  
  public String getPresetId();
  public void setPresetId(String presetId);
}
