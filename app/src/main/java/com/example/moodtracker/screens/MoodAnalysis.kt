package com.example.moodtracker.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.moodtracker.DBHelper
import com.example.moodtracker.data.Mood
import java.util.*
import androidx.compose.ui.Alignment.Companion as Alignment1

@Composable
fun MoodAnalysis() {

    // happy sad angry
    val values = remember { mutableStateOf(mutableListOf<Float>(0f,0f,0f)) }
    val list = remember { mutableStateOf(mutableListOf<Mood>()) }
    val dbHelper = DBHelper(LocalContext.current)



    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {

            // fetch all
            Button(onClick = {
                upToTodayAnalysis(list, values, dbHelper )
            }) {
                Text(text = "Currrent")
            }

            // fetch last week
            Button(onClick = {
                weeklyAnalysis(list, values, dbHelper) }) {
                Text(text = "Weekly")
            }

            // fetch last monthly
            Button(
                onClick = {
                    monthlyAnalysis(list, values, dbHelper)
                }
            ) {
                Text(text = "Monthly")
            }

        }

        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            PieChart(values = values.value)
        }
    }
}

private fun upToTodayAnalysis(list: MutableState<MutableList<Mood>>, values: MutableState<MutableList<Float>>, dbHelper: DBHelper) {

    list.value = dbHelper.fetchAllMoods().toMutableList();
    val tempList = mutableListOf<Float>(0f,0f,0f)
    values.value = tempList
    if (list.value.size > 0) {
        tempList[0] = Collections.frequency(list.value, Mood.HAPPY).toFloat()
        tempList[1] = Collections.frequency(list.value, Mood.SAD).toFloat()
        tempList[2] = Collections.frequency(list.value, Mood.ANGRY).toFloat()
    }
    values.value = tempList
}

private fun monthlyAnalysis(list: MutableState<MutableList<Mood>>, values: MutableState<MutableList<Float>>, dbHelper: DBHelper) {

    val calendar = Calendar.getInstance()
    val year: Int = calendar.get(Calendar.YEAR)
    val month: Int = calendar.get(Calendar.MONTH) + 1
    var day: Int = calendar.get(Calendar.DAY_OF_MONTH)

    val tempListOfMood = mutableListOf<Mood>()
    while(day >= 1) {
        val date: String = "$day-$month-$year"
        val tempListOfEvent = dbHelper.fetchEventsFromDate(date)
        tempListOfEvent.forEach {
            tempListOfMood.add(it.mood)
        }
        day--
    }
    list.value = tempListOfMood

    val tempList = mutableListOf<Float>(0f,0f,0f)
    values.value = tempList
    if (list.value.size > 0) {
        tempList[0] = Collections.frequency(list.value, Mood.HAPPY).toFloat()
        tempList[1] = Collections.frequency(list.value, Mood.SAD).toFloat()
        tempList[2] = Collections.frequency(list.value, Mood.ANGRY).toFloat()
    }
    values.value = tempList
}

private fun weeklyAnalysis(list: MutableState<MutableList<Mood>>, values: MutableState<MutableList<Float>>, dbHelper: DBHelper) {

    val calendar = Calendar.getInstance()
    val year: Int = calendar.get(Calendar.YEAR)
    val month: Int = calendar.get(Calendar.MONTH) + 1
    var day: Int = calendar.get(Calendar.DAY_OF_MONTH)
    var dayNo = calendar.get(Calendar.DAY_OF_WEEK)

    val tempListOfMood = mutableListOf<Mood>()
    while(dayNo >= 1) {
        val date: String = "$day-$month-$year"
        val tempListOfEvent = dbHelper.fetchEventsFromDate(date)
        tempListOfEvent.forEach {
            tempListOfMood.add(it.mood)
        }
        day--
        dayNo--
    }
    list.value = tempListOfMood

    val tempList = mutableListOf<Float>(0f,0f,0f)
    values.value = tempList
    if (list.value.size > 0) {
        tempList[0] = Collections.frequency(list.value, Mood.HAPPY).toFloat()
        tempList[1] = Collections.frequency(list.value, Mood.SAD).toFloat()
        tempList[2] = Collections.frequency(list.value, Mood.ANGRY).toFloat()
    }
    values.value = tempList
}

@Composable
fun PieChart(
    values: List<Float> = listOf(15f, 35f, 50f),
    colors: List<Color> = listOf(Color(0xFF58BDFF), Color(0xFF125B7F), Color(0xFF092D40)),
    legend: List<String> = listOf("HAPPY", "SAD", "ANGRY"),
    size: Dp = 200.dp
) {

    val sumOfValues = values.sum()

    val proportions = values.map {
        it * 100 / sumOfValues
    }

    val sweepAngles = proportions.map {
        360 * it / 100
    }

    Canvas(
        modifier = Modifier
            .size(size = size)
    ) {
        var startAngle = -90f
        for (i in sweepAngles.indices) {
            drawArc(
                color = colors[i],
                startAngle = startAngle,
                sweepAngle = sweepAngles[i],
                useCenter = true
            )
            startAngle += sweepAngles[i]
        }

    }

    Spacer(modifier = Modifier.height(32.dp))

    Column {
        for (i in values.indices) {
            DisplayMoodTypes(color = colors[i], legend = legend[i])
        }
    }

}

@Composable
fun DisplayMoodTypes(color: Color, legend: String) {

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment1.CenterVertically
    ) {
        Divider(
            modifier = Modifier.width(16.dp),
            thickness = 4.dp,
            color = color
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(text = legend)
    }
}