package BackEnd;

public class NutritionData {
    private String mealType;
    private int calories;
    private float fats;
    private float proteins;

    public NutritionData(String mealType, int calories, float fats, float proteins) {
        this.mealType = mealType;
        this.calories = calories;
        this.fats = fats;
        this.proteins = proteins;
    }

    public String getMealType() { return mealType; }
    public int getCalories() { return calories; }
    public float getFats() { return fats; }
    public float getProteins() { return proteins; }
}
