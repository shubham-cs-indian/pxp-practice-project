package com.cs.core.config.interactor.model.asset;

import com.cs.core.config.interactor.entity.configuration.base.ITreeEntity;
import com.cs.core.config.interactor.entity.klass.Asset;
import com.cs.core.config.interactor.entity.klass.IAsset;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.datarule.IDataRuleModel;
import com.cs.core.config.interactor.model.klass.AbstractKlassSaveModel;
import com.cs.core.config.interactor.model.variantcontext.IGetVariantContextModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class AssetKlassSaveModel extends AbstractKlassSaveModel implements IAssetKlassSaveModel {
  
  private static final long                         serialVersionUID               = 1L;
  
  protected Boolean                                 detectDuplicate;
  protected Boolean                                 isDetectDuplicateModified               = false;
  protected Boolean                                 uploadZip;
  protected List<IAssetExtensionConfigurationModel> addedExtensionConfiguration             = new ArrayList<>();
  protected List<String>                            deletedExtensionConfiguration           = new ArrayList<>();
  protected List<IAssetExtensionConfigurationModel> modifiedExtensionConfiguration          = new ArrayList<>();
  protected List<IAssetExtensionConfiguration>      extensionConfiguration                  = new ArrayList<IAssetExtensionConfiguration>();
  protected Boolean                                 indesignServer;
  protected boolean                                 trackDownloads                          = false;
  protected boolean                                 shouldDownloadAssetWithOriginalFilename = false;
  
  public AssetKlassSaveModel()
  {
    super(new Asset());
  }
  
  public AssetKlassSaveModel(IAsset klass)
  {
    super(klass);
  }
  
  @JsonDeserialize(as = Asset.class)
  @Override
  public ITreeEntity getParent()
  {
    return this.entity.getParent();
  }
  
  @JsonIgnore
  @Override
  public List<? extends ITag> getReferencedTags()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setReferencedTags(List<? extends ITag> referencedTags)
  {
    // TODO Auto-generated method stub
    
  }
  
  @JsonIgnore
  @Override
  public List<IGetVariantContextModel> getReferencedContexts()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setReferencedContexts(List<IGetVariantContextModel> referencedContexts)
  {
    // TODO Auto-generated method stub
    
  }
  
  @JsonIgnore
  @Override
  public List<IDataRuleModel> getDataRulesOfKlass()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setDataRulesOfKlass(List<IDataRuleModel> dataRulesOfKlass)
  {
    // TODO Auto-generated method stub
    
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
  
  public List<IAssetExtensionConfigurationModel> getAddedExtensionConfiguration()
  {
    return addedExtensionConfiguration;
  }
  
  @Override
  @JsonDeserialize(contentAs = AssetExtensionConfigurationModel.class)
  public void setAddedExtensionConfiguration(
      List<IAssetExtensionConfigurationModel> addedExtensionConfiguration)
  {
    this.addedExtensionConfiguration = addedExtensionConfiguration;
  }
  
  @Override
  public List<String> getDeletedExtensionConfiguration()
  {
    return deletedExtensionConfiguration;
  }
  
  @Override
  public void setDeletedExtensionConfiguration(List<String> deletedExtensionConfiguration)
  {
    this.deletedExtensionConfiguration = deletedExtensionConfiguration;
  }
  
  @Override
  public List<IAssetExtensionConfigurationModel> getModifiedExtensionConfiguration()
  {
    return modifiedExtensionConfiguration;
  }
  
  @Override
  @JsonDeserialize(contentAs = AssetExtensionConfigurationModel.class)
  public void setModifiedExtensionConfiguration(
      List<IAssetExtensionConfigurationModel> modifiedExtensionConfiguration)
  {
    this.modifiedExtensionConfiguration = modifiedExtensionConfiguration;
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
