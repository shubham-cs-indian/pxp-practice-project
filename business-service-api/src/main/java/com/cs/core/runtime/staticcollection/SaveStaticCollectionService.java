package com.cs.core.runtime.staticcollection;

import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.collections.IGetStaticCollectionModel;
import com.cs.core.runtime.interactor.model.collections.ISaveStaticCollectionModel;

@Service
public class SaveStaticCollectionService extends AbstractSaveStaticCollection<ISaveStaticCollectionModel, IGetStaticCollectionModel>
    implements ISaveStaticCollectionService {
  
}
