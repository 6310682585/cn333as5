package com.example.mynotes.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mynotes.domain.model.PhoneModel
import com.example.mynotes.util.fromHex

@ExperimentalMaterialApi
@Composable
fun Phone(
    modifier: Modifier = Modifier,
    phone: PhoneModel,
    onPhoneClick: (PhoneModel) -> Unit = {},
    isSelected: Boolean
) {
    val background = if (isSelected)
        Color.LightGray
    else
        MaterialTheme.colors.surface

    Card(
        shape = RoundedCornerShape(4.dp),
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth(),
        backgroundColor = background
    ) {
        ListItem(
            text = {
                Text(text = phone.name, maxLines = 1)
                   },
            secondaryText = {
                Text(text = phone.phoneNum, maxLines = 1)
            },
            icon = {
                NoteColor(
                    color = Color.fromHex(phone.color.hex),
                    size = 40.dp,
                    border = 1.dp
                )
            },
            modifier = Modifier.clickable {
                onPhoneClick.invoke(phone)
            },
            trailing = {
                if (phone.tag.id.toInt() != 1) {
                    Text(text = phone.tag.tagName, maxLines = 1)
                }
            }
        )

    }
}

@ExperimentalMaterialApi
@Preview
@Composable
private fun PhonePreview() {
    Phone(phone = PhoneModel(1, "J", "082"), isSelected = true)
}