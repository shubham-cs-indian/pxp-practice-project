package com.cs.config.idto;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.cs.config.idto.IConfigJSONDTO.ConfigTag;
import com.cs.core.dataintegration.idto.IPXON;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO.ClassifierType;
import com.cs.core.technical.ijosn.IJSONContent;

/**
 * @author vallee
 */
public interface IConfigClassifierDTO extends IConfigJSONDTO {

  public long getClassifierIID();
  
  public void setClassifierIID(long iid);

  public String getParentCode();
  
  public void setParentCode(String parentCode);

  public String getType();
  
  public String getLabel();
  
  public void setLabel(String label);

  public boolean isStandard();
  
  public void setIsStandard(boolean isStandard);

  public int getNumberOfVersionsToMaintain();
  
  public void setNumberOfVersionsToMaintain(int numberOfVersion);

  public boolean isDefault();
  
  public void setIsDefault(boolean isDefault);

  public boolean isAbstract();
  
  public void setIsAbstract(boolean isAbstract);

  public String getNatureType();
  
  public void setNatureType(String natureType);

  public boolean isNature();
  
  public void setIsNature(boolean isNature);

  public Collection<String> getEvents();
  
  public void setEvents(Collection<String> events);

  public Collection<String> getTasks();
  
  public void setTasks(Collection<String> tasks);

  public String getContextCode();
  
  public void setContextCode(String contextCode);

  public Collection<String> getStatusTags();
  
  public void setStatusTag(Collection<String> statusTags);

  public Collection<String> getProductVariantContextCode();
  
  public void setProductVariantContextCode(Collection<String> productVariantContextCode);

  public Collection<String> getPromotionalVersionContextCode();
  
  public void setPromotionalVersionContextCode(Collection<String> promotionalVersionContextCode);

  public Collection<IConfigNatureRelationshipDTO> getRelationships();

  public Map<String, Collection<IConfigSectionElementDTO>> getSections();

  public Map<String, IConfigEmbeddedRelationDTO> getEmbeddedClasses();

  public List<String> getLevelCodes();
  
  public String getMasterParentTag();
  
  public void setMasterParentTag(String masterParentTag);
  
  public void setSubType(String subType);
  
  public IClassifierDTO getClassifierDTO();
  
  public String getSubType();
  
  
  public void setClassifierDTO(String code, ClassifierType type);
  
  public List<String> getLevelLables();
  
  /*
   * set major taxonomy or minor taxonomy
   */
  public void setConfigTaxonomyType(String taxonomyType);
  
  public String getConfigTaxonomyType();
  
  public String getIcon();
  
  public void setIcon(String icon);
  
  public String getPreviewImage();
  
  public void setPreviewImage(String previewImage);
  
  public void setExtensionConfiguration(List<IJSONContent> extensionConfiguration);
  
  public List<IJSONContent> getExtensionConfiguration();
  
  public int getLevelIndex();
  
  //set for taxonomy level index
  public void setLevelIndex(int levelIndex);
  
  public List<Boolean> getIsNewlyCreated();
  
  //set for taxonomy level is newly created or not
  public void setIsNewlyCreated(List<Boolean> newlyCreated);
  
  public boolean isDetectDuplicate();
  
  /**
   * @param isDetectDuplicate set detect duplicate for 
   */
  public void isDetectDuplicate(boolean isDetectDuplicate);
  
  public boolean isUploadZip();
  
  /**
   * @param isUploadZip set upload zip allowed or not for asset class
   */
  public void isUploadZip(boolean isUploadZip);
  
  public boolean isTrackDownloads();
  
  /**
   * @param isTrackDownloads set track downloads allowed for asset class
   */
  public void isTrackDownloads(boolean isTrackDownloads);
}
