package AdventureModel;

public class AdventureMap {
    private StringBuilder Traversed = new StringBuilder();

    public AdventureMap(Room initialRoom){
        this.Traversed.append(initialRoom.getRoomName());
    }

    public void visit(Room r){
        this.Traversed.append(" -> " + r.getRoomName());
    }

    public String show(){
        return String.valueOf(Traversed);
    }
}
