package mv.sdd.io;

// NE PAS MODIFIER
public class Action {

    private final ActionType type;
    private final int param1;
    private final int param2;
    private final String param3;

    public Action(ActionType type) {
        this(type, 0, 0, null);
    }

    public Action(ActionType type, int param1) {
        this(type, param1, 0, null);
    }

    public Action(ActionType type, int param1, int param2, String param3) {
        this.type = type;
        this.param1 = param1;
        this.param2 = param2;
        this.param3 = param3;
    }

    public ActionType getType() {
        return type;
    }

    public int getParam1() {
        return param1;
    }

    public int getParam2() {
        return param2;
    }

    public String getParam3() {
        return param3;
    }

    @Override
    public String toString() {
        String chaine = type.name();
        if (param1 != 0) chaine += ";" + param1;
        if (param2 != 0) chaine += ";" + param2;
        if (param3 != null && !param3.isBlank()) chaine += ";" + param3;
        return chaine;
    }
}
