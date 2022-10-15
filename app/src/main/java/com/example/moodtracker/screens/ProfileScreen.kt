package com.example.moodtracker

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun ProfileScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Image(
            modifier = Modifier
                .padding(top = 50.dp, bottom = 50.dp)
                .align(Alignment.CenterHorizontally)
                .size(200.dp),
            painter = painterResource(id = R.drawable.user_profile),
            contentDescription = null,
        )

        TextField(
            modifier = Modifier,
            value = "Ankita Mankar",
            onValueChange = {},
            readOnly = true,
            label = {
                Text(text = "User Name")
            },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                focusedLabelColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
            ),
        )

        TextField(
            modifier = Modifier,
            value = "This App Is Made with <3",
            onValueChange = {},
            readOnly = true,
            label = {
                Text(text = "About")
            },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                focusedLabelColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
            ),
        )

        TextField(
            modifier = Modifier,
            value = "sarach.wallace@gmail.com",
            onValueChange = {},
            label = {
                Text(text = "Email")
            },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                focusedLabelColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
            ),
        )
    }
}
