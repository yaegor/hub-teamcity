package com.blackducksoftware.integration.hub.teamcity.agent.scan;

import jetbrains.buildServer.agent.AgentRunningBuild;
import jetbrains.buildServer.agent.BuildFinishedStatus;
import jetbrains.buildServer.agent.BuildProgressLogger;
import jetbrains.buildServer.agent.BuildRunnerContext;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import com.blackducksoftware.integration.hub.teamcity.agent.HubAgentBuildLogger;
import com.blackducksoftware.integration.hub.teamcity.common.HubConstantValues;
import com.blackducksoftware.integration.hub.teamcity.common.beans.HubCredentialsBean;
import com.blackducksoftware.integration.hub.teamcity.common.beans.HubProxyInfo;

public class HubBuildProcess extends HubCallableBuildProcess {

    @NotNull
    private final AgentRunningBuild build;

    @NotNull
    private final BuildRunnerContext context;

    private HubAgentBuildLogger logger;

    public HubBuildProcess(@NotNull final AgentRunningBuild build, @NotNull final BuildRunnerContext context) {
        this.build = build;
        this.context = context;
    }

    public void setProtexLogger(HubAgentBuildLogger logger) {
        this.logger = logger;
    }

    @Override
    public BuildFinishedStatus call() throws Exception {
        final BuildProgressLogger buildLogger = build.getBuildLogger();
        setProtexLogger(new HubAgentBuildLogger(buildLogger));

        BuildFinishedStatus result = BuildFinishedStatus.FINISHED_SUCCESS;

        logger.targetStarted("Hub Build Step");

        String serverUrl = getParameter(HubConstantValues.HUB_URL);

        HubCredentialsBean credentials = new HubCredentialsBean(getParameter(HubConstantValues.HUB_USERNAME), getParameter(HubConstantValues.HUB_PASSWORD));

        HubProxyInfo proxyInfo = new HubProxyInfo();
        proxyInfo.setHost(getParameter(HubConstantValues.HUB_PROXY_HOST));
        if (getParameter(HubConstantValues.HUB_PROXY_PORT) != null) {
            proxyInfo.setPort(Integer.valueOf(getParameter(HubConstantValues.HUB_PROXY_PORT)));
        }
        proxyInfo.setIgnoredProxyHosts(getParameter(HubConstantValues.HUB_NO_PROXY_HOSTS));
        proxyInfo.setProxyUsername(getParameter(HubConstantValues.HUB_PROXY_USER));
        proxyInfo.setProxyPassword(getParameter(HubConstantValues.HUB_PROXY_PASS));

        String projectName = getParameter(HubConstantValues.HUB_PROJECT_NAME);
        String version = getParameter(HubConstantValues.HUB_PROJECT_VERSION);

        String phase = getParameter(HubConstantValues.HUB_VERSION_PHASE);
        String distribution = getParameter(HubConstantValues.HUB_VERSION_DISTRIBUTION);

        String hubCLIPath = getParameter(HubConstantValues.HUB_CLI_PATH);
        String hubScanMemory = getParameter(HubConstantValues.HUB_SCAN_MEMORY);
        String hubScanTargets = getParameter(HubConstantValues.HUB_SCAN_TARGETS);

        logger.info("Hub Server Url : " + serverUrl);
        logger.info("Hub User : " + credentials.getHubUser());

        logger.info("Project : " + projectName);
        logger.info("Version : " + version);

        logger.info("Version Phase : " + phase);
        logger.info("Version Distribution : " + distribution);
        logger.info("CLI Path : " + hubCLIPath);
        logger.info("Hub scan memory : " + hubScanMemory + " MB");
        logger.info("Hub scan targets : " + hubScanTargets);

        String[] scanTargets = hubScanTargets.split(System.getProperty("line.separator"));
        int i = 0;
        for (String target : scanTargets) {
            i++;
            logger.info("Target #" + i + " : " + target);
        }

        if (StringUtils.isNotBlank(proxyInfo.getHost())) {
            logger.info("Proxy Host : " + proxyInfo.getHost());
        }
        if (proxyInfo.getPort() != null) {
            logger.info("Proxy Port : " + proxyInfo.getPort());
        }
        if (StringUtils.isNotBlank(proxyInfo.getIgnoredProxyHosts())) {
            logger.info("No Proxy Hosts : " + proxyInfo.getIgnoredProxyHosts());
        }
        if (StringUtils.isNotBlank(proxyInfo.getProxyUsername())) {
            logger.info("Proxy Username : " + proxyInfo.getProxyUsername());
        }

        logger.targetFinished("Hub Build Step");
        return result;
    }

    private String getParameter(@NotNull final String parameterName) {
        final String value = context.getRunnerParameters().get(parameterName);
        if (value == null || value.trim().length() == 0) {
            return null;
        }
        String result = value.trim();
        return result;
    }

}