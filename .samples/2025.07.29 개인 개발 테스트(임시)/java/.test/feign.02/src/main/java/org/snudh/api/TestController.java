package org.snudh.api;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
public class TestController {
  private final DiscoveryClient discoveryClient;
  private final RestClient restClient;

  public TestController(DiscoveryClient discoveryClient, RestClient.Builder restClientBuilder) {
		this.discoveryClient = discoveryClient;
		restClient = restClientBuilder.build();
	}

  @GetMapping("/send")
  public ResponseEntity<String> getSned() {
    ServiceInstance serviceInstance = discoveryClient.getInstances("ocs").get(0);

    String serviceAResponse = restClient.get()
    .uri(serviceInstance.getUri() + "/api/v1/rsvdate/medis?dday=4")
    .retrieve()
    .body(String.class);
    return new ResponseEntity<>(serviceAResponse, HttpStatus.OK);
    //return ResponseEntity.ok().build();
  }
}
