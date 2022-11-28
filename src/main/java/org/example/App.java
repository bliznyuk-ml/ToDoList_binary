package org.example;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class App {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        Map<String, Command> cmd = new HashMap<>();
        Map<String, User> userMap = new HashMap<>();
        final String[] login = new String[1];
        final String[] password = new String[1];

        //Зчитування з файлу даних про користувачів та заповнення userMap
        try (
                InputStream in = new FileInputStream("users.txt");
        ) {
            int size = in.available();
            byte[] buffer = new byte[size];
            int count = in.read(buffer);
            if (count == size && count != 0) {
                String users = new String(buffer, 1, count - 2);
                String[] u = users.split(",\s");
                for (String s : u) {
                    String[] user = new String[2];
                    user = s.split(":");
                    userMap.put(user[0], new User(user[0], user[1]));
                }
            }
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
                if (!userMap.containsKey(login[0])) {
                    userMap.put(login[0], new User(login[0], password[0]));
                    System.out.println("Account is added");
                    try {
                        Files.createFile(Path.of(login[0] + ".txt"));
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
                if (!userMap.containsKey(login[0])) {
                    System.err.println("User with login " + login[0] + " is absent");
                } else if (userMap.get(login[0]).getPassword().equals(password[0])) {

                    //Зчитування тасків користувача із файла
                    String fileName = login[0] + ".txt";
                    try (
                            InputStream in = new FileInputStream(fileName);
                    ) {
                        int size = in.available();
                        byte[] buffer = new byte[size];
                        int count = in.read(buffer);
                        if (count == size && count != 0) {
                            String tasks = new String(buffer, 0, count - 2);
                            String[] u = tasks.split("\n");
                            for (String s : u) {
                                String[] user = new String[6];
                                user = s.split(":");
                                userMap.get(login[0]).readTask(user[0], user[1], user[2], user[3], user[4], user[5]);
                            }
                        }
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

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

                            //String fileName = login[0] + ".txt";
                            try (
                                    OutputStream out = new FileOutputStream(fileName);
                            ) {
                                //out.write((login[0] + "^" + password[0] + "\n").getBytes());
                                out.write((userMap.get(login[0]).writeTask()).getBytes());
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                        } else if (cmd.get(choose) == null) {
                            continue;
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
                System.out.println(userMap);
            }
        };

        //Додавання задачі
        Command cmd4 = new Command() {
            @Override
            void command() {
                userMap.get(login[0]).addTask();
            }
        };

        //Позначка о виконанні задачі
        Command cmd5 = new Command() {
            @Override
            void command() {
                System.out.println("Enter the name of the task you want to mark as completed ");
                String name = scanner.next();
                userMap.get(login[0]).taskCompleted(name);
            }
        };

        //Видалення задачі
        Command cmd6 = new Command() {
            @Override
            void command() {
                System.out.println("Enter the name of the task to be deleted: ");
                String name = scanner.next();
                userMap.get(login[0]).deleteTask(name);
            }
        };

        //Показати невиконані задачі (відсортовані по важливості і даті)
        Command cmd7 = new Command() {
            @Override
            void command() {
                userMap.get(login[0]).sortTasksByImportanceDate();
            }
        };

        //Показати виконані задачі (відсортовані по даті в зворотньому порядку)
        Command cmd8 = new Command() {
            @Override
            void command() {
                userMap.get(login[0]).sortCompletedTasksByData();
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
                        OutputStream out = new FileOutputStream("users.txt");
                ) {
                    out.write(userMap.values().toString().getBytes());
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
