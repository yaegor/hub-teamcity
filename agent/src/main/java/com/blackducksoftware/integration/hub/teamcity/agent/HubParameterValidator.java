/**
 * Black Duck Hub Plug-In for TeamCity Agent
 *
 * Copyright (C) 2017 Black Duck Software, Inc.
 * http://www.blackducksoftware.com/
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.blackducksoftware.integration.hub.teamcity.agent;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang3.StringUtils;

import com.blackducksoftware.integration.hub.global.HubCredentials;

public class HubParameterValidator {
    private final HubAgentBuildLogger logger;

    public HubParameterValidator(final HubAgentBuildLogger logger) {
        this.logger = logger;
    }

    public boolean isServerUrlValid(final String url) {
        boolean validUrl = true;
        if (StringUtils.isBlank(url)) {
            logger.error("There is no Server URL specified");
            validUrl = false;
        } else {
            try {
                new URL(url);
            } catch (final MalformedURLException e) {
                logger.error("The server URL specified is not a valid URL.");
                validUrl = false;
            }
        }
        return validUrl;
    }

    public boolean isHubCredentialConfigured(final HubCredentials credential) {
        boolean validCredential = true;
        if (credential == null) {
            logger.error("There are no credentials configured.");
            validCredential = false;
        } else {
            if (StringUtils.isBlank(credential.getUsername())) {
                logger.error("There is no Hub username specified");
                validCredential = false;
            }
            if (StringUtils.isBlank(credential.getEncryptedPassword())) {
                logger.error("There is no Hub password specified.");
                validCredential = false;
            }
        }
        return validCredential;
    }

}
