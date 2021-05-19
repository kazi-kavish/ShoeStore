package com.udacity.shoestore.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import timber.log.Timber

class SharedShoeViewModel: ViewModel() {

    companion object {

        // Constants representing field names to be displayed in error messages

        private const val SHOE_NAME = "Shoe name"           // shoe-name field

        private const val SHOE_SIZE = "Shoe size"           // shoe-size field

        private const val SHOE_COMPANY = "Shoe company"     // shoe-company field

        private const val SHOE_DESC = "Shoe description"    // shoe-desc field

    }

    // a list to hold shoes in a mutable list form
    private lateinit var shoeList: MutableList<Shoe>        // since we don't want to wrap a MutableList directly in the LiveData - to ensure proper encapsulation

    // List of currently available shoes wrapped as LiveData
    private val _shoes = MutableLiveData<List<Shoe>>()
    val shoes: LiveData<List<Shoe>>
            get() = _shoes

    // Properties used to enable two-way data-binding with views in the ShoeDetailsFragment
    val shoeName = MutableLiveData<String>()
    val shoeCompany = MutableLiveData<String>()
    val shoeDesc = MutableLiveData<String>()
    val shoeSize = MutableLiveData<String>()

    // Property to mark "Save" button click
    private val _eventSaveShoe = MutableLiveData<Boolean>()
    val eventSaveShoe : LiveData<Boolean>
            get() = _eventSaveShoe

    // property to communicate a "required field missing" error to the UI-controller
    private val _eventMissingField = MutableLiveData<String>()
    val eventMissingField : LiveData<String>
        get() = _eventMissingField

    // event triggered when user clicks "cancel" button
    private val _eventCancel = MutableLiveData<Boolean>()
    val eventCancel : LiveData<Boolean>
        get() = _eventCancel

    // Property to mark "Add" button click in ShoeListFragment
    private val _eventAddShoe = MutableLiveData<Boolean>()
    val eventAddShoe : LiveData<Boolean>
        get() = _eventAddShoe

    init{

        Timber.i("MVM initialized")

        createInitiaList()

        initLiveData()

    }

    private fun createInitiaList() {
        shoeList = mutableListOf(
            Shoe("Sneaker", 13.5, "Reebok", "Reebok Men’s CL NYLON Classic Sneaker ", listOf()),
            Shoe("Runner", 14.0, "Nike", "Nike Men’s Air Zoom Pegasus 36 Running Shoe", listOf()),
            Shoe("Runner", 12.5, "Adidas", "Adidas Cloudfoam Running Shoe", listOf()),
            Shoe("Worker", 10.0, "Fila", "Fila Men’s Memory Workshift Slip Resistant Work Shoe", listOf()),
            Shoe("Oxford", 12.5, "Cole", "Kenneth Cole REACTION Men’s Left Lace Up Oxford ", listOf())
        )
    }

    private fun initLiveData() {

        _shoes.value = shoeList.toList()           // we don't want to wrap a mutable list inside LiveData  - whether private or public

        shoeName.value = ""
        shoeSize.value = ""
        shoeCompany.value = ""
        shoeDesc.value = ""

        _eventAddShoe.value = false
        _eventCancel.value = false
        _eventSaveShoe.value = false
        _eventMissingField.value = ""

    }

    // This function is invoked when user clicks "Save" button on ShoeDetails screen
    fun onSaveShoe(){
        
        val missingField = missingField() 
        if(missingField.isNotEmpty()) {
            _eventMissingField.value = missingField
        } else {
            createNewShoe()
            _shoes.value =  shoeList.toList()
            _eventSaveShoe.value = true
        }
    }

    fun onSaveShoeComplete() {
        _eventSaveShoe.value = false

        // also reset fields, we don't want to show stale values when user returns to ShoeDetails screen
        shoeName.value = ""
        shoeSize.value = ""
        shoeCompany.value = ""
        shoeDesc.value = ""
    }

    private fun createNewShoe() {
        val newShoe = Shoe( shoeName.value!!, shoeSize.value!!.toDouble(), shoeCompany.value!!, shoeDesc.value!!, listOf())
        shoeList.add(0, newShoe)
    }

    private fun missingField() = when {
        shoeName.value.isNullOrEmpty() -> SHOE_NAME
        shoeSize.value.isNullOrEmpty() || shoeSize.value?.toDoubleOrNull() == null -> SHOE_SIZE
        shoeCompany.value.isNullOrEmpty() -> SHOE_COMPANY
        shoeDesc.value.isNullOrEmpty() -> SHOE_DESC
        else -> ""
    }

    fun onMissingFieldComplete() {
        _eventMissingField.value = ""
    }

    // This function is invoked when user clicks "Cancel" button on ShoeDetails screen
    fun onCancel() {
        resetFields()
        _eventCancel.value = true
    }

    private fun resetFields() {
        shoeName.value = ""
        shoeSize.value = ""
        shoeCompany.value = ""
        shoeDesc.value = ""
    }

    fun onCancelComplete() {
        _eventCancel.value = false
    }

    fun onAddShoe() {
        _eventAddShoe.value = true
    }

    fun onAddShoeComplete() {
        _eventAddShoe.value = false
    }

    override fun onCleared() {
        super.onCleared()
        Timber.i("MainVM destroyed ...")
    }
}