package org.snudh.test.controler;

import org.snudh.ocs.MediBook;
import org.snudh.test.dto.PatientDto;
import org.snudh.test.queryexec.OcsQueryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/test/api/ocs")
public class OcsQuery {

  OcsQueryService ocsService;

  OcsQuery(OcsQueryService ocsService) {
    this.ocsService = ocsService;
  }

  @GetMapping("")
  String default_method() {
    ocsService.test_01();
    ocsService.test_02();
    ocsService.test_03();
    ocsService.test_04();
    return "OK";
  }

  @GetMapping("/T01")
  Optional<PatientDto> test_01() {
    return ocsService.test_04();
  }

  @GetMapping("/medi/dday/{dday}")
  //ResponseEntity<List<MediBook>> rsv_pats(@PathVariable Integer dday) {
  List<MediBook> rsv_pats(@PathVariable Integer dday) {
      return ocsService.test_15(dday);
  }

  @GetMapping("/exam/dday/{dday}")
  //ResponseEntity<List<MediBook>> rsv_pats(@PathVariable Integer dday) {
  List<MediBook> exam_pats(@PathVariable Integer dday) {
      return ocsService.test_16(dday);
  }
  
}
