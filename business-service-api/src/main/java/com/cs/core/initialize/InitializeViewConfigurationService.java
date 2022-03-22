package com.cs.core.initialize;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.constants.initialize.InitializeDataConstants;
import com.cs.core.config.interactor.model.viewconfiguration.IViewConfigurationModel;
import com.cs.core.config.interactor.model.viewconfiguration.ViewConfigurationModel;
import com.cs.core.config.strategy.usecase.viewconfiguration.IGetOrCreateViewConfigurationStrategy;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;

@Component
public class InitializeViewConfigurationService implements IInitializeViewConfigurationService {

	 @Autowired
	  protected IGetOrCreateViewConfigurationStrategy getOrCreateViewConfigurationStrategy;
	  
	  @Override
	  public void execute() throws Exception
	  {
	    InputStream stream = this.getClass()
	        .getClassLoader()
	        .getResourceAsStream(InitializeDataConstants.VIEW_CONFIGURATION);
	    IViewConfigurationModel dataModel = ObjectMapperUtil.readValue(stream,
	        ViewConfigurationModel.class);
	    stream.close();
	    getOrCreateViewConfigurationStrategy.execute(dataModel);
	  }
	}
