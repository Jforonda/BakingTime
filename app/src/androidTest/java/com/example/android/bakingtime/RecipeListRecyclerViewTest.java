package com.example.android.bakingtime;

import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.espresso.contrib.RecyclerViewActions;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class RecipeListRecyclerViewTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule
            = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void secondItemClicked_IngredientsAreShown_SecondItemClicked_StepDetailIsShown() {
        onView(ViewMatchers.withId(R.id.recycler_view_recipe))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        onView(ViewMatchers.withId(R.id.recycler_view_recipe_detail_steps))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        onView(withId(R.id.text_view_recipe_step_detail_description)).check(matches(isDisplayed()));
    }

    @Test
    public void thirdItemClicked_IngredientsAreShown() {
        onView(ViewMatchers.withId(R.id.recycler_view_recipe))
                .perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));

        onView(withText("Ingredients")).check(matches(isDisplayed()));
    }

    @Test
    public void clickThroughApp_ClickBack_BackAtMainActivity() {
        onView(ViewMatchers.withId(R.id.recycler_view_recipe))
                .perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));

        onView(ViewMatchers.withId(R.id.recycler_view_recipe_detail_steps))
                .perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));

        pressBack();

        pressBack();

        onView(withId(R.id.recycler_view_recipe)).check(matches(isDisplayed()));
    }

}
