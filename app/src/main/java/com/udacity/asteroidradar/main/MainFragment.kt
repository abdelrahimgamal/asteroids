package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.adapter.AsteroidClick
import com.udacity.asteroidradar.adapter.AsteroidsAdapter
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }
    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val asteroidsAdapter = AsteroidsAdapter(AsteroidClick { asteroid ->
            findNavController().navigate(MainFragmentDirections.actionShowDetail(asteroid))
        })

        viewModel.asteroids.observe(viewLifecycleOwner) {
            asteroidsAdapter.asteroids = it
        }
        binding.asteroidRecycler.apply {
            adapter = asteroidsAdapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.weekly -> viewModel.refreshData(FilterOptions.WEEKLY)
            R.id.today -> viewModel.refreshData(FilterOptions.DAILY)
            R.id.all -> viewModel.refreshData(FilterOptions.ALL)
        }
        return true
    }
}
