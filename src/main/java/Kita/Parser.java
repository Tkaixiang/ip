package Kita;

import Kita.Exceptions.*;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    private final static Pattern toDoPattern = Pattern.compile("^todo (.+)$");
    private final static Pattern deadlinePattern = Pattern.compile("^deadline (.+) /by (.+)$");
    private final static Pattern eventPattern = Pattern.compile("^event (?<name>.+) (?:(?:/from (?<from>.+) /to (?<to>.+))|(?:/to (?<to2>.+) /from (?<from2>.+)))$");

    /**
     * Parses a given command and executes commands in the given Commands object based on the result
     *
     * @param command The String command entered
     * @param commandsExecutor The commandsExecutor to execute commands on
     * @returns boolean - Whether the "bye" command was entered
     */
    public static boolean parse(String command, Commands commandsExecutor) throws KitaError, IOException {
        if (command.equals("bye")) {
            commandsExecutor.bye();
            return true;
        } else if (command.equals("list")) {
           commandsExecutor.list();
        } else if (command.startsWith("mark")) {
            commandsExecutor.mark(command);
        } else if (command.startsWith("unmark")) {
            commandsExecutor.unmark(command);
        } else if (command.startsWith("delete")) {
            commandsExecutor.delete(command);
        } else {
            Matcher eventMatcher = eventPattern.matcher(command);
            Matcher deadlineMatcher = deadlinePattern.matcher(command);
            Matcher todoMatcher = toDoPattern.matcher(command);

            if (command.startsWith("event")) {
                if (!eventMatcher.matches()) {
                    if (!command.contains("/from")) {
                        throw new KitaMissingFrom();
                    } else if (!command.contains("/to")) {
                        throw new KitaMissingTo();
                    }
                    throw new KitaMissingDescription();
                }
                commandsExecutor.createEvent(eventMatcher);

            } else if (command.startsWith("deadline")) {
                if (!deadlineMatcher.matches()) {
                    if (!command.contains("/by")) {
                        throw new KitaMissingBy();
                    }
                    throw new KitaMissingDescription();
                }
                commandsExecutor.createDeadline(deadlineMatcher);

            } else if (command.startsWith("todo")) {
                if (!todoMatcher.matches()) {
                    throw new KitaMissingDescription();
                }

                commandsExecutor.createToDo(todoMatcher);
            } else {
                // No valid command found :c
                throw new KitaNotFound();
            }
        }
        return false;
    }
}
