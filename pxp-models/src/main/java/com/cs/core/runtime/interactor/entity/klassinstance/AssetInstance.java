package com.cs.core.runtime.interactor.entity.klassinstance;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import com.cs.core.runtime.interactor.entity.eventinstance.EventInstanceSchedule;
import com.cs.core.runtime.interactor.entity.eventinstance.IEventInstanceLocation;
import com.cs.core.runtime.interactor.entity.eventinstance.IEventInstanceNotification;
import com.cs.core.runtime.interactor.entity.eventinstance.IEventInstanceSchedule;
import com.cs.core.runtime.interactor.entity.eventinstance.ITimeRange;
import com.cs.core.runtime.interactor.model.assetinstance.AssetInformationModel;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetInformationModel;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonTypeInfo(use = Id.NONE)
public class AssetInstance extends AbstractContentInstance implements IAssetInstance {
  
  private static final long                  serialVersionUID = 1L;
  
  protected IEventInstanceSchedule           eventSchedule;
  protected IAssetInformationModel           assetInformation;
  protected IEventInstanceLocation           location;
  protected TimeZone                         timezone;
  protected List<IEventInstanceNotification> notifications;
  protected List<ITimeRange>                 calendarDates;
  protected List<IEventInstanceSchedule>     exclusions       = new ArrayList<>();
  protected List<IEventInstanceSchedule>     inclusions       = new ArrayList<>();
  protected boolean                          isExpired        = false;
  
  @Override
  public IEventInstanceSchedule getEventSchedule()
  {
    return eventSchedule;
  }
  
  @JsonDeserialize(as = EventInstanceSchedule.class)
  @Override
  public void setEventSchedule(IEventInstanceSchedule schedule)
  {
    this.eventSchedule = schedule;
  }

  @Override
  public IAssetInformationModel getAssetInformation()
  {
    return assetInformation;
  }
  
  @JsonDeserialize(as = AssetInformationModel.class)
  @Override
  public void setAssetInformation(IAssetInformationModel assetInformation)
  {
    this.assetInformation = assetInformation;
  }

  @Override
  public boolean getIsExpired()
  {
    return isExpired;
  }

  @Override
  public void setIsExpired(boolean isExpired)
  {
    this.isExpired = isExpired;
  }
  
  
  
  
}
