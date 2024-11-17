package com.example.a22pr22103k_chetvergova

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun GridScreen(cols: Int, rows: Int, picturePrefix: String) {

    val resourceList = remember { makeShuffledPictureArray(cols, rows, picturePrefix) } //переменная для хранения рисунков в перемешку
    val cellStates = remember { mutableStateListOf<Status>().apply { repeat(cols * rows) { add(Status.CELL_CLOSE) } } } //создаем яйчейки закрытые
    var firstSelectedCell by remember { mutableIntStateOf(-1)} //-1 тк ничего не выбрано
    val context = LocalContext.current //для отображения toast

    LazyVerticalGrid( //вертикальная сетка для игры
        columns = GridCells.Fixed(cols),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(cellStates.size) { index ->
            GridItem(
                resourceId = resourceList[index],
                status = cellStates[index],
                onClick = {
                    if (firstSelectedCell == -1) { //если яйчейка не выбрана, открываем
                        firstSelectedCell = index
                        openCell(index, cellStates)
                    } else { // Проверяем, совпадают ли выбранные ячейки
                        if (checkIfMatch(firstSelectedCell, index, resourceList)) {
                            deleteCells(firstSelectedCell, index, cellStates) // Удаляем совпавшие ячейки
                        } else {
                            closeCells(firstSelectedCell, index, cellStates)// Закрываем ячейки, если они не совпали
                        }
                        firstSelectedCell = -1// Сбрасываем выбранную ячейку
                    }
                    if (checkGameOver(cellStates)) {
                        Toast.makeText(context, "Game Over", Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }
    }
}

fun makeShuffledPictureArray(cols: Int, rows: Int, picturePrefix: String): List<Int> { //для рандомного расположения
    val totalPairs = (cols * rows) / 2
    val resourceList = mutableListOf<Int>()

    for (i in 1..totalPairs) {
        val resourceName = "$picturePrefix$i"
        val resourceId = R.drawable::class.java.getDeclaredField(resourceName).getInt(null) //получаем айди ресурса
        resourceList.add(resourceId)
        resourceList.add(resourceId) //добавляем пары
    }

    return resourceList.shuffled()//делаем в разноброс
}


fun openCell(position: Int, cellStates: MutableList<Status>) {
    if (cellStates[position] != Status.CELL_DELETE) {// Устанавливаем состояние ячейки как открытая
        cellStates[position] = Status.CELL_OPEN
    }
}

fun closeCells(first: Int, second: Int, cellStates: MutableList<Status>) {
    cellStates[first] = Status.CELL_CLOSE// Закрываем ячейки, если они не совпали
    cellStates[second] = Status.CELL_CLOSE
}

fun deleteCells(first: Int, second: Int, cellStates: MutableList<Status>) {
    cellStates[first] = Status.CELL_DELETE// Удаляем ячейки, если они совпали
    cellStates[second] = Status.CELL_DELETE
}

fun checkIfMatch(first: Int, second: Int, resourceList: List<Int>): Boolean {
    return resourceList[first] == resourceList[second]// Проверка совпадения ресурсов
}

fun checkGameOver(cellStates: List<Status>): Boolean {
    return !cellStates.contains(Status.CELL_CLOSE)// Проверка, остались ли закрытые ячейки
}

@Composable
fun GridItem(resourceId: Int, status: Status, onClick: () -> Unit) {
    val imageResource = when (status) { // Определяем, какой ресурс показывать в зависимости от статуса ячейки
        Status.CELL_OPEN -> resourceId // Показываем ресурс, если ячейка открыта
        Status.CELL_CLOSE -> R.drawable.close // Изображение для закрытой ячейки
        Status.CELL_DELETE -> R.drawable.none // Изображение для удаленной ячейки
    }
    Image(// Отображаем изображение с распознаванием клика
        painter = painterResource(id = imageResource),
        contentDescription = null,
        modifier = Modifier
            .size(64.dp)
            .clickable { onClick() }// Обработка клика на ячейку
    )
}

enum class Status {
    CELL_OPEN, CELL_CLOSE, CELL_DELETE// Определение статусов ячеек
}


