package de.myprojects.my_asteroid_radar.main

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import de.myprojects.my_asteroid_radar.R
import de.myprojects.my_asteroid_radar.databinding.FragmentMainBinding
import de.myprojects.my_asteroid_radar.detail.DetailFragment
import de.myprojects.my_asteroid_radar.domain.PictureOfDay
import de.myprojects.my_asteroid_radar.utils.bindTextViewToAstronomicalUnit
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var asteroidAdapter: AsteroidAdapter

    private val viewModel: MainViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onViewCreated()"
        }
        ViewModelProvider(
            this,
            MainViewModelFactory(activity.application)
        ).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        setHasOptionsMenu(true)
        setBinding(inflater)
        setImageOfDay()
        initRecyclerView()
        observeAsteroid()
        observeConnection()
        observeNavigation()

        return binding.root
    }


    private fun setBinding(inflater: LayoutInflater) {
        binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel
    }

    private fun setImageOfDay() {
        viewModel.imageOfDay.observe(viewLifecycleOwner, { image ->
            Picasso.with(this.activity)
                .load(image.url)
                .into(activity_main_image_of_the_day)

            setContentDescription(image)
        })
    }

    private fun setContentDescription(image: PictureOfDay) {
        activity_main_image_of_the_day.contentDescription = image.title
    }

    private fun initRecyclerView() {
        asteroidAdapter = AsteroidAdapter(AsteroidListener { asteroid ->
            viewModel.onListItemClicked(asteroid)
        })

        binding.asteroidRecycler.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = asteroidAdapter
        }
    }

    private fun observeAsteroid() {
        viewModel.asteroids.observe(viewLifecycleOwner, {

            it?.let {
                asteroidAdapter.submitList(it)
            }
        })
    }

    private fun observeConnection() {
        viewModel.connectionError.observe(viewLifecycleOwner, { connectionError ->
            if (connectionError == MainViewModel.Connection.ERROR) {
                Toast.makeText(requireActivity(), "No connection", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun observeNavigation() {
        viewModel.navigateToDetail.observe(viewLifecycleOwner) { itemClicked ->

            itemClicked?.let {
                this.findNavController().navigate(
                    MainFragmentDirections
                        .actionMainFragmentToDetailFragment(it)
                )
                viewModel.onDetailNavigated()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }
}
