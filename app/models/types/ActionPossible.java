package models.types;

public enum ActionPossible {

    SALUER("Saluer"), PLANTER_DRAPEAU("Planter mon drapeau");

    String label;

    ActionPossible(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

}
