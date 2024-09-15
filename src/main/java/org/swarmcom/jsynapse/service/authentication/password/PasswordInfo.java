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
package org.swarmcom.jsynapse.service.authentication.password;

import org.swarmcom.jsynapse.domain.Authentication.*;

public class PasswordInfo extends AuthenticationInfo {
    static final String PASSWORD_TYPE = "m.login.password";
    static final String USER = "user";
    static final String PASSWORD = "password";

    public PasswordInfo() {
        setType(PASSWORD_TYPE);
    }

    @Override
    public boolean validateKeys(AuthenticationSubmission authentication) {
        return authentication.containsKey(USER) && authentication.containsKey(PASSWORD);
    }
}
