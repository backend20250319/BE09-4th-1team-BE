package com.playvoice.commentservice.command.client;

import com.playvoice.commentservice.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;

// gateway 통해서 접근
@FeignClient(name = "post-service", url = "http://localhost:8000/api/v1/post-service", configuration = FeignClientConfig.class)
public interface PostClient {

//    @PostMapping("/payments/process")
//    PaymentDTO processPayment(@RequestBody PaymentRequest request);
//
//    @PostMapping("/payments/cancel")
//    PaymentDTO cancelPayment(@RequestBody PaymentCancelRequest request);
}
