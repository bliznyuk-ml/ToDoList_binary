package org.example;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Task implements Comparable<Task> {

    private String name;
    private Importance importance;
    private String dateString;
    private String content;
    private Category category;
    private Date date;
    private boolean completed;

    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

    public Task(String name, Importance importance, String dateString, String content, Category category) {
        this.name = name;
        this.importance = importance;

        if (dateString.matches("(0|1|2|3)\\d-(0|1)\\d-(1|2)(0|9)\\d{2}")) {
            this.dateString = dateString;
        } else {
            System.err.println("wrong date");
            this.dateString = "01-01-1900";
        }
        try {
            this.date = formatter.parse(this.dateString);
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }

        this.content = content;
        this.category = category;
        this.completed = false;
    }

    public Task(String name, String importance, String dateString, String content, String category, String completed) {
        this.name = name;
        this.importance = Importance.valueOf(importance);

        if (dateString.matches("(0|1|2|3)\\d-(0|1)\\d-(1|2)(0|9)\\d{2}")) {
            this.dateString = dateString;
        } else {
            System.err.println("wrong date");
            this.dateString = "01-01-1900";
        }
        try {
            this.date = formatter.parse(this.dateString);
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }

        this.content = content;
        this.category = Category.valueOf(category);
        this.completed = Boolean.parseBoolean(completed);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Importance getImportance() {
        return importance;
    }

    public void setImportance(Importance importance) {
        this.importance = importance;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        if (dateString.matches("(0|1|2|3)\\d-(0|1)\\d-(1|2)(0|9)\\d{2}")) {
            this.dateString = dateString;
        } else {
            System.err.println("wrong date");
            this.dateString = "01-01-1900";
        }
        try {
            this.date = formatter.parse(this.dateString);
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public static Importance setImportant(int i) {
        switch (i) {
            case (1):
                return Importance.VERY_IMPORTANT;
            case (2):
                return Importance.MOST_IMPORTANT;
            case (3):
                return Importance.IMPORTANT;
            case (4):
                return Importance.LITTLE_IMPORTANT;
            case (5):
                return Importance.NO_MATTER;
            default:
                return Importance.NOT_INSTALLED;
        }
    }

    public static Category setCategory(int c) {
        switch (c) {
            case (1):
                return Category.NOTE;
            case (2):
                return Category.MEETING;
            case (3):
                return Category.CALL;
            case (4):
                return Category.BIRTHDAY;
            default:
                return Category.NOT_INSTALLED;
        }
    }

    @Override
    public String toString() {
        return "\nTask{" +
                "name='" + name + '\'' +
                ", importance=" + importance +
                ", date='" + formatter.format(date) + '\'' +
                ", content='" + content + '\'' +
                ", category=" + category +
                ", is completed?=" + completed +
                '}';
    }

    @Override
    public int compareTo(Task o) {
        int result = this.importance.compareTo(o.importance);
        if (result == 0) {
            result = this.date.compareTo(o.date);
        }
        return result;
    }
}

enum Importance {
    VERY_IMPORTANT(1),
    MOST_IMPORTANT(2),
    IMPORTANT(3),
    LITTLE_IMPORTANT(4),
    NO_MATTER(5),
    NOT_INSTALLED(6);

    private final int imp;

    Importance(int imp) {
        this.imp = imp;
    }

    public int getImp() {
        return imp;
    }
}

enum Category {
    NOT_INSTALLED(0), NOTE(1), MEETING(2), CALL(3), BIRTHDAY(4);

    private final int cat;

    Category(int cat) {
        this.cat = cat;
    }
}


