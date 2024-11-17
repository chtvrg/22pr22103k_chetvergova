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

    val resourceList = remember { makeShuffledPictureArray(cols, rows, picturePrefix) }


    val cellStates = remember { mutableStateListOf<Status>().apply { repeat(cols * rows) { add(Status.CELL_CLOSE) } } }

    var firstSelectedCell by remember { mutableIntStateOf(-1) }


    val context = LocalContext.current

    LazyVerticalGrid(
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
                    if (firstSelectedCell == -1) {

                        firstSelectedCell = index
                        openCell(index, cellStates)
                    } else {

                        if (checkIfMatch(firstSelectedCell, index, resourceList)) {

                            deleteCells(firstSelectedCell, index, cellStates)
                        } else {

                            closeCells(firstSelectedCell, index, cellStates)
                        }
                        firstSelectedCell = -1
                    }
                    if (checkGameOver(cellStates)) {

                        Toast.makeText(context, "Game Over", Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }
    }
}

fun makeShuffledPictureArray(cols: Int, rows: Int, picturePrefix: String): List<Int> {
    val totalPairs = (cols * rows) / 2
    val resourceList = mutableListOf<Int>()

    for (i in 1..totalPairs) {
        val resourceName = "$picturePrefix$i"
        val resourceId = R.drawable::class.java.getDeclaredField(resourceName).getInt(null)
        resourceList.add(resourceId)
        resourceList.add(resourceId)
    }

    return resourceList.shuffled()
}


fun openCell(position: Int, cellStates: MutableList<Status>) {
    if (cellStates[position] != Status.CELL_DELETE) {
        cellStates[position] = Status.CELL_OPEN
    }
}

fun closeCells(first: Int, second: Int, cellStates: MutableList<Status>) {
    cellStates[first] = Status.CELL_CLOSE
    cellStates[second] = Status.CELL_CLOSE
}

fun deleteCells(first: Int, second: Int, cellStates: MutableList<Status>) {
    cellStates[first] = Status.CELL_DELETE
    cellStates[second] = Status.CELL_DELETE
}

fun checkIfMatch(first: Int, second: Int, resourceList: List<Int>): Boolean {
    return resourceList[first] == resourceList[second]
}

fun checkGameOver(cellStates: List<Status>): Boolean {
    return !cellStates.contains(Status.CELL_CLOSE)
}

@Composable
fun GridItem(resourceId: Int, status: Status, onClick: () -> Unit) {
    val imageResource = when (status) {
        Status.CELL_OPEN -> resourceId
        Status.CELL_CLOSE -> R.drawable.close
        Status.CELL_DELETE -> R.drawable.none
    }
    Image(
        painter = painterResource(id = imageResource),
        contentDescription = null,
        modifier = Modifier
            .size(64.dp)
            .clickable { onClick() }
    )
}

enum class Status {
    CELL_OPEN, CELL_CLOSE, CELL_DELETE
}


