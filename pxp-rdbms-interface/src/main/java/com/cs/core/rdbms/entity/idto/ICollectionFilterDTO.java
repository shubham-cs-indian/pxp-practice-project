package com.cs.core.rdbms.entity.idto;

import java.io.Serializable;

public interface ICollectionFilterDTO extends Serializable {

  /**
   * Add a specific filter for collection.
   * @param isQuickList
   * @param collectionId
   */

  Boolean isQuickList();

  String  getCollectionId();

}
