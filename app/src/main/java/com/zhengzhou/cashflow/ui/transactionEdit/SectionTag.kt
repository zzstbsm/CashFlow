package com.zhengzhou.cashflow.ui.transactionEdit

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zhengzhou.cashflow.R
import com.zhengzhou.cashflow.customUiElements.SingleTag
import com.zhengzhou.cashflow.data.Tag
import com.zhengzhou.cashflow.data.TagEntry

@Composable
fun TagSection(
    currentTagList: List<Tag>,
    completeTagList: List<TagEntry>,
    currentTagText: String,
    onChangeText: (String) -> Unit,
    onTagAdd: (String) -> Unit,
    onTagClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {

    var showDropdownMenu by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        // Text edit part
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OutlinedTextField(
                value = currentTagText,
                singleLine = true,
                onValueChange = {
                    onChangeText(it)
                    showDropdownMenu = it.length > 2
                },
                label = {
                    Text(text = stringResource(id = R.string.tag))
                },
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth(.7f),
            )
            Button (
                onClick = {
                    onTagAdd(currentTagText)
                    onChangeText("")
                    showDropdownMenu = false
                },
                modifier = Modifier
                    .padding(4.dp),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add),
                    contentDescription = stringResource(id = R.string.accessibility_add_tag),
                    modifier = Modifier
                        .size(32.dp),
                )
            }
        }
        TagListFilter(
            filteredTagList = TagEntry.tagListFiltered(currentTagText, completeTagList),
            onSelectTag = { selectedTag ->
                showDropdownMenu = false
                onChangeText(selectedTag)
            },
            showDropdownMenu = showDropdownMenu,
        )
        Spacer(
            modifier = Modifier
                .height(8.dp)
        )

        // Tag list part
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Start,
        ) {
            TagListPart(
                tagList = currentTagList,
                onTagClick = { onTagClick(it) }
            )
        }
        Spacer(
            modifier = Modifier
                .height(32.dp)
        )
    }
}

@SuppressLint("UnrememberedMutableState", "MutableCollectionMutableState")
@Composable
private fun TagListPart(
    tagList: List<Tag>,
    onTagClick: (Int) -> Unit,
) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        LazyColumn {

            if (tagList.isEmpty()) {
                item {
                    SingleTag(
                        tag = "",
                        onTagClick = {  },
                        selected = false
                    )
                }
            } else {
                items(tagList.size) { position ->
                    SingleTag(
                        tag = tagList[position].name,
                        onTagClick = { onTagClick(position) },
                        selected = tagList[position].enabled
                    )
                }
            }
        }
    }
}

@Composable
private fun TagListFilter(
    filteredTagList: List<TagEntry>,
    onSelectTag: (String) -> Unit,
    showDropdownMenu: Boolean,
) {

    if (showDropdownMenu && filteredTagList.isNotEmpty()) {
        Box {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                LazyColumn(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    items(filteredTagList.size) { position ->
                        val tag = filteredTagList[position]
                        Text(
                            text = tag.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onSelectTag(tag.name)
                                }
                                .padding(8.dp)
                        )
                    }
                }

            }

        }
    }
}