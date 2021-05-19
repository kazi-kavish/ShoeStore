package com.udacity.shoestore.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.udacity.shoestore.R
import com.udacity.shoestore.databinding.CardShoeBinding
import com.udacity.shoestore.databinding.FragmentShoeListBinding
import com.udacity.shoestore.models.Shoe
import com.udacity.shoestore.models.SharedShoeViewModel
import timber.log.Timber


/**
 * A simple [Fragment] subclass.
 * Use the [ShoeListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ShoeListFragment : Fragment() {

    // Referred: https://developer.android.com/topic/libraries/architecture/viewmodel?utm_campaign=android_series_viewmodeldoc_110817#sharing

    // Use the 'by activityViewModels()' Kotlin property delegate
    // from the fragment-ktx artifact
    private val viewModel: SharedShoeViewModel by activityViewModels()

    private lateinit var binding: FragmentShoeListBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_shoe_list, container, false)

        binding.shoeViewModel = viewModel
        binding.setLifecycleOwner(this)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        viewModel.eventAddShoe.observe(viewLifecycleOwner, { isAdded ->
            if(isAdded){
                viewModel.onAddShoeComplete()
                navigateToShoeDetails()
            }
        })

        viewModel.shoes.observe( viewLifecycleOwner, { shoes ->
            showAllShoes(shoes)
        })
    }

    private fun navigateToShoeDetails() {
        Navigation.findNavController(binding.addShoeButton).navigate(ShoeListFragmentDirections.actionShoeListFragmentToShoeDetailsFragment())
    }

    private fun showAllShoes(shoes: List<Shoe>) {
        with(binding) {
            shoes.forEach { shoeListLinearLayout.addView(createShoeCard(it)) }
        }
    }

    private fun createShoeCard(shoe: Shoe): CardView {

        // Create a binding object for the card_shoe layout
        val shoeCard = DataBindingUtil.inflate<CardShoeBinding>(layoutInflater, R.layout.card_shoe, binding.shoeListLinearLayout, false)

        with(shoeCard) {

            val newCardID = "${shoe.company}${shoe.name}${shoe.size}".hashCode()
            card.id = newCardID
            shoeName.text = shoe.name
            shoeCompany.text = shoe.company
            shoeSize.text = shoe.size.toString()
            shoeDesc.text = shoe.description

            // random to create variation
            shoeImage.setImageResource(randomShoe())
            return card
        }
    }

    private fun randomShoe() : Int {
        return listOf(
            R.drawable.product_black,
            R.drawable.product_brown,
            R.drawable.product_blue,
            R.drawable.product_bluegray,
            R.drawable.product_camouflage
        ).random()
    }
}