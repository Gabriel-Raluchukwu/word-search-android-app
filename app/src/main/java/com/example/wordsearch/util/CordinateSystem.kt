package com.example.wordsearch.util

class CoordinateSystem  constructor(val gridRow: Int){

    private lateinit var  cornerPoints : List<Int>
    private lateinit var boundaryPoints: List<Int>

    init {
        cornerPoints = getCornerPoints(gridRow)
        boundaryPoints = getBoundaryPoints(gridRow)
    }

    companion object{
        fun getCornerPoints(gridRow: Int): List<Int>{
            var cornerPoints = mutableListOf<Int>()
            var gridSize = gridRow * gridRow
            cornerPoints.apply{
                add(0)//Starting point is a corner point
                add(0 + (gridRow - 1))
                add(gridSize - 1)
                add(gridSize - gridRow)
            }
            return cornerPoints
        }

        fun getBoundaryPoints(gridRow:Int):List<Int>{
            var gridSize = gridRow * gridRow
            var gridBoundaryCells = mutableSetOf<Int>()

                gridBoundaryCells.apply {
                    addAll(List(gridRow){(it * gridRow) - 1 })
                    removeAll{it < 0}
                    addAll(List(gridRow){it * 1})
                    addAll(List(gridRow - 1){(it * gridRow)})
                    addAll(List(gridRow){(gridSize - 1) - it})
                }
            return gridBoundaryCells.toList()
        }
    }

    fun canPlaceCharacterTop(position:Int, occupiedCells:List<Int>):Boolean{
        return when(position - gridRow){
            -1 -> false
            in cornerPoints -> false
            in boundaryPoints -> false
            in occupiedCells -> false
            else -> true
        }
    }

    fun canPlaceCharacterBottom(position: Int,occupiedCells: List<Int>):Boolean{
        return when(position + gridRow){
             -1 -> false
             in cornerPoints -> false
             in boundaryPoints -> false
             in occupiedCells -> false
            else -> true
        }
    }

    fun canPlaceCharacterLeft(position: Int,occupiedCells: List<Int>):Boolean{
        return when(position - 1){
            -1 -> false
            in cornerPoints -> false
            in boundaryPoints -> false
            in occupiedCells -> false
            else -> true
        }
    }

    fun canPlaceCharacterRight(position: Int,occupiedCells: List<Int>):Boolean{
        return when(position + 1){
            -1 -> false
            in cornerPoints -> false
            in boundaryPoints -> false
            in occupiedCells -> false
            else -> true
        }
    }

    fun placeCharacterTopLeft(position: Int,occupiedCells: List<Int>):Boolean{
        return when((position - gridRow) - 1){
            -1 -> false
            in cornerPoints -> false
            in boundaryPoints -> false
            in occupiedCells -> false
            else -> true
        }
    }

    fun canPlaceCharacterTopRight(position: Int,occupiedCells: List<Int>):Boolean{
        return when((position - gridRow) + 1){
            -1 -> false
            in cornerPoints -> false
            in boundaryPoints -> false
            in occupiedCells -> false
            else -> true
        }
    }

    fun canPlaceCharacterBottomLeft(position: Int,occupiedCells: List<Int>):Boolean{
        return when((position + gridRow) - 1){
            -1 -> false
            in cornerPoints -> false
            in boundaryPoints -> false
            in occupiedCells -> false
            else -> true
        }
    }

    fun placeCharacterBottomRight(position: Int,occupiedCells: List<Int>):Boolean{
        return when((position + gridRow) + 1){
            -1 -> false
            in cornerPoints -> false
            in boundaryPoints -> false
            in occupiedCells -> false
            else -> true
        }
    }
}