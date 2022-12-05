package org.example;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class User {
    private String login;
    private String password;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    List<Task> tasks = new ArrayList<>();

    Scanner scanner = new Scanner(System.in);

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public static void addUser() {

    }

    public void addTask() {
        System.out.println("Enter name");
        String name = scanner.nextLine();
        System.out.println("Enter date if format: dd-mm-yyyy");
        String date = scanner.nextLine();
        System.out.println("Enter Importance: 1.Very important; 2.Most important; 3.Important; 4.Little important; 5.No matter");
        int important = scanner.nextInt();
        String local = scanner.nextLine();
        System.out.println("Enter content");
        String content = scanner.nextLine();
        System.out.println("Enter Category: 1.Note; 2.Meeting; 3.Call, 4.Birthday");
        int category = scanner.nextInt();
        local = scanner.nextLine();
        tasks.add(new Task(name, Task.setImportant(important), date, content, Task.setCategory(category)));
    }

    public void deleteTask(String taskName) {
        tasks =
                tasks.stream()
                        .filter(t -> !t.getName().equals(taskName))
                        .collect(Collectors.toList());
    }

    public void sortTasksByImportanceDate() {
        System.out.print("Not competed tasks sorted by Importance and Date");
        List<Task> sorted =
                tasks.stream()
                        .filter(t -> !t.isCompleted())
                        .collect(Collectors.toList());
        Collections.sort(sorted);
        System.out.println(sorted);
        System.out.println();
    }

    public void sortCompletedTasksByData() {
        System.out.print("Competed tasks sorted by Date");
        Comparator dataComparator = new ImportanceDataComparator();
        List<Task> sorted =
                tasks.stream()
                        .filter(Task::isCompleted)
                        .collect(Collectors.toList());
        Collections.sort(sorted, dataComparator);
        System.out.println(sorted);
        System.out.println();
    }

    public void showAllTasks() {
        System.out.println("Show all tasks");
        System.out.println(tasks);
        System.out.println();
    }

    public void taskCompleted(String taskName) {
        for (Task t : tasks) {
            if (t.getName().equals(taskName)) {
                t.setCompleted(true);
            }
        }
    }

    public void writeBinTask(String fileName) {
        try (
                OutputStream out = new FileOutputStream(fileName);
                DataOutputStream dataOut = new DataOutputStream(out)
        ) {
            for (Task t : tasks) {
                dataOut.writeUTF(t.getName());
                dataOut.writeUTF(t.getImportance().toString());
                dataOut.writeUTF(t.getDateString());
                dataOut.writeUTF(t.getContent());
                dataOut.writeUTF(t.getCategory().toString());
                dataOut.writeUTF(String.valueOf(t.isCompleted()));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void readBinTask(String fileName) {
        try (
                InputStream in = new FileInputStream(fileName);
                DataInputStream dataIn = new DataInputStream(in)
        ) {
            while (dataIn.available() > 0) {
                tasks.add(new Task(dataIn.readUTF(), dataIn.readUTF(), dataIn.readUTF(), dataIn.readUTF(), dataIn.readUTF(), dataIn.readUTF()));

            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return login + ":" + password;
    }
}