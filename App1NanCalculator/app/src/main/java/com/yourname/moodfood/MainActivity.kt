package com.yourname.moodfood

import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button

class MainActivity : AppCompatActivity() {

    private var selectedMood = ""
    private var selectedCondition = ""

    private val moodLayouts = mutableListOf<LinearLayout>()
    private val conditionLayouts = mutableListOf<LinearLayout>()

    // mood → its default drawable name
    private val moodBackgrounds = mapOf(
        "lazy" to R.drawable.mood_bg_lazy,
        "sad" to R.drawable.mood_bg_sad,
        "grumpy" to R.drawable.mood_bg_grumpy,
        "stressed" to R.drawable.mood_bg_stressed,
        "adventurous" to R.drawable.mood_bg_adventurous,
        "self-care" to R.drawable.mood_bg_selfcare,
        "sophisticated" to R.drawable.mood_bg_sophisticated,
        "aggressive" to R.drawable.mood_bg_aggressive,
        "burned out" to R.drawable.mood_bg_burnedout
    )

    data class FoodResult(val name: String, val emoji: String, val description: String, val drawableName: String)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupMoodButtons()
        setupConditionButtons()
        setupBottomButtons()
        setupInfo()
    }

    private fun setupMoodButtons() {
        val moods = listOf(
            Pair(R.id.btnLazy, "lazy"),
            Pair(R.id.btnSad, "sad"),
            Pair(R.id.btnGrumpy, "grumpy"),
            Pair(R.id.btnStressed, "stressed"),
            Pair(R.id.btnAdventurous, "adventurous"),
            Pair(R.id.btnSelfCare, "self-care"),
            Pair(R.id.btnSophisticated, "sophisticated"),
            Pair(R.id.btnAggressive, "aggressive"),
            Pair(R.id.btnBurnedOut, "burned out")
        )

        moods.forEach { (id, mood) ->
            val layout = findViewById<LinearLayout>(id)
            moodLayouts.add(layout)
            layout.setOnClickListener {
                val anim = AnimationUtils.loadAnimation(this, R.anim.button_press)
                layout.startAnimation(anim)
                selectedMood = mood
                // Reset all mood buttons
                moods.forEach { (otherId, otherMood) ->
                    val bg = moodBackgrounds[otherMood] ?: R.drawable.mood_bg_lazy
                    findViewById<LinearLayout>(otherId).setBackgroundResource(bg)
                }
                // Highlight selected with purple border
                layout.setBackgroundResource(R.drawable.mood_btn_selected)
                updateDisplay()
            }
        }
    }

    private fun setupConditionButtons() {
        val conditions = listOf(
            Pair(R.id.btnSnack, "snack"),
            Pair(R.id.btnQuick, "quick"),
            Pair(R.id.btnBudget, "budget"),
            Pair(R.id.btnHealthy, "healthy")
        )

        conditions.forEach { (id, condition) ->
            val layout = findViewById<LinearLayout>(id)
            conditionLayouts.add(layout)
            layout.setOnClickListener {
                val anim = AnimationUtils.loadAnimation(this, R.anim.button_press)
                layout.startAnimation(anim)

                if (selectedCondition == condition) {
                    // Deselect
                    selectedCondition = ""
                    layout.setBackgroundResource(R.drawable.condition_btn_default)
                    // Reset text color to dark
                    (layout.getChildAt(1) as? TextView)?.setTextColor(getColor(R.color.text_dark))
                } else {
                    selectedCondition = condition
                    // Reset all condition buttons
                    conditions.forEach { (otherId, _) ->
                        val otherLayout = findViewById<LinearLayout>(otherId)
                        otherLayout.setBackgroundResource(R.drawable.condition_btn_default)
                        (otherLayout.getChildAt(1) as? TextView)?.setTextColor(getColor(R.color.text_dark))
                    }
                    // Highlight selected
                    layout.setBackgroundResource(R.drawable.condition_btn_selected)
                    (layout.getChildAt(1) as? TextView)?.setTextColor(getColor(R.color.text_white))
                }
                updateDisplay()
            }
        }
    }

    private fun updateDisplay() {
        val tvDisplay = findViewById<TextView>(R.id.tvDisplayText)
        val tvEmoji = findViewById<TextView>(R.id.tvFoodEmoji)

        when {
            selectedMood.isNotEmpty() && selectedCondition.isNotEmpty() -> {
                tvDisplay.text = "Mood: ${selectedMood.replaceFirstChar { it.uppercase() }} · ${selectedCondition.replaceFirstChar { it.uppercase() }}\nTap 'Get Food Suggestion'!"
                tvEmoji.text = "✨"
            }
            selectedMood.isNotEmpty() -> {
                tvDisplay.text = "Mood: ${selectedMood.replaceFirstChar { it.uppercase() }}\nNow pick a filter (optional)"
                tvEmoji.text = "🤔"
            }
            else -> {
                tvDisplay.text = "Your food suggestion will appear here"
                tvEmoji.text = "🍔"
            }
        }
    }

    private fun setupBottomButtons() {
        findViewById<Button>(R.id.btnCalculate).setOnClickListener {
            if (selectedMood.isEmpty()) {
                Toast.makeText(this, "Pick a mood first! 😊", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val result = getResult(selectedMood, selectedCondition)
            if (result != null) {
                val imageRes = resources.getIdentifier(result.drawableName, "drawable", packageName)
                    .takeIf { it != 0 } ?: R.drawable.food_placeholder
                ResultDialog.newInstance(result.name, result.description, imageRes)
                    .show(supportFragmentManager, "result")
            } else {
                Toast.makeText(this, "No match for that combo! Try another. 🤔", Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<Button>(R.id.btnReset).setOnClickListener {
            selectedMood = ""
            selectedCondition = ""

            // Reset all mood buttons
            listOf(
                Pair(R.id.btnLazy, "lazy"), Pair(R.id.btnSad, "sad"),
                Pair(R.id.btnGrumpy, "grumpy"), Pair(R.id.btnStressed, "stressed"),
                Pair(R.id.btnAdventurous, "adventurous"), Pair(R.id.btnSelfCare, "self-care"),
                Pair(R.id.btnSophisticated, "sophisticated"), Pair(R.id.btnAggressive, "aggressive"),
                Pair(R.id.btnBurnedOut, "burned out")
            ).forEach { (id, mood) ->
                val bg = moodBackgrounds[mood] ?: R.drawable.mood_bg_lazy
                findViewById<LinearLayout>(id).setBackgroundResource(bg)
            }

            // Reset all condition buttons
            listOf(R.id.btnSnack, R.id.btnQuick, R.id.btnBudget, R.id.btnHealthy).forEach { id ->
                val layout = findViewById<LinearLayout>(id)
                layout.setBackgroundResource(R.drawable.condition_btn_default)
                (layout.getChildAt(1) as? TextView)?.setTextColor(getColor(R.color.text_dark))
            }

            updateDisplay()
        }
    }

    private fun setupInfo() {
        findViewById<android.widget.ImageButton>(R.id.btnInfo).setOnClickListener {
            InstructionsDialog().show(supportFragmentManager, "instructions")
        }
    }

    private fun getResult(mood: String, condition: String): FoodResult? {
        return when {
            mood == "self-care" && condition == "budget" ->
                FoodResult("Grilled Cheese", "🧀", "Comfort in every golden, melty bite.", "food_grilled_cheese")
            mood == "self-care" && condition == "healthy" ->
                FoodResult("Avocado Toast", "🥑", "Nourish yourself, you deserve it.", "food_avocado_toast")
            mood == "self-care" && condition == "" ->
                FoodResult("Avocado Toast", "🥑", "A little self-love on a plate.", "food_avocado_toast")

            (mood == "lazy" || mood == "burned out") && condition == "quick" ->
                FoodResult("Instant Noodles", "🍜", "Zero effort, maximum comfort.", "food_instant_noodles")
            (mood == "lazy" || mood == "burned out") && condition == "healthy" ->
                FoodResult("Banana & Oat Smoothie", "🍌", "Nature's lazy energy boost.", "food_smoothie")
            (mood == "lazy" || mood == "burned out") && condition == "" ->
                FoodResult("Instant Noodles", "🍜", "Because effort is overrated today.", "food_instant_noodles")

            (mood == "sad" || mood == "grumpy") && condition == "quick" ->
                FoodResult("Nutella Toast", "🍫", "Sweet fix for a sour mood.", "food_nutella_toast")
            (mood == "sad" || mood == "grumpy") && condition == "snack" ->
                FoodResult("Warm Chocolate Brownie", "🍫", "Because chocolate fixes everything.", "food_brownie")
            (mood == "sad" || mood == "grumpy") && condition == "" ->
                FoodResult("Warm Chocolate Brownie", "🍫", "You deserve something warm and sweet.", "food_brownie")

            mood == "sophisticated" && (condition == "healthy" || condition == "") ->
                FoodResult("Seared Salmon", "🐟", "Elegant. Refined. Utterly delicious.", "food_salmon")
            mood == "sophisticated" && condition == "snack" ->
                FoodResult("French Onion Soup", "🧅", "A classic for discerning taste.", "food_french_onion_soup")

            mood == "adventurous" && condition == "healthy" ->
                FoodResult("Fruit Salad with Chili & Lime", "🍉", "Bold flavours for bold souls.", "food_fruit_salad")
            mood == "adventurous" && condition == "quick" ->
                FoodResult("Spicy Mango with Lime & Tajín", "🥭", "Sweet, spicy, and unapologetic.", "food_mango_tajin")
            mood == "adventurous" && condition == "" ->
                FoodResult("Spicy Mango with Lime & Tajín", "🥭", "Go bold or go home.", "food_mango_tajin")

            mood == "stressed" && condition == "budget" ->
                FoodResult("Buttered Popcorn", "🍿", "Crunch away the stress.", "food_popcorn")
            mood == "stressed" && condition == "snack" ->
                FoodResult("Frozen Grapes", "🍇", "Cool down, you've got this.", "food_frozen_grapes")
            mood == "stressed" && condition == "" ->
                FoodResult("Buttered Popcorn", "🍿", "Sometimes you just need to crunch.", "food_popcorn")

            mood == "aggressive" && condition == "quick" ->
                FoodResult("Beef Jerky", "🥩", "Bold, tough, and no-nonsense.", "food_beef_jerky")
            mood == "aggressive" && condition == "snack" ->
                FoodResult("Crunchy Carrots & Hummus", "🥕", "Take it out on the crunch.", "food_carrots_hummus")
            mood == "aggressive" && condition == "" ->
                FoodResult("Beef Jerky", "🥩", "Match that energy.", "food_beef_jerky")

            else -> null
        }
    }
}