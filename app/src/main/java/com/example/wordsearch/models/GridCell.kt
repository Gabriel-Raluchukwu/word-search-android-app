package com.example.wordsearch.models

import timber.log.Timber

import com.example.wordsearch.util.CoordinateSystem
import com.example.wordsearch.util.Direction
import com.example.wordsearch.util.RelativePosition

class GridCell (val value:Char?, val index:Int){

    private val gridRow = 12

    val  boundaryCells:List<PositionedCell>
        get() = getBoundaryGridCells()



    fun getBoundaryGridCells():List<PositionedCell>{
        //Check if cell is a grid boundary cell
        //Grid Boundary cells contain only 5 Positioned cells while non-boundary cells contain 8 positioned cells
        if(isCornerCell()) {
         return getGridCornerBoundaryCells()
        }
        if(isGridBoundaryCell()) {
           return getGridBoundaryCells()
        }

        //Cell is neither a corner nor boundary cell
        val cells = mutableListOf<PositionedCell>()
        cells.apply {
            add(PositionedCell(index - 1,RelativePosition.LEFT))
            add(PositionedCell(index + 1,RelativePosition.RIGHT))
            add(PositionedCell(index - gridRow,RelativePosition.TOP))
            add(PositionedCell(index + gridRow ,RelativePosition.BOTTOM))
            add(PositionedCell((index - gridRow) + 1,RelativePosition.TOPRIGHT))
            add(PositionedCell((index - gridRow) - 1,RelativePosition.TOPLEFT))
            add(PositionedCell((index + gridRow) + 1 ,RelativePosition.BOTTOMRIGHT))
            add(PositionedCell((index + gridRow) - 1,RelativePosition.BOTTOMLEFT))
        }
        return cells
    }


     private fun isCornerCell():Boolean{
        val gridCornerPoints = CoordinateSystem.getCornerPoints(gridRow)
        return  gridCornerPoints.contains(index)
    }

    private fun isGridBoundaryCell():Boolean{
        val gridBoundaryPoints = CoordinateSystem.getBoundaryPoints(gridRow)
        return  gridBoundaryPoints.contains(index)
    }

    private fun getGridCornerBoundaryCells():List<PositionedCell>{
        val cornerCells = mutableListOf<PositionedCell>()
        val gridSize = gridRow * gridRow

        if(index == 0) cornerCells.apply {
            add(PositionedCell(1,RelativePosition.RIGHT))
            add(PositionedCell(index + gridRow,RelativePosition.BOTTOM))
            add(PositionedCell(index+ gridRow + 1,RelativePosition.BOTTOMRIGHT))
            }

        if(index == (gridRow - 1)) cornerCells.apply {
            add(PositionedCell(gridRow - 2, RelativePosition.LEFT))
            add(PositionedCell((gridRow - 1) + gridRow, RelativePosition.BOTTOM))
            add(PositionedCell((gridRow - 2) + gridRow, RelativePosition.BOTTOMLEFT))
            }

        if(index == (gridSize - gridRow)) cornerCells.apply {
            add(PositionedCell((gridSize - gridRow) + 1, RelativePosition.RIGHT))
            add(PositionedCell((gridSize - (gridRow * 2)), RelativePosition.TOP))
            add(PositionedCell((gridSize - (gridRow * 2) + 1), RelativePosition.TOPRIGHT))
        }

        if(index == (gridSize - 1)) cornerCells.apply {
            add(PositionedCell(gridSize - 2, RelativePosition.LEFT))
            add(PositionedCell((gridSize - 1) - gridRow, RelativePosition.TOP))
            add(PositionedCell((gridSize - 2) - gridRow, RelativePosition.TOPLEFT))
        }
        return cornerCells
    }

