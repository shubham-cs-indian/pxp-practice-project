package com.cs.core.runtime.builder;

import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.RelationshipInstanceBuilder;

/**
 * factory class for all builder classes
 * @author Janak.Gurme
 *
 */
public class BuilderFactory {
  
  
  /**
   * factory method for relationshipInstanceBuilder
   * 
   * @param rdbmsComponentUtils
   * @return
   */
  public static IRelationshipInstanceBuilder newRelationshipInstanceBuilder(RDBMSComponentUtils rdbmsComponentUtils,
      IGetConfigDetailsForCustomTabModel configDetails)
  {
    return new RelationshipInstanceBuilder(rdbmsComponentUtils, configDetails);
  }
    
  
  
}
