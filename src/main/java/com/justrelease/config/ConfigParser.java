package com.justrelease.config;

import com.justrelease.config.build.ExecConfig;
import com.justrelease.config.build.VersionUpdateConfig;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by bilal on 26/07/15.
 */
public class ConfigParser {
    ReleaseConfig releaseConfig;
    String configLocation;
    InputStream in;

    public ConfigParser(String configLocation) {
        this.configLocation = configLocation;
    }

    void loadFromWorkingDirectory() {

        try {
            in = new URL(configLocation).openStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void parse(ReleaseConfig releaseConfig) throws Exception {
        this.releaseConfig = releaseConfig;
        loadFromWorkingDirectory();
        if (in != null) parseAndBuildConfig();
    }

    private void parseAndBuildConfig() throws Exception {
        Yaml yaml = new Yaml();
        Map map = (Map) yaml.load(in);
        handleConfig(map);
    }

    private void handleConfig(Map root) {
        handleBuild(root);
        handleVersionUpdate(root);
        handleTaggingRepos(root);

    }

    private void handleTaggingRepos(Map root) {
        GithubRepo mainRepo = releaseConfig.getMainRepo();
        if (root.get("publish") == null) return;
        ArrayList<LinkedHashMap> arrayList = ((ArrayList) root.get("publish"));
        for (LinkedHashMap entry : arrayList) {
            String key = (String) entry.keySet().iterator().next();
            if ("npm".equals(key)) {
                // TODO - add npm publish support
            } else if ("github".equals(key)) {
                ArrayList<String> commands = (ArrayList<String>)entry.get(key);
                for (String command : commands) {
                    if (command.startsWith("description"))
                        mainRepo.setDescriptionFileName(command.split("=")[1]);
                    if (command.startsWith("attachment"))
                        mainRepo.setAttachmentFile(command.split("=")[1]);
                }

            }
        }
    }

    private void handleVersionUpdate(Map root) {
        GithubRepo mainRepo = releaseConfig.getMainRepo();
        ArrayList<String> versionUpdates = (ArrayList) root.get("version.update");
        for (String regex : versionUpdates) {
            VersionUpdateConfig versionUpdateConfig = new VersionUpdateConfig(regex, mainRepo);
            releaseConfig.addVersionUpdateConfig(versionUpdateConfig);
        }
    }


    private void handleBuild(Map root) {
        String directory = "";
        GithubRepo mainRepo = releaseConfig.getMainRepo();
        ArrayList<String> commands = (ArrayList) root.get("create.artifacts");
        for (String command : commands) {
            ExecConfig execConfig = new ExecConfig(directory, command, mainRepo);
            releaseConfig.addExecConfig(execConfig);
        }
    }
}
