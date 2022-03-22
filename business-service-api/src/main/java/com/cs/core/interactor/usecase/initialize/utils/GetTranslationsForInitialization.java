package com.cs.core.interactor.usecase.initialize.utils;

import com.cs.core.config.interactor.model.translations.ISaveRelationshipTranslationModel;
import com.cs.core.config.interactor.model.translations.ISaveRelationshipTranslationsRequestModel;
import com.cs.core.config.interactor.model.translations.ISaveStandardTranslationModel;
import com.cs.core.config.interactor.model.translations.ISaveTranslationsRequestModel;
import com.cs.core.config.interactor.model.translations.SaveRelationshipTranslationModel;
import com.cs.core.config.interactor.model.translations.SaveRelationshipTranslationsRequestModel;
import com.cs.core.config.interactor.model.translations.SaveStandardTranslationsRequestModel;
import com.cs.core.config.interactor.model.translations.SaveTranslationModel;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.InputStream;
import java.util.List;

public class GetTranslationsForInitialization {
  
  public static ISaveTranslationsRequestModel getSaveTranslationsRequestModel(String file,
      String entityType) throws Exception
  {
    
    ISaveTranslationsRequestModel translationRequestDataModel = new SaveStandardTranslationsRequestModel();
    
    translationRequestDataModel.setEntityType(entityType);
    InputStream stream = GetTranslationsForInitialization.class.getClassLoader()
        .getResourceAsStream(file);
    
    List<ISaveStandardTranslationModel> data = ObjectMapperUtil.readValue(stream,
        new TypeReference<List<SaveTranslationModel>>()
        {
        });
    stream.close();
    
    ((SaveStandardTranslationsRequestModel) translationRequestDataModel).setData(data);
    
    return translationRequestDataModel;
  }
  
  public static ISaveRelationshipTranslationsRequestModel getSaveRelationshipAndReferenceTranslationsRequestModel(
      String file, String entityType) throws Exception
  {
    ISaveRelationshipTranslationsRequestModel translationRequestDataModel = new SaveRelationshipTranslationsRequestModel();
    
    translationRequestDataModel.setEntityType(entityType);
    InputStream stream = GetTranslationsForInitialization.class.getClassLoader()
        .getResourceAsStream(file);
    
    List<ISaveRelationshipTranslationModel> data = ObjectMapperUtil.readValue(stream,
        new TypeReference<List<SaveRelationshipTranslationModel>>()
        {
        });
    stream.close();
    
    translationRequestDataModel.setData(data);
    
    return translationRequestDataModel;
  }
}
