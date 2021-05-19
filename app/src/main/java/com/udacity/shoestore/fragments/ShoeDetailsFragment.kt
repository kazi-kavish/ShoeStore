package com.udacity.shoestore.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.udacity.shoestore.R
import com.udacity.shoestore.databinding.FragmentShoeDetailsBinding
import com.udacity.shoestore.models.SharedShoeViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [ShoeDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ShoeDetailsFragment : Fragment() {


    private lateinit var binding: FragmentShoeDetailsBinding

    // Use the 'by activityViewModels()' Kotlin property delegate
    // from the fragment-ktx artifact
    private val viewModel: SharedShoeViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_shoe_details, container, false)

        binding.shoeViewModel = viewModel
        binding.setLifecycleOwner(this)

        return binding.root

    }

    // Using this method to acess the VM, as it is an activity level VM - and this is called after activity is fully created
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.eventSaveShoe.observe( viewLifecycleOwner, { shoeAdded ->
            if(shoeAdded) {
                viewModel.onSaveShoeComplete()
                displayShoeList()
            }
        })

        viewModel.eventMissingField.observe(viewLifecycleOwner, { missingField ->
            if( missingField.isNotEmpty() ) {
                showMissingFieldError(missingField)
                viewModel.onMissingFieldComplete()
            }
        })

        viewModel.eventCancel.observe( viewLifecycleOwner, { cancelled ->
            if(cancelled) {
                viewModel.onCancelComplete()
                displayShoeList()
            }
        })
    }

    // Show a Toast with an error message naming the missing shoe detail
    private fun showMissingFieldError(missingField : String) {
        Toast.makeText(activity, String.format(getString(R.string.error_missing_detail, missingField)), Toast.LENGTH_LONG).show()
    }

    private fun displayShoeList(){
        Navigation.findNavController(binding.saveButton).navigate(
            ShoeDetailsFragmentDirections.actionShoeDetailsFragmentToShoeListFragment())
    }
}