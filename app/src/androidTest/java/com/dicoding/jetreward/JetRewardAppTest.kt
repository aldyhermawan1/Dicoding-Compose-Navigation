package com.dicoding.jetreward

import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.dicoding.jetreward.model.FakeRewardDataSource
import com.dicoding.jetreward.ui.navigation.Screen
import com.dicoding.jetreward.ui.theme.JetRewardTheme
import com.dicoding.jetreward.utils.assertCurrentRouteName
import com.dicoding.jetreward.utils.onNodeWithContentDescriptionStringId
import com.dicoding.jetreward.utils.onNodeWithStringId
import org.junit.*

class JetRewardAppTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    private lateinit var navController: TestNavHostController

    @Before
    fun setUp() {
        composeTestRule.setContent {
            JetRewardTheme {
                navController = TestNavHostController(LocalContext.current)
                navController.navigatorProvider.addNavigator(ComposeNavigator())
                JetRewardApp(navController = navController)
            }
        }
    }

    @Test
    fun navHost_verifyStartDestination() {
        navController.assertCurrentRouteName(Screen.Home.route)
    }

    @Test
    fun navHost_clickItem_navigatesToDetailWithData() {
        with(composeTestRule) {
            onNodeWithTag("RewardList").performScrollToIndex(10)
            onNodeWithText(FakeRewardDataSource.dummyRewards[10].title).performClick()
            navController.assertCurrentRouteName(Screen.DetailReward.route)
            onNodeWithText(FakeRewardDataSource.dummyRewards[10].title).assertIsDisplayed()
        }
    }

    @Test
    fun navHost_bottomNavigation_working() {
        with(composeTestRule) {
            onNodeWithStringId(R.string.menu_cart).performClick()
            navController.assertCurrentRouteName(Screen.Cart.route)
            onNodeWithStringId(R.string.menu_profile).performClick()
            navController.assertCurrentRouteName(Screen.Profile.route)
            onNodeWithStringId(R.string.menu_home).performClick()
            navController.assertCurrentRouteName(Screen.Home.route)
        }
    }

    @Test
    fun navHost_clickItem_navigatesBack() {
        with(composeTestRule) {
            onNodeWithTag("RewardList").performScrollToIndex(10)
            onNodeWithText(FakeRewardDataSource.dummyRewards[10].title).performClick()
            navController.assertCurrentRouteName(Screen.DetailReward.route)
            onNodeWithContentDescriptionStringId(R.string.back).performClick()
            navController.assertCurrentRouteName(Screen.Home.route)
        }
    }

    @Test
    fun navHost_checkout_rightBackStack() {
        with(composeTestRule) {
            onNodeWithText(FakeRewardDataSource.dummyRewards[4].title).performClick()
            navController.assertCurrentRouteName(Screen.DetailReward.route)
            onNodeWithStringId(R.string.plus_symbol).performClick()
            onNodeWithTag("count").assert(hasText("1"))
            onNodeWithContentDescription("Order Button").performClick()
            navController.assertCurrentRouteName(Screen.Cart.route)
            onNodeWithStringId(R.string.menu_home).performClick()
            navController.assertCurrentRouteName(Screen.Home.route)
        }
    }
}