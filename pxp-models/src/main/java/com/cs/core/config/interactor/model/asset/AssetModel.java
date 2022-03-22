package com.cs.core.config.interactor.model.asset;

import com.cs.core.config.interactor.entity.klass.Asset;
import com.cs.core.config.interactor.entity.klass.IAsset;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.tag.Tag;
import com.cs.core.config.interactor.model.datarule.IDataRuleModel;
import com.cs.core.config.interactor.model.klass.AbstractKlassModel;
import com.cs.core.config.interactor.model.tag.DataRuleModel;
import com.cs.core.config.interactor.model.variantcontext.GetVariantContextModel;
import com.cs.core.config.interactor.model.variantcontext.IGetVariantContextModel;
import com.cs.core.config.interactor.model.variantcontext.ITechnicalImageVariantWithAutoCreateEnableModel;
import com.cs.core.config.interactor.model.variantcontext.TechnicalImageVariantWithAutoCreateEnableModel;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

@JsonTypeInfo(use = Id.NONE)
public class AssetModel extends AbstractKlassModel implements IAssetModel {
  
  private static final long                                       serialVersionUID                                 = 1L;
  
  protected List<? extends ITag>                                  referencedTags                                   = new ArrayList<>();
  protected List<IGetVariantContextModel>                         referencedContexts                               = new ArrayList<>();
  protected List<IDataRuleModel>                                  dataRulesOfKlass                                 = new ArrayList<>();
  protected List<ITechnicalImageVariantWithAutoCreateEnableModel> technicalImageVariantContextWithAutoCreateEnable = new ArrayList<>();
  protected Boolean                                               detectDuplicate                                  = false;
  protected Boolean                                               isDetectDuplicateModified                        = false;
  protected Boolean                                               uploadZip                                        = false;
  protected List<IAssetExtensionConfiguration>                    extensionConfiguration                           = new ArrayList<IAssetExtensionConfiguration>();
  protected Boolean                                               indesignServer                                   = false;
  protected boolean                                               trackDownloads                                   = false;
  protected boolean                                               shouldDownloadAssetWithOriginalFilename          = false;
  
  public AssetModel()
  {
    super(new Asset());
  }
  
  public AssetModel(IAsset klass)
  {
    super(klass);
  }
  
  @Override
  public List<? extends ITag> getReferencedTags()
  {
    return referencedTags;
  }
  
  @JsonDeserialize(contentAs = Tag.class)
  @Override
  public void setReferencedTags(List<? extends ITag> referencedTags)
  {
    this.referencedTags = referencedTags;
  }
  
  @Override
  public List<IGetVariantContextModel> getReferencedContexts()
  {
    return referencedContexts;
  }
  
  @JsonDeserialize(contentAs = GetVariantContextModel.class)
  @Override
  public void setReferencedContexts(List<IGetVariantContextModel> referencedContexts)
  {
    this.referencedContexts = referencedContexts;
  }
  
  public List<ITechnicalImageVariantWithAutoCreateEnableModel> getTechnicalImageVariantContextWithAutoCreateEnable()
  {
    return technicalImageVariantContextWithAutoCreateEnable;
  }
  
  @JsonDeserialize(contentAs = TechnicalImageVariantWithAutoCreateEnableModel.class)
  @Override
  public void setTechnicalImageVariantContextWithAutoCreateEnable(
      List<ITechnicalImageVariantWithAutoCreateEnableModel> technicalImageVariantContextWithAutoCreateEnable)
  {
    this.technicalImageVariantContextWithAutoCreateEnable = technicalImageVariantContextWithAutoCreateEnable;
  }
  
  @Override
  public List<IDataRuleModel> getDataRulesOfKlass()
  {
    if (dataRulesOfKlass == null) {
      dataRulesOfKlass = new ArrayList<>();
    }
    return dataRulesOfKlass;
  }
  
  @JsonDeserialize(contentAs = DataRuleModel.class)
  @Override
  public void setDataRulesOfKlass(List<IDataRuleModel> dataRulesOfKlass)
  {
    this.dataRulesOfKlass = dataRulesOfKlass;
  }
  
  @Override
  public Boolean getDetectDuplicate()
  {
    return detectDuplicate;
  }
  
  @Override
  public void setDetectDuplicate(Boolean detectDuplicate)
  {
    this.detectDuplicate = detectDuplicate;
  }
  
  @Override
  public Boolean getUploadZip()
  {
    
    return uploadZip;
  }
  
  @Override
  public void setUploadZip(Boolean uploadZip)
  {
    this.uploadZip = uploadZip;
  }
  
  @Override
  public List<IAssetExtensionConfiguration> getExtensionConfiguration()
  {
    return extensionConfiguration;
  }
  
  @Override
  @JsonDeserialize(contentAs = AssetExtensionConfiguration.class)
  public void setExtensionConfiguration(List<IAssetExtensionConfiguration> extensionConfiguration)
  {
    this.extensionConfiguration = extensionConfiguration;
  }
  
  @Override
  public Boolean getIndesignServer()
  {
    return indesignServer;
  }
  
  @Override
  public void setIndesignServer(Boolean indesignServer)
  {
    this.indesignServer = indesignServer;
  }
  
  @Override
  public boolean getTrackDownloads()
  {
    return this.trackDownloads;
  }

  @Override
  public void setTrackDownloads(boolean trackDownloads)
  {
    this.trackDownloads = trackDownloads;
  }
  
  @Override
  public Boolean getIsDetectDuplicateModified()
  {
    return isDetectDuplicateModified;
  }

  @Override
  public void setIsDetectDuplicateModified(Boolean isDetectDuplicateModified)
  {
    this.isDetectDuplicateModified = isDetectDuplicateModified;
  }

  @Override
  public boolean isShouldDownloadAssetWithOriginalFilename()
  {
    return shouldDownloadAssetWithOriginalFilename;
  }

  @Override
  public void setShouldDownloadAssetWithOriginalFilename(
      boolean shouldDownloadAssetWithOriginalFilename)
  {
    this.shouldDownloadAssetWithOriginalFilename = shouldDownloadAssetWithOriginalFilename;
  }
  
  
}
