package com.cs.config.strategy.plugin.usecase.language;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.attributiontaxonomy.ILanguage;
import com.cs.core.runtime.interactor.model.configuration.IIdAndTypeModel;
import com.cs.repository.language.LanguageRepository;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

public class GetAllAuditLogLabel extends AbstractOrientPlugin {
  
  public GetAllAuditLogLabel(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetAllAuditLogLabel/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<String> auditLogUICodeList = CommonConstants.AUDIT_LOG_UI_CODE_LIST;
    String uiLanguage = UtilClass.getLanguage().getUiLanguage();
    Map<String, Object> mapToReturn = new HashMap<>();
    mapToReturn.put(IIdAndTypeModel.ADDITIONAL_PROPERTIES, getUICodeVsLabelMap(auditLogUICodeList, uiLanguage));
    return mapToReturn;
  }
  
  private Map<String, Object> getUICodeVsLabelMap(List<String> auditLogUiCodeList,
      String uiLanguage) throws Exception
  {
    Map<String, Object> referenceLanguages = new HashMap<>();
      Iterable<Vertex> languageVertices = LanguageRepository.getUITranslationLabelByCode(auditLogUiCodeList);
      languageVertices.forEach(languageVertex -> {
        referenceLanguages.put(languageVertex.getProperty(ILanguage.CODE), languageVertex.getProperty(ILanguage.LABEL + "__" + uiLanguage).toString());
      });
    return referenceLanguages;
  }
}
