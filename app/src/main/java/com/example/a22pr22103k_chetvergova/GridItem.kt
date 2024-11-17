package com.example.a22pr22103k_chetvergova

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun GridItem(resourceId: Int) {
    Image(
        painter = painterResource(id = resourceId),
        contentDescription = null,
        modifier = Modifier
            .size(64.dp)
            .padding(4.dp)
    )
}