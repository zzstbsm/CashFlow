package com.zhengzhou.cashflow.about_me

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration

internal enum class AboutMeAnnotatedStrings(
    @StringRes val text: Int,
    @StringRes val link: Int,
    val annotatedText: String,
) {

    GITHUB(
        text = R.string.GitHub,
        link = R.string.GitHub_link,
        annotatedText = "zzstbsm"
    ),
    LINKEDIN(
        text = R.string.LinkedIn,
        link = R.string.LinkedIn_link,
        annotatedText = "Zhou Zheng"
    );

    private val annotationURL = "URL"
    private var annotatedString: AnnotatedString? = null

    @Composable
    private fun buildAnnotatedString(): AnnotatedString {
        annotatedString = buildAnnotatedString {
            val text = stringResource(id = text)

            val startIndex = text.indexOf(annotatedText)
            val endIndex = startIndex + annotatedText.length

            append(text)
            addStyle(
                style = SpanStyle(
                    color = Color.Blue,
                    textDecoration = TextDecoration.Underline
                ),
                start = startIndex,
                end = endIndex,
            )
            addStringAnnotation(
                tag = annotationURL,
                annotation = stringResource(id = link),
                start = startIndex,
                end = endIndex,
            )
        }
        return annotatedString as AnnotatedString
    }

    @Composable
    fun getAnnotatedString(): AnnotatedString {
        return annotatedString ?: buildAnnotatedString()
    }

    @SuppressWarnings("WeakerAccess")
    fun getURLAnnotation(): String = annotationURL

    fun onClick(
        uriHandler: UriHandler,
        start: Int,
        end: Int,
    ) {

        requireNotNull(annotatedString) {
            "Exception: annotation not initialized, get the annotation first"
        }

        annotatedString!!
            .getStringAnnotations(getURLAnnotation(), start, end)
            .firstOrNull()?.let { stringAnnotation ->
                uriHandler.openUri(stringAnnotation.item)
            }

    }

}