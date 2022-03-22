package com.cs.core.runtime.staticcollection;

import org.springframework.stereotype.Component;

import com.cs.core.runtime.interactor.model.collections.IGetStaticCollectionModel;
import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstanceTreeStrategyModel;

@Component
public class GetStaticCollectionService extends AbstractGetStaticCollection<IGetKlassInstanceTreeStrategyModel, IGetStaticCollectionModel>
implements IGetStaticCollectionService {
 
}
