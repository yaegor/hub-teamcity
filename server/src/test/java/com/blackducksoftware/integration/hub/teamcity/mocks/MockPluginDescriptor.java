/**
 * Black Duck Hub Plug-In for TeamCity Server
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
package com.blackducksoftware.integration.hub.teamcity.mocks;

import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import jetbrains.buildServer.web.openapi.PluginDescriptor;

public class MockPluginDescriptor {
    public static PluginDescriptor getMockedPluginDescriptor() {
        final PluginDescriptor mockedPluginDescriptor = Mockito.mock(PluginDescriptor.class);

        Mockito.doAnswer(new Answer<String>() {
            @Override
            public String answer(final InvocationOnMock invocation) {
                final Object[] args = invocation.getArguments();

                return (String) args[0];
            }
        }).when(mockedPluginDescriptor).getPluginResourcesPath(Mockito.anyString());

        return mockedPluginDescriptor;
    }

}
