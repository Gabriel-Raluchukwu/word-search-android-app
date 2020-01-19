package com.example.wordsearch.gameFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wordsearch.models.GridCell
import com.example.wordsearch.models.PositionedCell
import com.example.wordsearch.util.CoordinateSystem
import com.example.wordsearch.util.Direction
import com.example.wordsearch.util.RelativePosition
import timber.log.Timber
import java.lang.StringBuilder
import java.util.*

class GameViewModel : ViewModel(){
    private val gridRow = 12
    private val coordinateSystem = CoordinateSystem(gridRow)
    private val foundWords = mutableListOf<String>()
    private val builder = StringBuilder()

    //Occupied GridCell Indices
    private val _occupiedCells = mutableListOf<Int>()
    //UnOccupied Cells
    private val _unOccupiedCells: MutableList<Int> = getAllCellPositions()

    private val _numberOfFoundWords = MutableLiveData<Int>()
    val numberOfFoundWords:LiveData<Int>
        get() = _numberOfFoundWords

    //List of Words to search
    private val _searchWords =  MutableLiveData<List<String>>()
    val searchWords: LiveData<List<String>>
        get() = _searchWords

    private val _isWord =  MutableLiveData<Boolean>()
    val isWord:LiveData<Boolean>
        get() = _isWord

    private val _isCompleteWord =  MutableLiveData<Boolean>()
    val isCompleteWord:LiveData<Boolean>
        get() = _isCompleteWord

    //Grid is a N * N matrix where N = number_of_rows = number_of_columns = 12
    private val _gridRowLiveData = MutableLiveData<Int>()
    val gridRowLiveData: LiveData<Int>
        get() =  _gridRowLiveData

    private val _gridCellList = MutableLiveData<List<Char>>()
    val gridCellList: LiveData<List<Char>>
        get() = _gridCellList

    private val alphabets = mutableListOf('A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z')

    //Starting with sorted list of words
    private val wordList = listOf("objectivec","variable","inject","kotlin","mobile","swift","java","test")

    init{
        _searchWords.value = wordList.shuffled(Random())
        _gridCellList.value = populateGrid()
        _gridRowLiveData.value = 12
        _isWord.value = false
        _isCompleteWord.value = false
        _numberOfFoundWords.value = 0
        Timber.i("Created GameViewModel class")
    }

    override fun onCleared() {
        super.onCleared()
        Timber.i("ViewModel cleared")
    }

    fun refreshGame(){
        foundWords.clear()
        _occupiedCells.clear()
        _unOccupiedCells.apply {
            clear()
            addAll(getAllCellPositions())
        }
        _numberOfFoundWords.value = 0
        _gridCellList.value = populateGrid()
    }

    private fun populateGrid(): List<Char>{
        val gridSize = gridRow * gridRow
        var wordGridList = mutableListOf<GridCell>()
        var wordPositionPair = mutableListOf<Pair<Int,Char>>()
        for(word in wordList){
            var placement:List<GridCell>?
           do {
                placement = generate(word.toUpperCase())
           }while (placement == null)

            wordGridList.addAll(placement)
        }
       wordPositionPair.addAll( List(gridSize){ num ->
           Pair(num,wordGridList.find {cell -> cell.index == num}?.value ?: alphabets.random())//TODO: CHANGE 0 to random alphabets
       })
        return List(gridSize){ wordPositionPair[it].second }
    }

    fun buildSelectedCharacters(character:String,position:Int){
        var test: Boolean = false
        var char = character.toCharArray().first()
        builder.append(char)
        wordList.forEach { word ->
            var _word = word.toUpperCase()
            //var test1 = builder.length
            val equalityTest =
                if(builder.length > _word.length){ false } else { (builder.toString()) == _word.substring(0 until (builder.length - 0)) }
            if(equalityTest) test = equalityTest

            if(builder.toString().toLowerCase() == word){
                foundWords.add(word)
                _numberOfFoundWords.value?.plus(1)
                _isCompleteWord.value = true
            }
        }
        if(!test) {
            Timber.d("Current Character: $character. isWord -> ${_isWord.value}")
            clearSelectedCharactersStringBuilder()
            builder.append(character)
        }
        _isWord.value = test
    }

    fun onWordCompleted(){
        _isCompleteWord.value = false
    }

    private fun clearSelectedCharactersStringBuilder(){
        builder.clear()
    }

    private fun splitStringToCharacters(word:String): List<Char>{
        return word.toCharArray().toList()
    }

    private fun getAllCellPositions(): MutableList<Int>{
        val gridSize = gridRow * gridRow
        return MutableList(gridSize){it * 1}
    }

    private fun selectRandomPosition(word: String):Int{
        return if(word.length >= 9 || word.length < 5) CoordinateSystem.getBoundaryPoints(gridRow).shuffled().first() else _unOccupiedCells.random()
    }

    private fun generate(word:String) : List<GridCell>?{
        var status = true
        var lastCell: GridCell
        val gridList = mutableListOf<GridCell>()
        val charArray = splitStringToCharacters(word)
        var position = selectRandomPosition(word)

        if(_occupiedCells.contains(position)){
            status = false
        }
        else{

            var initialCell = GridCell('-',position)
            val direction = initialCell.placementDirection()
            val characterArray = if(direction == Direction.LEFT) charArray.reversed() else charArray
            initialCell.value = characterArray[0]
            gridList.add(initialCell)
            lastCell = gridList.first()
            for(character in characterArray.subList(1,word.length)){
                var canPlace = placeCharacter(lastCell,direction)
               if(canPlace.first){
                   var cell = GridCell(character,canPlace.second)
                   gridList.add(cell)
                   _occupiedCells.add(cell.index)
                   _unOccupiedCells.remove(cell.index)
                   lastCell = cell
               }
                else{
                   //Cannot place character in specified direction
                   status = false
               }
            }
        }
        return if(status){gridList} else {null}
    }

    private fun placeCharacter( lastCell:GridCell, direction: Direction): Pair<Boolean,Int>{
        val neighbourCells = lastCell.getBoundaryGridCells()
        return when(direction){
            Direction.UP -> {
                val cell:PositionedCell? = neighbourCells.find { it.position == RelativePosition.TOP}
                Pair(coordinateSystem.canPlaceCharacterTop(lastCell.index ,_occupiedCells),cell?.value ?: -1)
            }
            Direction.DOWN -> {
                val cell:PositionedCell? = neighbourCells.find { it.position == RelativePosition.BOTTOM}
                Pair(coordinateSystem.canPlaceCharacterBottom(lastCell.index,_occupiedCells),cell?.value ?: -1)
            }
            Direction.RIGHT -> {
                val cell:PositionedCell? = neighbourCells.find { it.position == RelativePosition.RIGHT}
                Pair(coordinateSystem.canPlaceCharacterRight(lastCell.index ,_occupiedCells),cell?.value ?: -1)
            }
            Direction.LEFT -> {
                val cell:PositionedCell? = neighbourCells.find { it.position == RelativePosition.LEFT}
                Pair(coordinateSystem.canPlaceCharacterLeft(lastCell.index ,_occupiedCells),cell?.value ?: -1)
            }
        }
    }
}
