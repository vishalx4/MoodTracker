package com.example.moodtracker.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.moodtracker.DBHelper
import com.example.moodtracker.R
import com.example.moodtracker.data.Event
import com.example.moodtracker.data.Mood

@Composable
fun EventScreen(date: String) {
    val dbHelper = DBHelper(LocalContext.current)
    var dialogState by remember { mutableStateOf(false) }
    val eventList = dbHelper.fetchEventsFromDate(date)

    if (eventList.isEmpty()) {
        EmptyEvent(
            openDialog = { dialogState = true }
        )
    } else {
        ShowList(eventList, openDialog = { dialogState = true })
    }

    if (dialogState) {
        AddEvent(
            date = date,
            dbHelper = dbHelper,
            closeDialog = { dialogState = false }
        )
    }
}

@Composable
fun ShowList(list: List<Event>, openDialog: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {

        Image(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(10.dp)
                .size(60.dp, 60.dp)
                .clickable { openDialog.invoke() },
            painter = painterResource(id = R.drawable.add),
            contentDescription = "add event"
        )


        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn {
                items(list) {
                    EventItem(it)
                }
            }
        }
    }

}

private fun getImageVectorFromMood(mood: Mood): Int {
    return when(mood) {
        Mood.HAPPY -> R.drawable.happy
        Mood.SAD -> R.drawable.sad
        Mood.ANGRY -> R.drawable.angry
    }
}

@Composable
fun EventItem(event: Event) {

    val imageVector = ImageVector.vectorResource(id = getImageVectorFromMood(event.mood))

    Card(modifier = Modifier
        .fillMaxSize()
        .padding(20.dp)) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            val (lable, iconView) = createRefs()
            Text(
                text= event.name,
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(lable) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(iconView.start)
                        width = Dimension.fillToConstraints
                    }
            )
            Image(
                imageVector = imageVector,
                contentDescription = "calender icon",
                modifier = Modifier
                    .size(40.dp, 40.dp)
                    .constrainAs(iconView) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
            )
        }
    }
}

@Composable
fun EmptyEvent( openDialog: () -> Unit ) {

    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "List is empty Please add the Event")

            Image(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(10.dp)
                    .size(60.dp, 60.dp)
                    .clickable { openDialog.invoke() },
                painter = painterResource(id = R.drawable.add),
                contentDescription = "add event" )
        }
    }
}

@Composable
fun AddEvent(
    date: String,
    dbHelper: DBHelper,
    closeDialog: () -> Unit
) {
    val moodList: List<Mood> = listOf(Mood.HAPPY, Mood.SAD, Mood.ANGRY)
    var eventName by remember { mutableStateOf("") }
    val selectedMood = remember { mutableStateOf(-1) }

    Dialog(
        onDismissRequest = { closeDialog.invoke() },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false
        )
    ) {

        Surface() {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                TextField(value = eventName, onValueChange = { eventName = it })

                Spacer(modifier = Modifier.padding(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    for (i in moodList.indices) {
                        if (selectedMood.value == i) {
                            Image(
                                modifier = Modifier.padding(10.dp)
                                    .background(Color.Green)
                                    .clickable { selectedMood.value = i },
                                imageVector = ImageVector.vectorResource(id = getImageVectorFromMood(
                                    moodList[i]
                                )),
                                contentDescription = "icon"
                            )
                        } else {
                            Image(
                                modifier = Modifier.padding(10.dp)
                                    .clickable { selectedMood.value = i },
                                imageVector = ImageVector.vectorResource(id = getImageVectorFromMood(
                                    moodList[i]
                                )),
                                contentDescription = "icon"
                            )
                        }
                    }
                }

                Column(modifier = Modifier.fillMaxWidth()) {

                    Button(
                        onClick = {
                            dbHelper.addEvent(
                                date = date,
                                event = Event(eventName, Mood.values()[selectedMood.value])
                            )
                            closeDialog.invoke()
                        }
                    ) {
                        Text(text = "ADD")
                    }


                    Button(onClick = { closeDialog.invoke() }
                    ) {
                        Text(text = "CANCEL")
                    }
                }

            }
        }
    }
}
