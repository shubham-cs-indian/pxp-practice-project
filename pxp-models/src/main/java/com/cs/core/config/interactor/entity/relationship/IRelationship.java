package com.cs.core.config.interactor.entity.relationship;

import com.cs.core.config.interactor.entity.configuration.base.IConfigMasterPropertyEntity;
import com.cs.core.config.interactor.entity.propertycollection.ISection;

import java.util.List;

public interface IRelationship extends IConfigMasterPropertyEntity {
  
  public static final String AUTO_UPDATE       = "autoUpdate";
  public static final String SIDE1             = "side1";
  public static final String SIDE2             = "side2";
  public static final String SECTIONS          = "sections";
  public static final String RHYTHM            = "rhythm";
  public static final String TAB_ID            = "tabId";
  public static final String IS_NATURE         = "isNature";
  public static final String PROPERTY_IID      = "propertyIID";
  public static final String ENABLE_AFTER_SAVE = "enableAfterSave";
  public static final String IS_LITE           = "isLite";

  
  public Boolean getAutoUpdate();
  
  public void setAutoUpdate(Boolean autoUpdate);
  
  public IRelationshipSide getSide1();
  
  public void setSide1(IRelationshipSide side1);
  
  public IRelationshipSide getSide2();
  
  public void setSide2(IRelationshipSide side2);
  
  public List<? extends ISection> getSections();
  
  public void setSections(List<? extends ISection> sections);
  
  public String getRhythm();
  
  public void setRhythm(String rhythm);
  
  public String getTabId();
  
  public void setTabId(String tabId);
  
  public Boolean getIsNature();
  
  public void setIsNature(Boolean isNature);
  
  public long getPropertyIID();
  
  public void setPropertyIID(long relationshipIID);
  
  public Boolean getEnableAfterSave();
  
  public void setEnableAfterSave(Boolean enableAfterSave);
  
  public Boolean getIsLite();
  
  public void setIsLite(Boolean isLite);
  
}
