package com.codingame.gameengine.runner;

import com.google.common.io.Files;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

import com.codingame.gameengine.runner.dto.GameResultDto;

public class CommandLineInterface {

    public static void main(String[] args) {
        try {
            Options options = new Options();

            // Define required options
            options.addOption("p1", true, "Required. Player 1 command line.")
                    .addOption("p1name", true, "Player 1 name. Default: Player 1 command line")
                    .addOption("p2", true, "Required. Player 2 command line.")
                    .addOption("p2name", true, "Player 2 name. Default: Player 2 command line")
                    .addOption("lvl", true, "League level. Default: 3")
                    .addOption("s", false, "Start server mode")
                    .addOption("l", true, "File to save game log")
                    .addOption("d", true, "Referee initial data")
                    .addOption("r", true, "Start server mode to replay a game log");

            CommandLine cmd = new DefaultParser().parse(options, args);

            if (cmd.hasOption("r")) {
                String json_result = new String(java.nio.file.Files.readAllBytes(Paths.get(cmd.getOptionValue("r"))));
                new Renderer(8888).render(2, json_result);
            } else {
                // Launch Game
                MultiplayerGameRunner gameRunner = new MultiplayerGameRunner();

                // Choose league level
                Integer leagueLevel = Integer.parseInt(cmd.getOptionValue("lvl", "3"));
                gameRunner.setLeagueLevel(leagueLevel);

                if (cmd.hasOption("d")) {
                    String[] parse = cmd.getOptionValue("d").split("=", 0);
                    Long seed = Long.parseLong(parse[1]);
                    gameRunner.setSeed(seed);
                } else {
                    gameRunner.setSeed(System.currentTimeMillis());
                }

                GameResultDto result = gameRunner.gameResult;

                int playerCount = 0;

                for (int i = 1; i <= 3; ++i) {
                    String playerName = cmd.getOptionValue("p" + i);
                    if (cmd.hasOption("p" + i + "name")) {
                        playerName = cmd.getOptionValue("p" + i + "name");
                    }

                    if (cmd.hasOption("p" + i)) {
                        gameRunner.addAgent(cmd.getOptionValue("p" + i), playerName);
                        playerCount += 1;
                    }
                }

                if (cmd.hasOption("s")) {
                    gameRunner.start();
                } else {
                    Method initialize = GameRunner.class.getDeclaredMethod("initialize",
                            Properties.class);
                    initialize.setAccessible(true);
                    initialize.invoke(gameRunner, new Properties());

                    Method runAgents = GameRunner.class.getDeclaredMethod("runAgents");
                    runAgents.setAccessible(true);
                    runAgents.invoke(gameRunner);

                    if (cmd.hasOption("l")) {
                        Method getJSONResult = GameRunner.class.getDeclaredMethod("getJSONResult");
                        getJSONResult.setAccessible(true);

                        Files.asCharSink(Paths.get(cmd.getOptionValue("l")).toFile(), Charset.defaultCharset())
                                .write((String) getJSONResult.invoke(gameRunner));
                    }

                    for (int i = 0; i < playerCount; ++i) {
                        System.out.println(result.scores.get(i));
                    }

                    for (String line : result.uinput) {
                        System.out.println(line);
                    }
                }

                // We have to clean players process properly
                Field getPlayers = GameRunner.class.getDeclaredField("players");
                getPlayers.setAccessible(true);
                @SuppressWarnings("unchecked")
                List<Agent> players = (List<Agent>) getPlayers.get(gameRunner);

                if (players != null) {
                    for (Agent player : players) {
                        Field getProcess = CommandLinePlayerAgent.class.getDeclaredField("process");
                        getProcess.setAccessible(true);
                        Process process = (Process) getProcess.get(player);

                        process.destroy();
                    }
                }
            }
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace(System.err);
            System.exit(1);
        }
    }
}
