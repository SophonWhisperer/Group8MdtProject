package ds.cmu.edu.interestingpicture;

import android.graphics.Bitmap;

import java.util.LinkedList;

/**
 * Created by aparna on 4/27/2016.
 */
public class Recipe {
    public Bitmap getDish() {
        return dish;
    }

    public void setDish(Bitmap dish) {
        this.dish = dish;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRecipeUrl() {
        return recipeUrl;
    }

    public void setRecipeUrl(String recipeUrl) {
        this.recipeUrl = recipeUrl;
    }

    public LinkedList<String> getAllIngredients() {
        return allIngredients;
    }

    public void setAllIngredients(LinkedList<String> allIngredients) {
        this.allIngredients = allIngredients;
    }

    public LinkedList<String> getRemainingIngre() {
        return remainingIngre;
    }

    public void setRemainingIngre(LinkedList<String> remainingIngre) {
        this.remainingIngre = remainingIngre;
    }

    Bitmap dish;
    String title;
    String recipeUrl;
    LinkedList<String> allIngredients;
    LinkedList<String> remainingIngre;

}
