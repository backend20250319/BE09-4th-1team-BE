package com.playvoice.postservice.fegin;

import com.playvoice.postservice.fegin.dto.UserResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "PLAYVOICE-USER-SERVICE", configuration = FeignConfig.class)
public interface UserServiceClient {

    @GetMapping("/users/{userId}")
    UserResponseDTO getUserById(@PathVariable("userId") Long userId
    );
}
