package com.cs.core.initialize;

import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.constants.CommonConstants;
import com.cs.constants.initialize.InitializeDataConstants;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.translations.ISaveTranslationsRequestModel;
import com.cs.core.config.interactor.model.variantcontext.ISaveVariantContextModel;
import com.cs.core.config.interactor.model.variantcontext.SaveVariantContextModel;
import com.cs.core.config.strategy.usecase.translations.ICreateOrSavePropertiesTranslationsStrategy;
import com.cs.core.config.strategy.usecase.variantcontext.IGetOrCreateVariantContextsStrategy;
import com.cs.core.interactor.usecase.initialize.utils.GetTranslationsForInitialization;
import com.cs.core.interactor.usecase.initialize.utils.ValidationUtils;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.core.type.TypeReference;

@Component
public class InitializeContextsService implements IInitializeContextsService {
  
  @Autowired
  protected IGetOrCreateVariantContextsStrategy         getOrCreateVariantContextStrategy;
  
  @Autowired
  protected ICreateOrSavePropertiesTranslationsStrategy savePropertiesTranslationsStrategy;
  
  @Override
  public void execute() throws Exception
  {
    initailizeStandardVariantContext();
    initializeVariantContextTranslations();
  }
  
  private void initializeVariantContextTranslations() throws Exception
  {
    ISaveTranslationsRequestModel translationRequestDataModel = GetTranslationsForInitialization
        .getSaveTranslationsRequestModel(InitializeDataConstants.VARIANT_CONTEXTS_TRANSLATIONS_JSON,
            CommonConstants.CONTEXT);
    
    savePropertiesTranslationsStrategy.execute(translationRequestDataModel);
  }
  
  private void initailizeStandardVariantContext() throws Exception
  {
    List<ISaveVariantContextModel> variantContextList = getVariantContexts();
    
    IListModel<ISaveVariantContextModel> variantContexts = new ListModel<>();
    variantContexts.setList(variantContextList);
    getOrCreateVariantContextStrategy.execute(variantContexts);
  }
  
  private List<ISaveVariantContextModel> getVariantContexts() throws Exception
  {
    InputStream stream = this.getClass()
        .getClassLoader()
        .getResourceAsStream(InitializeDataConstants.VARIANT_CONTEXTS_JSON);
    List<ISaveVariantContextModel> variantContextList = ObjectMapperUtil.readValue(stream,
        new TypeReference<List<SaveVariantContextModel>>()
        {
        });
    stream.close();
    
    for (ISaveVariantContextModel variantContext : variantContextList) {
      ValidationUtils.validateVariantContext(variantContext);
    }
    
    return variantContextList;
  }
}
