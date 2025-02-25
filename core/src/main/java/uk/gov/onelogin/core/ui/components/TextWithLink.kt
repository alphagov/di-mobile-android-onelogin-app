package uk.gov.onelogin.core.ui.components

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow

/**
 * Adds `inlineContent` Map to ClickableText
 * identical otherwise to the Android provided ClickableText
 * `inlineContent` Map is how the Text Composable does inline content
 */
@SuppressWarnings("kotlin:S107") // Suppressing due to matching Android
@Composable
fun TextWithLink(
    modifier: Modifier = Modifier,
    linkText: AnnotatedString,
    text: String? = null,
    style: TextStyle = TextStyle.Default,
    softWrap: Boolean = true,
    overflow: TextOverflow = TextOverflow.Clip,
    maxLines: Int = Int.MAX_VALUE,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    inlineContent: Map<String, InlineTextContent> = mapOf(),
    onClick: (Int) -> Unit
) {
    val annotatedString = buildAnnotatedString {
        text?.let {
            append(it)
        }
        pushStringAnnotation(tag = "URL", annotation = "")
        append(linkText)
        pop()
    }

    val layoutResult = remember { mutableStateOf<TextLayoutResult?>(null) }

    val pressIndicator = Modifier.pointerInput(onClick) {
        detectTapGestures { pos ->
            layoutResult.value?.let { layoutResult ->
                val offset = layoutResult.getOffsetForPosition(pos)
                annotatedString.getStringAnnotations(
                    tag = "URL",
                    start = offset - 1,
                    end = offset - 1
                )
                    .firstOrNull()?.let { onClick(offset) }
            }
        }
    }

    BasicText(
        text = annotatedString,
        modifier = modifier.then(pressIndicator),
        style = style,
        softWrap = softWrap,
        overflow = overflow,
        maxLines = maxLines,
        onTextLayout = {
            layoutResult.value = it
            onTextLayout(it)
        },
        inlineContent = inlineContent
    )
}