    private fun getGridBoundaryCells():List<PositionedCell>{
        val gridSize = gridRow * gridRow
        val boundaryCells = mutableListOf<PositionedCell>()

            //Top Row
            if(List(gridRow){it * 1}.contains(index)) boundaryCells.apply{
                add(PositionedCell(index -1,RelativePosition.LEFT))
                add(PositionedCell(index +1,RelativePosition.RIGHT))
                add(PositionedCell(index + gridRow, RelativePosition.BOTTOM))
                add(PositionedCell((index -1) + gridRow, RelativePosition.BOTTOMLEFT))
                add(PositionedCell((index +1) + gridRow, RelativePosition.BOTTOMRIGHT))
            }

            //Right Row
            if(List(gridRow){(it * gridRow) - 1 }.contains(index)) boundaryCells.apply{
                add(PositionedCell(index - gridRow,RelativePosition.TOP))
                add(PositionedCell(index + gridRow,RelativePosition.BOTTOM))
                add(PositionedCell(index - gridRow - 1,RelativePosition.TOPLEFT))
                add(PositionedCell(index + gridRow - 1,RelativePosition.BOTTOMLEFT))
                add(PositionedCell(index - 1,RelativePosition.TOPLEFT))
            }
            //Left Row
            if(List(gridRow - 1){(it * gridRow)}.contains(index)) boundaryCells.apply{
                add(PositionedCell(index - gridRow,RelativePosition.TOP))
                add(PositionedCell(index + gridRow,RelativePosition.BOTTOM))
                add(PositionedCell(index - gridRow + 1,RelativePosition.TOPRIGHT))
                add(PositionedCell(index + gridRow + 1,RelativePosition.BOTTOMRIGHT))
                add(PositionedCell(index + 1,RelativePosition.RIGHT))
            }
            //Bottom Row
            if(List(gridRow){(gridSize - 1) - it}.contains(index)) boundaryCells.apply{
                add(PositionedCell(index - 1,RelativePosition.LEFT))
                add(PositionedCell(index + 1,RelativePosition.RIGHT))
                add(PositionedCell(index - gridRow,RelativePosition.TOP))
                add(PositionedCell(index  - (gridRow - 1),RelativePosition.TOPRIGHT))
                add(PositionedCell(index - (gridRow + 1),RelativePosition.TOPLEFT))
            }
        return boundaryCells
    }

    fun placementDirection(): Direction {
        val total = (gridRow * gridRow) - 1 //143 cells in total
        val leftBoundaryCells = mutableListOf<Int>()
        leftBoundaryCells.apply {
            addAll(List(gridRow){(it * gridRow) - 1 })
            removeAll { it  < 0 }
            removeAt(0)// Removes Corner Cell
        }

        val rightBoundaryCells = mutableListOf<Int>()
        rightBoundaryCells.apply {
            addAll(List(gridRow - 1){(it * gridRow)})
            removeAt(0)// Removes Corner Cell
        }

        return when(index){
            in 1 until gridRow - 1  -> Direction.DOWN
            in total - 2 downTo total - (gridRow - 1) -> Direction.UP
            in leftBoundaryCells -> Direction.RIGHT
            in rightBoundaryCells -> Direction.LEFT
            else -> determinePlacementDirection()
        }
    }

    fun determinePlacementDirection():Direction{

        val quadrantOne = mutableListOf(Direction.RIGHT,Direction.DOWN)
        val quadrantTwo = mutableListOf(Direction.LEFT,Direction.DOWN)
        val quadrantThree = mutableListOf(Direction.RIGHT,Direction.UP)
        val quadrantFour = mutableListOf(Direction.LEFT,Direction.UP)

        //row number and column number start from zero i.e zero-index based
        val rowNumber = index / gridRow
        val columnNumber = index % gridRow
        Timber.d("Row number: ${rowNumber + 1} Column number: ${columnNumber + 1}")
        return when{
            //FirstQuadrant
            rowNumber <= 6 && columnNumber <= 6 -> quadrantOne.random()
            //Second Quadrant
            rowNumber <= 7 && columnNumber >= 7 -> quadrantTwo.random()
            //Third Quadrant
            rowNumber >= 7 && columnNumber <= 6 -> quadrantThree.random()
            //Fourth Quadrant
            rowNumber >= 7 && columnNumber >=7 -> quadrantFour.random()
            else -> Direction.DOWN
        }
    }
}