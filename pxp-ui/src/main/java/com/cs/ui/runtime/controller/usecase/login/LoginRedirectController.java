package com.cs.ui.runtime.controller.usecase.login;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginRedirectController {
  
  @RequestMapping(value = "/getlogin")
  public String redirect() throws Exception
  {
    System.out.println("LoginRedirectController.redirect()");

    return "login/build/index.html";
  }
}
