package BackEnd;

public class HealthTip {
    private int tipId;
    private String tipText;

    public HealthTip(int tipId, String tipText) {
        this.tipId = tipId;
        this.tipText = tipText;
    }

    public int getTipId() {
        return tipId;
    }

    public String getTipText() {
        return tipText;
    }

    @Override
    public String toString() {
        return tipText;
    }
}
