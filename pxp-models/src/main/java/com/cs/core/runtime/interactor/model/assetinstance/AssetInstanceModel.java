package com.cs.core.runtime.interactor.model.assetinstance;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.runtime.interactor.entity.eventinstance.IEventInstanceSchedule;
import com.cs.core.runtime.interactor.entity.klassinstance.AssetInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.IAssetInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstanceRelationshipInstance;
import com.cs.core.runtime.interactor.model.instance.AbstractContentInstanceModel;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonTypeInfo(use = Id.NONE)
public class AssetInstanceModel extends AbstractContentInstanceModel
    implements IAssetInstanceModel {
  
  private static final long                                    serialVersionUID                   = 1L;
  
  protected List<? extends IKlassInstanceRelationshipInstance> klassInstanceRelationshipInstances = new ArrayList<>();
  protected IKlass                                             typeKlass;
  
  public AssetInstanceModel()
  {
    this.entity = new AssetInstance();
  }
  
  public AssetInstanceModel(IAssetInstance assetInstance)
  {
    this.entity = assetInstance;
  }
  
  /* @JsonDeserialize(contentAs = KlassInstanceImage.class)
  @Override
  public void setAddedAssets(List<? extends IKlassInstanceImage> usedImageKeys)
  {
    this.addedAssets = usedImageKeys;
  }
  
  @Override
  public List<? extends IKlassInstanceImage> getAddedAssets()
  {
    if (addedAssets == null) {
      addedAssets = new ArrayList<>();
    }
    return this.addedAssets;
  }
  
  @JsonDeserialize(contentAs = KlassInstanceImage.class)
  @Override
  public List<? extends IKlassInstanceImage> getDeletedAssets()
  {
    if (deletedAssets == null) {
      deletedAssets = new ArrayList<>();
    }
    return deletedAssets;
  }
  
  @JsonDeserialize(contentAs = KlassInstanceImage.class)
  @Override
  public void setDeletedAssets(List<? extends IKlassInstanceImage> deletedImageIds)
  {
    this.deletedAssets = deletedImageIds;
  }*/
  
  @Override
  public String toString()
  {
    try {
      return ObjectMapperUtil.writeValueAsString(this);
    }
    catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
  
  @Override
  public String getBranchOf()
  {
    return ((IAssetInstance) entity).getBranchOf();
  }
  
  @Override
  public void setBranchOf(String branchOf)
  {
    ((IAssetInstance) entity).setBranchOf(branchOf);
  }
  
  @Override
  public IEventInstanceSchedule getEventSchedule()
  {
    return ((IAssetInstance) entity).getEventSchedule();
  }
  
  @Override
  public void setEventSchedule(IEventInstanceSchedule schedule)
  {
    ((IAssetInstance) entity).setEventSchedule(schedule);
  }

  @Override
  public IAssetInformationModel getAssetInformation()
  {
    return ((IAssetInstance) entity).getAssetInformation();
  }

  @Override
  @JsonDeserialize(as = AssetInformationModel.class)
  public void setAssetInformation(IAssetInformationModel assetInformation)
  {
    ((IAssetInstance) entity).setAssetInformation(assetInformation);
  }


  @Override
  public boolean getIsExpired()
  {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public void setIsExpired(boolean isExpired)
  {
    // TODO Auto-generated method stub
    
  }
  
  /*  @Override
  public IKlass getTypeKlass()
  {
    return typeKlass;
  }
  
  @JsonDeserialize(as = Asset.class)
  @Override
  public void setTypeKlass(IKlass typeKlass)
  {
    this.typeKlass = typeKlass;
  }
  */
  
  /*
    @Override
    public String getLabel()
    {
      return ((IAssetInstance) entity).getLabel();
    }
  
    @Override
    public void setLabel(String label)
    {
      ((IAssetInstance) entity).setLabel(label);
    }
  
    @Override
    public String getType()
    {
      return ((IAssetInstance) entity).getType();
    }
  
    @Override
    public void setType(String type)
    {
      ((IAssetInstance) entity).setType(type);
    }
  
    @Override
    public String getColor()
    {
      return ((IAssetInstance) entity).getColor();
    }
  
    @Override
    public void setColor(String color)
    {
      ((IAssetInstance) entity).setColor(color);
    }
  
    @Override
    public IEventInstanceLocation getLocation()
    {
      return ((IAssetInstance) entity).getLocation();
    }
  
    @JsonDeserialize(as = EventInstanceLocation.class)
    @Override
    public void setLocation(IEventInstanceLocation location)
    {
      ((IAssetInstance) entity).setLocation(location);
    }
  
    @Override
    public TimeZone getTimezone()
    {
      return ((IAssetInstance) entity).getTimezone();
    }
  
    @Override
    public void setTimezone(TimeZone timezone)
    {
      ((IAssetInstance) entity).setTimezone(timezone);;
    }
  
    @Override
    public List<IEventInstanceNotification> getNotifications()
    {
      return ((IAssetInstance) entity).getNotifications();
    }
  
    @JsonDeserialize(contentAs = EventInstanceNotification.class)
    @Override
    public void setNotifications(List<IEventInstanceNotification> notifications)
    {
      ((IAssetInstance) entity).setNotifications(notifications);
    }
  
    @Override
    public IEventInstanceSchedule getEventSchedule()
    {
      return ((IAssetInstance) entity).getEventSchedule();
    }
  
    @JsonDeserialize(as = EventInstanceSchedule.class)
    @Override
    public void setEventSchedule(IEventInstanceSchedule schedule)
    {
      ((IAssetInstance) entity).setEventSchedule(schedule);
    }
  
    @Override
    public List<ITimeRange> getCalendarDates()
    {
      return ((IAssetInstance) entity).getCalendarDates();
    }
  
    @JsonDeserialize(contentAs = TimeRange.class)
    @Override
    public void setCalendarDates(List<ITimeRange> flatDates)
    {
      ((IAssetInstance) entity).setCalendarDates(flatDates);
    }
  
    @Override
    public List<IEventInstanceSchedule> getExclusions()
    {
      return ((IAssetInstance) entity).getExclusions();
    }
  
    @JsonDeserialize(contentAs = EventInstanceSchedule.class)
    @Override
    public void setExclusions(List<IEventInstanceSchedule> exclusions)
    {
      ((IAssetInstance) entity).setExclusions(exclusions);
    }
  
    @Override
    public List<IEventInstanceSchedule> getInclusions()
    {
      return ((IAssetInstance) entity).getInclusions();
    }
  
    @JsonDeserialize(contentAs = EventInstanceSchedule.class)
    @Override
    public void setInclusions(List<IEventInstanceSchedule> inclusions)
    {
      ((IAssetInstance) entity).setInclusions(inclusions);
    }
  
    @Override
    public String getImage()
    {
      return ((IAssetInstance) entity).getImage();
    }
  
    @Override
    public void setImage(String image)
    {
      ((IAssetInstance) entity).setImage(image);
    }
  */
}
