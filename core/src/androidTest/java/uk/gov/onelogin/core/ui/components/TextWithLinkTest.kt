package uk.gov.onelogin.core.ui.components

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.click
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import org.junit.Test
import uk.gov.onelogin.core.TestCase

class TextWithLinkTest : TestCase() {
    @Test
    fun testOnlyLinkTextClickOnLink() {
        var clickIndex = -1
        composeTestRule.setContent {
            TextWithLink(
                linkText = AnnotatedString("Click Me"),
                modifier = Modifier.testTag("ClickableText"),
                onClick = { index ->
                    clickIndex = index
                }
            )
        }

        // Perform a click in the middle of the text
        composeTestRule.onNodeWithTag("ClickableText", useUnmergedTree = true)
            .performTouchInput {
                click(center)
            }

        // Assert that the click was received in the correct range
        assert(clickIndex in 4..7) // "Click Me" - click should be somewhere within "Me"
    }

    @Test
    fun testOnlyLinkTextClickOutsideLink() {
        var clicked = false
        composeTestRule.setContent {
            TextWithLink(
                linkText = AnnotatedString("Click Me"),
                modifier = Modifier.testTag("ClickableText"),
                onClick = {
                    clicked = true
                }
            )
        }

        // Perform a click outside the bounds of the text
        composeTestRule.onNodeWithTag("ClickableText", useUnmergedTree = true)
            .performTouchInput {
                click(bottomLeft.copy(x = bottomLeft.x - 10f)) // Click outside to the left
            }

        // Assert that the click was not handled
        assert(!clicked)
    }

    @Test
    fun testLinkWithNormalTextClickOnNormalText() {
        var clicked = false
        val annotatedString =
            buildAnnotatedString {
                withStyle(SpanStyle(textDecoration = TextDecoration.Underline)) {
                    append("Click Me")
                }
            }
        composeTestRule.setContent {
            TextWithLink(
                text = "Normal Text",
                linkText = annotatedString,
                modifier = Modifier.testTag("ClickableText"),
                onClick = {
                    clicked = true
                }
            )
        }

        // Perform a click in the middle of the normal text
        composeTestRule.onNodeWithTag("ClickableText", useUnmergedTree = true)
            .performTouchInput {
                click(center.copy(x = center.x - 50f)) // Click in the "Normal Text" part
            }

        // Assert that the click was not handled
        assert(!clicked)
    }

    @Test
    fun testLinkWithNormalTextClickOnLinkText() {
        var clicked = false
        val annotatedString =
            buildAnnotatedString {
                withStyle(SpanStyle(textDecoration = TextDecoration.Underline)) {
                    append("Click Me")
                }
            }
        composeTestRule.setContent {
            TextWithLink(
                text = "Normal Text",
                linkText = annotatedString,
                modifier = Modifier.testTag("ClickableText"),
                onClick = {
                    clicked = true
                }
            )
        }

        // Perform a click in the middle of the link text
        composeTestRule.onNodeWithTag("ClickableText", useUnmergedTree = true)
            .performTouchInput {
                click(bottomRight.copy(x = bottomRight.x - 10f)) // Click in the "Click me part
            }

        // Assert that the click was handled
        assert(clicked)
    }
}
