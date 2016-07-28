package models.types;

public enum Ressource {

    JAUNE("Jaune", "#ffeb3b"), ROUGE("Rouge", "#f44336"), BLEU("Bleu", "#2196f3");

    private String label;
    private String color;

    Ressource(String label, String color) {
        this.label = label;
        this.color = color;
    }

    public String getLabel() {
        return label;
    }

    public String getColor() {
        return color;
    }
}
