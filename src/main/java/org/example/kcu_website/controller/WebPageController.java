package org.example.kcu_website.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebPageController {

  @GetMapping("/aboutkcu")
  public String aboutkcu() {
    return "aboutKcu";
  }

  @GetMapping("/developers")
  public String developers() {
    return "websiteDevelopers";
  }

  @GetMapping("/getinvolved")
  public String getinvolved() {
    return "getInvolved";
  }

  @GetMapping("/login")
  public String login() {
    return "login";
  }

}
