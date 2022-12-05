package org.example;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class App {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        Map<String, Command> cmd = new HashMap<>();
        Map<String, User> userMap = new HashMap<>();

        List<User> users = new ArrayList<>();

        final String[] login = new String[1];
        final String[] password = new String[1];


        //Зчитування з файлу даних про користувачів та заповнення userMap
       try(
               InputStream in = new FileInputStream("users.bin");
               DataInputStream dataIn = new DataInputStream(in)
       ){
           while (dataIn.available() > 0){
               users.add(new User(dataIn.readUTF(), dataIn.readUTF()));
           }
       } catch (FileNotFoundException e) {
           throw new RuntimeException(e);
       } catch (IOException e) {
           throw new RuntimeException(e);
       }

        //Команда реєстрації
        Command cmd1 = new Command() {
            @Override
            void command() {
                System.out.println("Enter login:");
                login[0] = scanner.next();
                System.out.println("Enter password:");
                password[0] = scanner.next();
                if (users.stream().noneMatch(u -> u.getLogin().equals(login[0]))) {
                    users.add(new User(login[0], password[0]));
                    System.out.println("Account is added");
                    try {
                        Files.createFile(Path.of(login[0] + ".bin"));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    System.err.println("This login exists");
                }
            }
        };

        //Команда входу
        Command cmd2 = new Command() {
            @Override
            void command() {
                System.out.println("Enter login:");
                login[0] = scanner.next();
                System.out.println("Enter password:");
                password[0] = scanner.next();
                Boolean exit = true;
                if (users.stream().noneMatch(u -> u.getLogin().equals(login[0]))) {
                    System.err.println("User with login " + login[0] + " is absent");
                } else if (users.stream().anyMatch(u -> u.getLogin().equals(login[0]) && u.getPassword().equals(password[0]))) {

                    //Зчитування тасків користувача із файла
                    String fileName = login[0] + ".bin";
                    users.stream().filter(u -> u.getLogin().equals(login[0])).findAny().get().readBinTask(fileName);

                    while (exit) {
                        System.out.println("Select function: \n" +
                                "a. Add task \n" +
                                "b. Mark task completed \n" +
                                "c. Delete task \n" +
                                "d. Show outstanding tasks \n" +
                                "e. Show completed tasks \n" +
                                "f. Exit");
                        String choose = scanner.next();
                        if (choose.equals("f")) {
                            exit = false;
                            users.stream().filter(u -> u.getLogin().equals(login[0])).findAny().get().writeBinTask(fileName);
                        } else {
                            cmd.get(choose).command();
                        }
                    }
                } else {
                    System.err.println("Wrong password");
                }
            }
        };

        //Друк всіх користувачів (прихована функція для перевірки роботи додатку)
        Command cmd3 = new Command() {
            @Override
            void command() {
                System.out.println(users);
            }
        };

        //Додавання задачі
        Command cmd4 = new Command() {
            @Override
            void command() {
                users.stream().filter(u -> u.getLogin().equals(login[0])).findAny().get().addTask();
            }
        };

        //Позначка о виконанні задачі
        Command cmd5 = new Command() {
            @Override
            void command() {
                System.out.println("Enter the name of the task you want to mark as completed ");
                String name = scanner.next();
                users.stream().filter(u -> u.getLogin().equals(login[0])).findAny().get().taskCompleted(name);
            }
        };

        //Видалення задачі
        Command cmd6 = new Command() {
            @Override
            void command() {
                System.out.println("Enter the name of the task to be deleted: ");
                String name = scanner.next();
                users.stream().filter(u -> u.getLogin().equals(login[0])).findAny().get().deleteTask(name);
            }
        };

        //Показати невиконані задачі (відсортовані по важливості і даті)
        Command cmd7 = new Command() {
            @Override
            void command() {
                users.stream().filter(u -> u.getLogin().equals(login[0])).findAny().get().sortTasksByImportanceDate();
            }
        };

        //Показати виконані задачі (відсортовані по даті в зворотньому порядку)
        Command cmd8 = new Command() {
            @Override
            void command() {
                users.stream().filter(u -> u.getLogin().equals(login[0])).findAny().get().sortCompletedTasksByData();
            }
        };

        cmd.put("1", cmd1);
        cmd.put("2", cmd2);
        cmd.put("5", cmd3);
        cmd.put("a", cmd4);
        cmd.put("b", cmd5);
        cmd.put("c", cmd6);
        cmd.put("d", cmd7);
        cmd.put("e", cmd8);

        boolean stop = true;
        while (stop) {
            System.out.println("Choose command: \n 1. Registration \n 2. Log in \n 3. Exit");
            String choose = scanner.next();
            if (choose.equals("3")) {

                try (
                        OutputStream out = new FileOutputStream("users.bin");
                        DataOutputStream dataOut = new DataOutputStream(out)
                ) {
                    for (User u : users){
                        dataOut.writeUTF(u.getLogin());
                        dataOut.writeUTF(u.getPassword());
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                stop = false;
            } else if (cmd.get(choose) == null) {
                continue;
            } else {
                cmd.get(choose).command();
            }
        }
    }
}
