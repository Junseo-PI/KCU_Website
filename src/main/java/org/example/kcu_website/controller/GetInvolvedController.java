package org.example.kcu_website.controller;

import org.example.kcu_website.entity.GetInvolvedService;
import org.example.kcu_website.model.GetInvolved;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
public class GetInvolvedController {
    @Autowired
    private GetInvolvedService getInvolvedService;

    @GetMapping("/getinvolved")
    public String getInvolved(Model model) {
        Optional<GetInvolved> getInvolvedPeriod = getInvolvedService.findGetInvolvedPeriod();

        if (getInvolvedPeriod.isPresent()) {
            GetInvolved getInvolved = getInvolvedPeriod.get();
            boolean isWithinPeriod = getInvolvedService.isWithinGetInvolvedPeriod(getInvolved.getStartDate(), getInvolved.getEndDate());
            model.addAttribute("isWithinPeriod", isWithinPeriod);
            model.addAttribute("getInvolved", getInvolved);
        } else {
            model.addAttribute("isWithinPeriod", false);
        }

        return "getInvolved";
    }
}
