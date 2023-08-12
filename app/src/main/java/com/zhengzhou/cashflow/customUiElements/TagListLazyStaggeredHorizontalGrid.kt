package com.zhengzhou.cashflow.customUiElements

import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.zhengzhou.cashflow.data.Tag

@Composable
fun TagListLazyStaggeredHorizontalGrid(
    tagList: List<Tag>,
    modifier: Modifier = Modifier,
) {
    LazyHorizontalStaggeredGrid(
        rows = StaggeredGridCells.Fixed(1),
        content = {
            if (tagList.isEmpty()) {
                item {
                    SingleTag(
                        tag = "",
                        selected = true,
                        onTagClick = { },
                    )
                }
            } else {
                items(tagList.size) { position ->
                    val tag = tagList[position]
                    SingleTag(
                        tag = tag.name,
                        selected = true,
                        onTagClick = { },
                    )
                }
            }
        },
        modifier = modifier
    )
}