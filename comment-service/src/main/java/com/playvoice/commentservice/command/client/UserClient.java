package com.playvoice.commentservice.command.client;

import com.playvoice.commentservice.command.dto.AuthorDTO;
import com.playvoice.commentservice.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// gateway 통해서 접근
@FeignClient(name = "user-service", url = "http://localhost:8000/api/v1/user-service", configuration = FeignClientConfig.class)
public interface UserClient {

    @GetMapping("/users/{userId}")
    AuthorDTO getAuthor(@PathVariable("userId") Long userId);
}
