package com.cs.core.runtime.strategy.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.propertycollection.ISection;
import com.cs.core.config.interactor.entity.propertycollection.Section;
import com.cs.core.config.strategy.usecase.standard.properties.IGetAllStandardPropertiesStrategy;

@Component
public class SectionWithMandatoryAttributes {
  
  @Autowired
  IGetAllStandardPropertiesStrategy neo4jGetAllStandardPropertiesStrategy;
  
  public List<ISection> getSectionsWithMandatoryAttributesForTarget(boolean isFolder)
      throws Exception
  {
    List<ISection> defaultSections = new ArrayList<>();
    ISection basicInfoSection = new Section(
        SystemLevelIds.MARKET_GENERAL_INFORMATION_PROPERTY_COLLECTION);
    basicInfoSection.setSequence(0);
    
    defaultSections.add(basicInfoSection);
    
    return defaultSections;
  }
  
  public List<ISection> getSectionsWithMandatoryAttributesForAsset(boolean isFolder)
      throws Exception
  {
    List<ISection> defaultSections = new ArrayList<>();
    ISection basicInfoSection = new Section(
        SystemLevelIds.ASSET_GENERAL_INFORMATION_PROPERTY_COLLETION);
    basicInfoSection.setSequence(0);
    
    // ISection metadataInfoSection = new
    // Section(SystemLevelIds.MEDIA_METADATA_COLLECTION);
    ISection metadataInfoSection = new Section(SystemLevelIds.METADATA_COLLECTION);
    metadataInfoSection.setSequence(1);
    
    defaultSections.add(basicInfoSection);
    // defaultSections.add(metadataInfoSection);
    defaultSections.add(metadataInfoSection);
    
    return defaultSections;
  }
  
  public List<ISection> getSectionsWithMandatoryAttributesForArticle(boolean isFolder)
      throws Exception
  {
    List<ISection> defaultSections = new ArrayList<>();
    
    ISection basicInfoSection = new Section(
        SystemLevelIds.ARTICLE_GENERAL_INFORMATION_PROPERTY_COLLECTION);
    basicInfoSection.setSequence(0);
    
    defaultSections.add(basicInfoSection);
    
    ISection priceSection = new Section(SystemLevelIds.PRICING_PROPERTY_COLLECTION);
    priceSection.setSequence(1);
    defaultSections.add(priceSection);
    
    return defaultSections;
  }
}
