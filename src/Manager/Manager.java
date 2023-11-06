package Manager;

public class Manager{

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }
}
