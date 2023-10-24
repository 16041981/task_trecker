public class Subtask extends Task {

    int idEpic;
    public Subtask (String description, String name, String status, int idEpic){
        super(description, name, status);
        this.idEpic = idEpic;
    }

    @Override // переопределяем toString
    public String toString() {
        return "Subtask{" +
                "id=" + '\''+id + '\'' + "idEpic=" + '\''+idEpic + '\'' +
                ", description=" +'\''+ description + '\'' +
                ", name=" + name + ", status=" + status +'\''+ '}';
    }


}
