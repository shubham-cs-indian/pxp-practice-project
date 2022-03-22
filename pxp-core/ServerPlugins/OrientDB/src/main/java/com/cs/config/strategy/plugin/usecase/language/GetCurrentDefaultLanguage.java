package com.cs.config.strategy.plugin.usecase.language;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.entity.attributiontaxonomy.ILanguage;
import com.cs.core.config.interactor.exception.language.MultipleDefaultLanguageFoundException;
import com.cs.core.runtime.interactor.exception.language.LanguageNotFoundException;
import com.cs.repository.language.LanguageRepository;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GetCurrentDefaultLanguage extends AbstractOrientPlugin {
  
  public GetCurrentDefaultLanguage(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetCurrentDefaultLanguage/*" };
  }
  
  private static final List<String> FIELDS_TO_FETCH = Arrays.asList(ILanguage.LABEL, ILanguage.ID,
      ILanguage.CODE, ILanguage.ICON);
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    
    Map<String, Object> languageMap = new HashMap<>();
    
    Iterable<Vertex> language = LanguageRepository.getCurrentDefaultLanguage();
    Iterator<Vertex> languageIterator = language.iterator();
    
    if (!languageIterator.hasNext()) {
      throw new LanguageNotFoundException();
    }
    
    languageMap = UtilClass.getMapFromVertex(FIELDS_TO_FETCH, languageIterator.next());
    
    if (languageIterator.hasNext()) {
      throw new MultipleDefaultLanguageFoundException();
    }
    return languageMap;
  }
}
