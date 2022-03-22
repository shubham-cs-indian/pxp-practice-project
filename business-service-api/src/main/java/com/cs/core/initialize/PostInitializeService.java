package com.cs.core.initialize;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class PostInitializeService implements IPostInitializeService {
  
  @Autowired
  protected ApplicationContext context;
  
  @Override
  public void execute() throws Exception
  {
    Map<String, ? extends IPostInitializeProjectService> initializers = context
        .getBeansOfType(IPostInitializeProjectService.class);
    for (IPostInitializeProjectService initializer : initializers.values()) {
      initializer.execute();
    }
  }
}
