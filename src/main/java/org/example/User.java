package org.example;

import java.util.*;
import java.util.stream.Collectors;

public class User {
    private String name;
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    List<Task> tasks = new ArrayList<>();

    Scanner scanner = new Scanner(System.in);

    public User(String name, String password) {
        this.name = name;
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

    public void readTask(String name, String importance, String date, String content, String category, String completed) {
        tasks.add(new Task(name, importance, date, content, category, completed));
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

    public String writeTask() {
        String s = "";
        for (Task t : tasks) {
            s += t.getName() + ":" + t.getImportance().toString() + ":" + t.getDateString() + ":" + t.getContent() + ":" + t.getCategory().toString() + ":" + t.isCompleted() + "\n";
        }
        return s;
    }

    @Override
    public String toString() {
        return name + ":" + password;
    }
}

class MakeUsers {
    static Set<User> users = new HashSet<>();
    static Scanner scanner = new Scanner(System.in);

    public static void addUser() {
        System.out.println("Enter login: ");
        String login = scanner.nextLine();
        System.out.println("Enter password: ");
        String password = scanner.nextLine();
        users.add(new User(login, password));
    }

    public static void showUsers() {
        for (User u : users)
            System.out.println(u.toString());
    }

}
