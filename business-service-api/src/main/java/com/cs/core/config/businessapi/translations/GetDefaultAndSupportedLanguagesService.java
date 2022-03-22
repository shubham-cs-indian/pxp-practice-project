package com.cs.core.config.businessapi.translations;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.entity.configuration.base.ITreeEntity;
import com.cs.core.config.interactor.entity.tag.ITagBasic;
import com.cs.core.config.interactor.model.configdetails.ConfigEntityInformationModel;
import com.cs.core.config.interactor.model.tag.GetTagModel;
import com.cs.core.config.interactor.model.tag.IGetTagModel;
import com.cs.core.config.interactor.model.tag.ITagModel;
import com.cs.core.config.interactor.model.translations.DefaultAndSupportingLanguagesModel;
import com.cs.core.config.interactor.model.translations.IDefaultAndSupportingLanguagesModel;
import com.cs.core.config.strategy.usecase.tag.IGetTagStrategy;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetDefaultAndSupportedLanguagesService extends AbstractGetConfigService<IIdParameterModel, IDefaultAndSupportingLanguagesModel>
    implements IGetDefaultAndSupportedLanguagesService {
  
  @Autowired
  protected IGetTagStrategy getTagStrategy;
  
  @Override
  public IDefaultAndSupportingLanguagesModel executeInternal(IIdParameterModel dataModel) throws Exception
  {
    IGetTagModel tagModel = new GetTagModel("language", null);
    ITagModel tagInfo = getTagStrategy.execute(tagModel);
    List<? extends ITreeEntity> tagValues = tagInfo.getChildren();
    List<IConfigEntityInformationModel> supportedLanguages = new ArrayList<>();
    for (ITreeEntity tagValue : tagValues) {
      IConfigEntityInformationModel infoModel = new ConfigEntityInformationModel();
      infoModel.setId(tagValue.getId());
      infoModel.setLabel(((ITagBasic) tagValue).getLabel());
      infoModel.setCode(((ITagBasic) tagValue).getCode());
      infoModel.setIcon(((ITagBasic) tagValue).getIcon());
      supportedLanguages.add(infoModel);
    }
    // TODO : remove hardcoded default language
    IDefaultAndSupportingLanguagesModel returnModel = new DefaultAndSupportingLanguagesModel();
    returnModel.setDefaultLanguage("en_US");
    returnModel.setSupportedLanguage(supportedLanguages);
    return returnModel;
  }
}
