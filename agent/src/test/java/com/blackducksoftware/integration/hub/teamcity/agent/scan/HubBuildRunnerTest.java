/*******************************************************************************
 * Copyright (C) 2016 Black Duck Software, Inc.
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
 *******************************************************************************/
package com.blackducksoftware.integration.hub.teamcity.agent.scan;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.blackducksoftware.integration.hub.teamcity.agent.util.TestAgentRunningBuild;
import com.blackducksoftware.integration.hub.teamcity.agent.util.TestArtifactsWatcher;
import com.blackducksoftware.integration.hub.teamcity.agent.util.TestBuildRunnerContext;
import com.blackducksoftware.integration.hub.teamcity.common.HubBundle;

import jetbrains.buildServer.agent.AgentBuildRunnerInfo;

public class HubBuildRunnerTest {
	@Test
	public void testConstructor() {
		assertNotNull(new HubBuildRunner(new TestArtifactsWatcher()));
	}

	@Test
	public void testCreateBuildProcess() throws Exception {
		final HubBuildRunner runner = new HubBuildRunner(new TestArtifactsWatcher());
		assertNotNull(runner.createBuildProcess(new TestAgentRunningBuild(), new TestBuildRunnerContext()));
	}

	@Test
	public void testGetRunnerInfo() {
		final HubBuildRunner runner = new HubBuildRunner(new TestArtifactsWatcher());
		final AgentBuildRunnerInfo runnerInfo = runner.getRunnerInfo();

		assertTrue(runnerInfo.canRun(null));

		assertEquals(HubBundle.RUNNER_TYPE, runnerInfo.getType());
	}

}
