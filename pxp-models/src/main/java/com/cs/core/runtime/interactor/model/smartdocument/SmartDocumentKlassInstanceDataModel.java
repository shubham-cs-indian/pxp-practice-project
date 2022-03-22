package com.cs.core.runtime.interactor.model.smartdocument;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SmartDocumentKlassInstanceDataModel implements ISmartDocumentKlassInstanceDataModel {
  
  private static final long                                       serialVersionUID       = 1L;
  protected String                                                id;
  protected String                                                label;
  protected String                                                imageUrl;
  protected String                                                previewImageUrl;
  protected Map<String, ISmartDocumentAttributeInstanceDataModel> attributes             = new HashMap<>();
  protected Map<String, ISmartDocumentTagInstanceDataModel>       tags                   = new HashMap<>();
  protected String                                                klassType;
  protected String                                                natureKlassLabel;
  protected List<String>                                          nonNatureKlassLabels   = new ArrayList<>();
  protected List<List<String>>                                    taxonomyLabels         = new ArrayList<>();
  protected List<String>                                          selectedTaxonomyLabels = new ArrayList<>();
  protected ISmartDocumentContextDataModel                        context;
  protected String                                                presetId;
  
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
  public Map<String, ISmartDocumentAttributeInstanceDataModel> getAttributes()
  {
    return attributes;
  }
  
  @Override
  @JsonDeserialize(contentAs = SmartDocumentAttributeInstanceDataModel.class)
  public void setAttributes(Map<String, ISmartDocumentAttributeInstanceDataModel> attributes)
  {
    this.attributes = attributes;
  }
  
  @Override
  public String getImageUrl()
  {
    return imageUrl;
  }
  
  @Override
  public void setImageUrl(String imageUrl)
  {
    this.imageUrl = imageUrl;
  }
  
  @Override
  public String getPreviewImageUrl()
  {
    return previewImageUrl;
  }
  
  @Override
  public void setPreviewImageUrl(String previewImageUrl)
  {
    this.previewImageUrl = previewImageUrl;
  }
  
  @Override
  public String getKlassType()
  {
    return klassType;
  }
  
  @Override
  public void setKlassType(String klassType)
  {
    this.klassType = klassType;
  }
  
  @Override
  public Map<String, ISmartDocumentTagInstanceDataModel> getTags()
  {
    return tags;
  }
  
  @Override
  @JsonDeserialize(contentAs = SmartDocumentTagInstanceDataModel.class)
  public void setTags(Map<String, ISmartDocumentTagInstanceDataModel> tags)
  {
    this.tags = tags;
  }
  
  @Override
  public String getNatureKlassLabel()
  {
    return natureKlassLabel;
  }
  
  @Override
  public void setNatureKlassLabel(String natureKlassLabel)
  {
    this.natureKlassLabel = natureKlassLabel;
  }
  
  @Override
  public List<String> getNonNatureKlassLabels()
  {
    return nonNatureKlassLabels;
  }
  
  @Override
  public void setNonNatureKlassLabels(List<String> nonNatureKlassLabels)
  {
    this.nonNatureKlassLabels = nonNatureKlassLabels;
  }
  
  @Override
  public List<List<String>> getTaxonomyLabels()
  {
    return taxonomyLabels;
  }
  
  @Override
  public void setTaxonomyLabels(List<List<String>> taxonomyLabels)
  {
    this.taxonomyLabels = taxonomyLabels;
  }
  
  @Override
  public List<String> getSelectedTaxonomyLabels()
  {
    return selectedTaxonomyLabels;
  }
  
  @Override
  public void setSelectedTaxonomyLabels(List<String> selectedTaxonomyLabels)
  {
    this.selectedTaxonomyLabels = selectedTaxonomyLabels;
  }
  
  @Override
  public ISmartDocumentContextDataModel getContext()
  {
    return context;
  }

  @Override
  public void setContext(ISmartDocumentContextDataModel context)
  {
    this.context = context;
  }

  @Override
  public String getPresetId()
  {
    return presetId;
  }

  @Override
  public void setPresetId(String presetId)
  {
    this.presetId = presetId;
  }
}
