package com.cs.core.initialize;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.config.standard.IConfigMap;
import com.cs.constants.CommonConstants;
import com.cs.constants.initialize.InitializeDataConstants;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.tag.Tag;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.tag.ITagModel;
import com.cs.core.config.interactor.model.tag.ITagTypeModel;
import com.cs.core.config.interactor.model.tag.TagModel;
import com.cs.core.config.interactor.model.tag.TagTypeModel;
import com.cs.core.config.interactor.model.translations.ISaveTranslationsRequestModel;
import com.cs.core.config.strategy.usecase.tagtype.IGetOrCreateTagTypeStrategy;
import com.cs.core.config.strategy.usecase.tagtype.IGetOrCreateTagsStrategy;
import com.cs.core.config.strategy.usecase.translations.ICreateOrSavePropertiesTranslationsStrategy;
import com.cs.core.interactor.usecase.initialize.utils.GetTranslationsForInitialization;
import com.cs.core.interactor.usecase.initialize.utils.ValidationUtils;
import com.cs.core.rdbms.config.idao.IConfigurationDAO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.core.type.TypeReference;

@Component
public class InitializeTagsService implements IInitializeTagsService {
  
  @Autowired
  protected IGetOrCreateTagTypeStrategy                 getOrCreateTagTypeStrategy;
  
  @Autowired
  protected IGetOrCreateTagsStrategy                    getOrCreateTagsStrategy;
  
  @Autowired
  protected ICreateOrSavePropertiesTranslationsStrategy savePropertiesTranslationsStrategy;
  
  @Override
  public void execute() throws Exception
  {
    inititalizeTagTypes();
    initializeTags();
    initializeTagTranslations();
  }
  
  private void initializeTagTranslations() throws Exception
  {
    ISaveTranslationsRequestModel translationRequestDataModel = GetTranslationsForInitialization
        .getSaveTranslationsRequestModel(InitializeDataConstants.TAG_TRANSLATIONS_JSON,
            CommonConstants.TAG);
    savePropertiesTranslationsStrategy.execute(translationRequestDataModel);
  }
  
  private void inititalizeTagTypes() throws Exception
  {
    InputStream stream = this.getClass()
        .getClassLoader()
        .getResourceAsStream(InitializeDataConstants.TAG_TYPES_JSON);
    List<TagTypeModel> tagTypes = ObjectMapperUtil.readValue(stream,
        new TypeReference<List<TagTypeModel>>()
        {
        });
    stream.close();
    
    for (ITagTypeModel tagType : tagTypes) {
      ValidationUtils.validateTagTypes(tagType);
    }
    
    IListModel<ITagTypeModel> tagTypesList = new ListModel<>();
    tagTypesList.setList(tagTypes);
    
    getOrCreateTagTypeStrategy.execute(tagTypesList);
  }
  
  private void initializeTags() throws Exception
  {
    List<ITagModel> tagModels = new ArrayList<>();
    InputStream stream = this.getClass()
        .getClassLoader()
        .getResourceAsStream(InitializeDataConstants.TAGS_JSON);
    List<ITag> tags = ObjectMapperUtil.readValue(stream, new TypeReference<List<ITag>>()
    {
    });
    stream.close();
    
    IConfigurationDAO configurationDAO = RDBMSUtils.newConfigurationDAO();
    for (ITag tag : tags) {
      tag.setIsDimensional(true);
      tag.setIsMultiselect(false);
      tag.setShouldDisplay(true);
      tag.setIsRoot(true);
      ITagModel tagModel = new TagModel((Tag) tag);
      
      ValidationUtils.validateTag(tag, false);
      
      PropertyType propertyType = IConfigMap.getPropertyType(tag.getTagType());
      long propertyIID = tag.getPropertyIID();
      configurationDAO.createStandardProperty(propertyIID, tag.getCode(), propertyType);
      
      tagModels.add(tagModel);
      @SuppressWarnings("unchecked")
      List<ITag> tagChildrens = (List<ITag>) tag.getChildren();
      
      for (ITag childTag : tagChildrens) {
        childTag.setParent(tag);
        ITagModel childTagModel = new TagModel((Tag) childTag);
        
        ValidationUtils.validateTag(childTag, true);
        
        configurationDAO.createTagValue(childTag.getCode(), propertyIID);
        
        tagModels.add(childTagModel);
      }
      tagModel.setChildren(null);
    }
    
    IListModel<ITagModel> tagsListModel = new ListModel<>();
    tagsListModel.setList(tagModels);
    
    getOrCreateTagsStrategy.execute(tagsListModel);
  }
}
