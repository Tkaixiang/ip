package kita;

import java.util.Scanner;

/**
 * Main Kita chatbot class
 */
public class Kita {

    public static void main(String[] args) {
        TaskList tasks;
        Scanner getInput = new Scanner(System.in);
        Storage saveSystem;

        try {
            saveSystem = new Storage();
            tasks = new TaskList(saveSystem.readTasksFromFile());
        } catch (Exception e) {
            System.out.println("Oh no, Kita failed to create/read from the save file successfully :c");
            System.out.println(e);
            return;
        }

        Commands commandsExecutor = new Commands(tasks, saveSystem);
        commandsExecutor.hello();

        while (true) {
            try {
                String command = getInput.nextLine();
                commandsExecutor.printLine();
                boolean shouldEnd = Parser.parse(command, commandsExecutor);
                if (shouldEnd) {
                    break;
                }
            } catch (Exception e) {
                System.out.println(e);
            }
            commandsExecutor.printLine();
        }
    }
}
