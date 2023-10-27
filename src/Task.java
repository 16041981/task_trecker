public class Task {

    protected int id;
    protected String description;
    protected String name;
    protected String status;

    public Task(int id, String description, String name, String status) {
        this.id = id;
        this.description = description;
        this.name = name;
        this.status = status;
    }

    public Task(String description, String name, String status) {
        this.description = description;
        this.name = name;
        this.status = status;
    }

    public Task(String description, String name) {
        this.description = description;
        this.name = name;
    }

    public Task(int id, String description, String name) {
        this.id = id;
        this.description = description;
        this.name = name;
    }

    public Task(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + '\''+id + '\'' +
                ", description=" +'\''+ description + '\'' +
                ", name=" + name + ", status=" + status +'\''+ '}';
    }

}
