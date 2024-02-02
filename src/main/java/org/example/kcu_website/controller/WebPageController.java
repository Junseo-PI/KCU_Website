package org.example.kcu_website.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebPageController {

  @GetMapping("/")
  public String index() {
    return "index";
  }

  @GetMapping("/aboutkcu")
  public String aboutkcu() {
    return "aboutKcu";
  }

  @GetMapping("/leaders")
  public String leaders() {
    return "leaders";
  }

  @GetMapping("/developers")
  public String developers() {
    return "websiteDevelopers";
  }

  @GetMapping("/getinvolved")
  public String getinvolved() {
    return "getInvolved";
  }

  @GetMapping("/projects")
  public String projects() {
    return "projects";
  }

}
