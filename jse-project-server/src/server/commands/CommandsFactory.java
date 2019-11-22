package server.commands;
import java.util.HashMap;
import java.util.Map;

public class CommandsFactory {
    private static final CommandsFactory instance = new CommandsFactory();
    private Map<String, Command> allSeverCommands = new HashMap<>();

    private CommandsFactory() {
        allSeverCommands.put("GiveData", new GiveDataCommand());
        allSeverCommands.put("GetData", new GetDataCommand());
        allSeverCommands.put("GetUser", new GetUserCommand());
        allSeverCommands.put("GetUserData", new GetUserDataCommand());
        allSeverCommands.put("GiveUsersBase", new GiveUsersBaseCommand());
        allSeverCommands.put("GiveUserData", new GiveUserDataCommand());
        allSeverCommands.put("ResetDataBase", new ResetDataBaseCommand());
    }

    public static CommandsFactory getFactory() {
        return instance;
    }

    public Command getCommand(String commandName) {
        if (commandName == null || commandName.isEmpty()) {
            throw new IllegalArgumentException("commandName can not be empty!");
        }

        Command command = allSeverCommands.get(commandName);
        if (command == null) {
            throw new IllegalStateException("command with name - " + commandName + " - NOT FOUND!");
        }

        return command;
    }
}
