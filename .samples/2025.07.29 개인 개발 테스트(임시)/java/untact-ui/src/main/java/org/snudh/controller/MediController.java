package org.snudh.controller;

import org.snudh.domain.PatInfoVO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/service/medi")
@RequiredArgsConstructor
public class MediController {
  @RequestMapping("/reservation")
  public String reservation(
    HttpServletRequest request,
    Model model
  ) {
    HttpSession session = request.getSession(false);
    PatInfoVO pi = null;
    if (session != null) pi = (PatInfoVO)session.getAttribute("PATINFO");

    if (pi == null) return "reservation";

    String meddept = pi.getDeptnm();
    if (meddept.startsWith("원스톱협진센터")) meddept = "원스톱협진센터";

    model.addAttribute("patname", pi.getPatname());
    model.addAttribute("patno", pi.getPatno());
    model.addAttribute("meddate", pi.getMeddate());
    model.addAttribute("meddept", meddept);
    model.addAttribute("meddrnm", pi.getMeddrnm());
    return "reservation";
  }
}
