package com.cs.core.config.language;

import com.cs.config.businessapi.base.Validations;
import com.cs.core.config.interactor.model.language.ICreateLanguageModel;
import com.cs.core.config.interactor.model.language.ILanguageModel;
import com.cs.core.config.strategy.usecase.language.IGetLanguageByLocaleIdStrategy;
import com.cs.core.exception.*;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idto.ILanguageConfigDTO;
import com.cs.core.runtime.interactor.exception.language.LanguageNotFoundException;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LanguageValidations extends Validations {

  @Autowired
  public IGetLanguageByLocaleIdStrategy getLanguageByLocaleIdStrategy;

  public boolean isValidDateFormat(String dateFormat)
  {
    return Locales.getAllDateFormats().contains(dateFormat);
  }

  public boolean isValidNumberFormat(String numberFormat)
  {
    return Locales.getAllNumberFormats().contains(numberFormat);
  }

  public boolean isParentValid(String parentId) throws RDBMSException
  {
    if("-1".equals(parentId)){
      return true;
    }
    if (Locales.getAllLocales().contains(parentId)) {
      ILanguageConfigDTO languageConfig = ConfigurationDAO.instance().getLanguageConfig(parentId);
      return languageConfig != null;
    }
    return false;
  }

  public boolean isLocaleIdValid(String localeId)
  {
    return Locales.getAllLocaleIds().contains(localeId);
  }

  public boolean isLocaleIdDuplicate(String localeId) throws Exception
  {
    try {
      getLanguageByLocaleIdStrategy.execute(new IdParameterModel(localeId));
    }
    catch(LanguageNotFoundException ex){
      return false;
    }
    return true;
  }

  public void validateLanguageCreation(ICreateLanguageModel dataModel) throws Exception
  {
    if (!isParentValid(dataModel.getParentId())) {
      throw new InvalidParentIdException();
    }
    if (!isLocaleIdValid(dataModel.getLocaleId())) {
      throw new InvalidLocaleIdException();
    }
    if (isLocaleIdDuplicate(dataModel.getLocaleId())) {
      throw new DuplicateLocaleException();
    }
    if (!isLanguageCodeValid(dataModel)) {
      throw new InvalidCodeException();
    }
    validateLanguage(dataModel);
  }

  protected boolean isLanguageCodeValid(ILanguageModel dataModel) throws InvalidCodeException
  {
    if(!isCodeValid(dataModel.getCode())){
      return false;
    }
    Locales locale = Locales.getLocaleByLocaleIds(dataModel.getLocaleId());
    if(locale == null){
      return false;
    }
    String x = locale.locale;
    String expectedCode = x.replace("-", "_");
    return expectedCode.equals(dataModel.getCode());
  }

  protected void validateLanguage(ILanguageModel dataModel) throws Exception
  {
    if (!isValidDateFormat(dataModel.getDateFormat())) {
      throw new InvalidDateFormatException();
    }
    if (!isValidNumberFormat(dataModel.getNumberFormat())) {
      throw new InvalidNumberFormatException();
    }
    if (isEmpty(dataModel.getAbbreviation())) {
      throw new InvalidAbbreviationException();
    }
    if (!isLocaleIdValid(dataModel.getLocaleId())) {
      throw new InvalidLocaleIdException();
    }
    if (!isLanguageCodeValid(dataModel)) {
      throw new InvalidCodeException();
    }
  }

}
