package org.example;

import java.util.Comparator;

public class ImportanceDataComparator implements Comparator<Task> {

    @Override
    public int compare(Task o1, Task o2) {
        return o2.getImportance().getImp() - o1.getImportance().getImp();
    }
}
