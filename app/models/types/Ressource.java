package models.types;

public enum Ressource {

    JAUNE("Jaune"), ROUGE("Rouge"), BLEU("Bleu");

    String label;

    Ressource(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
