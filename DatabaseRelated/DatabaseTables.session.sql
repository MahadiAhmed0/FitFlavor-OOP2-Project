CREATE TABLE users (
    username VARCHAR(50) PRIMARY KEY,
    password VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    weight DOUBLE NOT NULL
);

CREATE TABLE Meals (
    meal_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    calories INT,
    fats FLOAT,
    proteins FLOAT
);
CREATE TABLE Ingredients (
    ingredient_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) UNIQUE NOT NULL
);
CREATE TABLE MealIngredients (
    meal_id INT,
    ingredient_id INT,
    PRIMARY KEY (meal_id, ingredient_id),
    FOREIGN KEY (meal_id) REFERENCES Meals(meal_id) ON DELETE CASCADE,
    FOREIGN KEY (ingredient_id) REFERENCES Ingredients(ingredient_id) ON DELETE CASCADE
);

CREATE TABLE DailyMeals (
     id INT AUTO_INCREMENT PRIMARY KEY,
     meal_type VARCHAR(50),
     meal_id INT,
     date DATE,
     FOREIGN KEY (meal_id) REFERENCES Meals(meal_id)
 );

 CREATE TABLE WeeklyMeals (
    id INT AUTO_INCREMENT PRIMARY KEY,
    meal_type VARCHAR(50),
    meal_id INT,
    week_start_date DATE,
    day_of_week VARCHAR(10),
    FOREIGN KEY (meal_id) REFERENCES Meals(meal_id)
);
