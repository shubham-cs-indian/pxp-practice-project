package com.cs.core.config.language;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractDeleteConfigService;
import com.cs.core.config.interactor.exception.entityconfiguration.EntityConfigurationDependencyException;
import com.cs.core.config.interactor.model.configdetails.GetEntityConfigurationRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetEntityConfigurationResponseModel;
import com.cs.core.config.interactor.model.language.IDeleteLanguageRequestModel;
import com.cs.core.config.interactor.model.language.IGetChildLanguageCodeAgainstLanguageIdReturnModel;
import com.cs.core.config.strategy.usecase.entityconfiguration.IGetEntityConfigurationStrategy;
import com.cs.core.config.strategy.usecase.language.IDeleteLanguageStrategy;
import com.cs.core.config.strategy.usecase.language.IGetChildLanguageCodeVersusLanguageIdStrategy;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.runtime.interactor.exception.delete.InstanceExistsForLanguageException;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;

@Service
public class DeleteLanguageService extends AbstractDeleteConfigService<IDeleteLanguageRequestModel, IBulkDeleteReturnModel>
    implements IDeleteLanguageService {
  
  @Autowired
  protected IDeleteLanguageStrategy deleteLanguageStrategy;
  
  @Autowired
  protected IGetEntityConfigurationStrategy      getLanguageEntityConfigurationStrategy;
  
  @Autowired
  protected IGetChildLanguageCodeVersusLanguageIdStrategy      getChildLanguageCodeVersusLanguageIdStrategy;

  @SuppressWarnings("unchecked")
  @Override
  public IBulkDeleteReturnModel executeInternal(IDeleteLanguageRequestModel dataModel) throws Exception
  {

    IGetEntityConfigurationResponseModel getEntityResponse = getLanguageEntityConfigurationStrategy
        .execute(new GetEntityConfigurationRequestModel(dataModel.getIds()));
    Map<String, IIdLabelCodeModel> referenceData = getEntityResponse.getReferenceData();
    boolean hasChildDependency = getEntityResponse.isHasChildDependency();

    if (!referenceData.keySet()
        .isEmpty() || hasChildDependency) {
      throw new EntityConfigurationDependencyException();
    }

    IGetChildLanguageCodeAgainstLanguageIdReturnModel childLanguageCodeAgainstLanguageIdReturnModel = getChildLanguageCodeVersusLanguageIdStrategy
        .execute(dataModel);
    List<String> languageIds = new ArrayList<String>();
    childLanguageCodeAgainstLanguageIdReturnModel.getChildLanguageCodeAgainstLanguageId().forEach((key, values) -> {
      values.forEach((value) -> {
        languageIds.add(value);
      });
    });
    
    if (!languageIds.isEmpty()) {
      List<Long> baseentityIIDs = RDBMSUtils.getBaseentityIIDsForLanguage(languageIds);
      if (!baseentityIIDs.isEmpty()) {
        throw new InstanceExistsForLanguageException();
      }
    }
    
    IBulkDeleteReturnModel response = deleteLanguageStrategy.execute(dataModel);
    List<String> success = (List<String>) response.getSuccess();
    
    ConfigurationDAO.instance().deleteLanguageConfigByCodes(success);
     
     return response;
    
  }

}
