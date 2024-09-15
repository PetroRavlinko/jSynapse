/*
 * (C) Copyright 2015 eZuce Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
*/
package org.swarmcom.jsynapse.controller.client.api.v1;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import org.swarmcom.jsynapse.controller.JsynapseApi;
import org.swarmcom.jsynapse.domain.Authentication.AuthenticationFlows;
import org.swarmcom.jsynapse.domain.Authentication.AuthenticationResult;
import org.swarmcom.jsynapse.domain.Authentication.AuthenticationSubmission;
import org.swarmcom.jsynapse.service.authentication.AuthenticationService;

import static org.swarmcom.jsynapse.controller.JsynapseApi.CLIENT_V1_API;

@RestController
@RequestMapping(value = CLIENT_V1_API)
public class AuthenticationRestApi extends JsynapseApi {

    private final AuthenticationService authenticationService;

    @Inject
    public AuthenticationRestApi(final AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping("/login")
    public AuthenticationFlows getLoginSchemas() {
        return authenticationService.getSupportedFlows();
    }

    @PostMapping("/login")
    public AuthenticationResult login(@RequestBody final AuthenticationSubmission login, HttpServletRequest request) {
        login.setRemoteAddr(request.getRemoteAddr());
        return authenticationService.login(login);
    }

    @GetMapping("/register")
    public AuthenticationFlows getRegistrationSchemas() {
        return authenticationService.getSupportedFlows();
    }

    @PostMapping("/register")
    public AuthenticationResult register(@RequestBody final AuthenticationSubmission registration, HttpServletRequest request) {
        registration.setRemoteAddr(request.getRemoteAddr());
        return authenticationService.register(registration);
    }
}