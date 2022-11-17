package com.dicoding.jetreward.ui.screen.detail

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import com.dicoding.jetreward.R
import com.dicoding.jetreward.model.OrderReward
import com.dicoding.jetreward.model.Reward
import com.dicoding.jetreward.onNodeWithStringId
import com.dicoding.jetreward.ui.theme.JetRewardTheme
import org.junit.*

class DetailScreenKtTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val fakeOrderReward = OrderReward(
        reward = Reward(4, R.drawable.reward_4, "Jaket Hoodie Dicoding", 7500),
        count = 0
    )

    @Before
    fun setUp() {
        composeTestRule.setContent {
            JetRewardTheme {
                DetailContent(
                    image = fakeOrderReward.reward.image,
                    title = fakeOrderReward.reward.title,
                    basePoint = fakeOrderReward.reward.requiredPoint,
                    count = fakeOrderReward.count,
                    onBackClick = { },
                    onAddToCart = { }
                )
            }
        }
        composeTestRule.onRoot().printToLog("currentLabelExists")
    }

    @Test
    fun detailContent_isDisplayed() {
        with(composeTestRule) {
            onNodeWithText(fakeOrderReward.reward.title).assertIsDisplayed()
            onNodeWithStringId(R.string.required_point, fakeOrderReward.reward.requiredPoint).assertIsDisplayed()
        }
    }

    @Test
    fun increaseProduct_buttonEnabled() {
        with(composeTestRule) {
            onNodeWithContentDescription("Order Button").assertIsNotEnabled()
            onNodeWithStringId(R.string.plus_symbol).performClick()
            onNodeWithContentDescription("Order Button").assertIsEnabled()
        }
    }

    @Test
    fun increaseProduct_correctCounter() {
        with(composeTestRule) {
            onNodeWithStringId(R.string.plus_symbol).performClick().performClick()
            onNodeWithTag("count").assert(hasText("2"))
        }
    }

    @Test
    fun decreaseProduct_stillZero() {
        with(composeTestRule) {
            onNodeWithStringId(R.string.minus_symbol).performClick()
            onNodeWithTag("count").assert(hasText("0"))
        }
    }
}