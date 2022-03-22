package com.cs.core.runtime.staticcollection;

import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.collections.ICreateStaticCollectionModel;
import com.cs.core.runtime.interactor.model.collections.IStaticCollectionModel;

@Service
public class CreateStaticCollectionService extends AbstractCreateStaticCollection<ICreateStaticCollectionModel, IStaticCollectionModel> 
  implements ICreateStaticCollectionService {
  
}