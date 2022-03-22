package com.cs.ui.runtime.controller.usecase.login;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class HtmlRedirectController {
  
  @RequestMapping(value = "/", method = { RequestMethod.GET, RequestMethod.POST })
  public String redirect(HttpServletRequest servletRequest, HttpServletResponse servletResponse)
      throws Exception
  {
    // System.out.println("HtmlRedirectController.redirect()");
    // Boolean debugMode = Boolean.valueOf(servletRequest.getParameter("debugMode"));

    return "home/index.html";
  }
}
