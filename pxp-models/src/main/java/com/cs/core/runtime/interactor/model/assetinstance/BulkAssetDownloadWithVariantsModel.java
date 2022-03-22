package com.cs.core.runtime.interactor.model.assetinstance;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.runtime.interactor.entity.eventinstance.ITimeRange;
import com.cs.core.runtime.interactor.entity.eventinstance.TimeRange;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class BulkAssetDownloadWithVariantsModel implements IBulkAssetDownloadWithVariantsModel {
  
  private static final long                             serialVersionUID                        = 1L;
  protected List<IAssetDownloadInformationWithTIVModel> downloadInformation                     = new ArrayList<>();
  protected boolean                                     shouldDownloadAssetWithOriginalFilename = false;
  protected String                                      downloadFileName;
  protected String                                      comments;
  protected boolean                                     shouldCreateSeparateFolders             = false;
  protected boolean                                     isDownloadedFromInstance                = false;
  protected ITimeRange                                  downloadWithinTimeRange                 = new TimeRange();
  protected boolean                                     shouldTrackDownloads                    = false;
  
  @Override
  public List<IAssetDownloadInformationWithTIVModel> getDownloadInformation()
  {
    return downloadInformation;
  }

  @JsonDeserialize(contentAs = AssetDownloadInformationWithTIVModel.class)
  @Override
  public void setDownloadInformation(List<IAssetDownloadInformationWithTIVModel> downloadInformation)
  {
    this.downloadInformation = downloadInformation;
  }
  
  @Override
  public boolean getShouldDownloadAssetWithOriginalFilename()
  {
    return shouldDownloadAssetWithOriginalFilename;
  }
  
  @Override
  public void setShouldDownloadAssetWithOriginalFilename(boolean shouldDownloadAssetWithOriginalFilename)
  {
    this.shouldDownloadAssetWithOriginalFilename = shouldDownloadAssetWithOriginalFilename;
  }
  
  @Override
  public String getDownloadFileName()
  {
    return downloadFileName;
  }
  
  @Override
  public void setDownloadFileName(String downloadFileName)
  {
    this.downloadFileName = downloadFileName;
  }
  
  @Override
  public String getComments()
  {
    return comments;
  }
  
  @Override
  public void setComments(String comments)
  {
    this.comments = comments;
  }

  @Override
  public boolean getShouldCreateSeparateFolders()
  {
    return shouldCreateSeparateFolders;
  }

  @Override
  public void setShouldCreateSeparateFolders(boolean shouldCreateSeparateFolders)
  {
    this.shouldCreateSeparateFolders = shouldCreateSeparateFolders;
  }
  
  @Override
  public boolean getIsDownloadedFromInstance()
  {
    return isDownloadedFromInstance;
  }

  @Override
  public void setDownloadedFromInstance(boolean isDownloadedFromInstance)
  {
    this.isDownloadedFromInstance = isDownloadedFromInstance;
  }

  @Override
  public ITimeRange getDownloadWithinTimeRange()
  {
    return downloadWithinTimeRange;
  }

  @Override
  @JsonDeserialize(as = TimeRange.class)
  public void setDownloadWithinTimeRange(ITimeRange downloadWithinTimeRange)
  {
    this.downloadWithinTimeRange = downloadWithinTimeRange;
  }

  @Override
  public boolean getShouldTrackDownloads()
  {
    return shouldTrackDownloads;
  }

  @Override
  public void setShouldTrackDownloads(boolean shouldTrackDownloads)
  {
    this.shouldTrackDownloads = shouldTrackDownloads;
  }

}
