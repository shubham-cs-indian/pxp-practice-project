package com.cs.core.runtime.interactor.constants.application;

import com.cs.core.runtime.interactor.entity.klassinstance.IContentInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstance;
import com.cs.core.runtime.interactor.entity.language.ILanguageKlassInstance;

public class FieldsToFetch {
  
  public static class KlassInstance {
    
    public static final String[] FIELDS_TO_FETCH_FOR_KLASS_INSTNACE = new String[] {
        IKlassInstance.ID, IKlassInstance.ATTRIBUTES, IKlassInstance.TAGS,
        IContentInstance.LANGUAGE_CODES };
  }
  
  public static class LanguageInstance {
    
    public static final String[] FIELDS_TO_FETCH_FOR_LANGUAGE_INSTNACE = new String[] {
        ILanguageKlassInstance.ID, ILanguageKlassInstance.DEPENDENT_ATTRIBUTES,
        ILanguageKlassInstance.LANGUAGE };
  }
  
  public static class AttributeInstance {
  }
  
  public static class KPIInstance {
  }
  
  public static class GoldenRecordInstance {
  }
}
