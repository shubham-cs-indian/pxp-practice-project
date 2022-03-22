package com.cs.core.technical.icsexpress.scope;

import java.util.Collection;

import com.cs.core.technical.icsexpress.coupling.ICSECouplingSource;
import com.cs.core.technical.icsexpress.definition.ICSEObject;

/**
 * 
 * @author Janak.Gurme
 *
 */
public interface ICSEEntityByTranslationFilter {
  

    /**
     * @return the object to be filtered
     */
    ICSECouplingSource getObject();

    /**
     * @return the concerned languages
     */
    Collection<ICSEObject> getTranslations();
  
}
