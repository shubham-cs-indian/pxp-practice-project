package com.cs.core.runtime.interactor.entity.klassinstance;

import com.cs.core.runtime.interactor.entity.eventinstance.IEventInstanceSchedule;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetInformationModel;

public interface IAssetInstance extends IContentInstance {
  
  public static final String EVENT_SCHEDULE = "eventSchedule";
  public static final String ASSET_INFORMATION = "assetInformation";
  public static final String IS_EXPIRED        = "isExpired";
  
  public void setEventSchedule(IEventInstanceSchedule schedule);
  
  public IEventInstanceSchedule getEventSchedule();
  
  public IAssetInformationModel getAssetInformation();
  
  public void setAssetInformation(IAssetInformationModel assetInformation);
  
  public boolean getIsExpired();
  
  public void setIsExpired(boolean isExpired);
}
