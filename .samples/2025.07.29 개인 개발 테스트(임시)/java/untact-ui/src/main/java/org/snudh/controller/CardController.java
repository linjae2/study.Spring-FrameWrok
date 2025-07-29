package org.snudh.controller;

import org.snudh.domain.PatInfoVO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/service")
public class CardController {
  @RequestMapping("/card")
  public String card(
    HttpServletRequest request,
    Model model
  ) {
    HttpSession session = request.getSession(false);
    PatInfoVO pi = null;
    if (session != null) pi = (PatInfoVO)session.getAttribute("PATINFO");

    if (pi != null) {
      String txtno = pi.getPatno();
      txtno = txtno.substring(0, 2) + ' ' + txtno.substring(2, 4) + ' ' + txtno.substring(4);
      model.addAttribute("txtno", txtno);
      model.addAttribute("patname", pi.getPatname());
      model.addAttribute("patno", pi.getPatno());
    }

    return "card";
  }
}
