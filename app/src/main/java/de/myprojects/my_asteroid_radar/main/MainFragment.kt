package de.myprojects.my_asteroid_radar.main

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import de.myprojects.my_asteroid_radar.R
import de.myprojects.my_asteroid_radar.databinding.FragmentMainBinding
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var asteroidAdapter: AsteroidAdapter

    private val viewModel: MainViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onViewCreated()"
        }
        //The ViewModelProviders (plural) is deprecated.
        //ViewModelProviders.of(this, DevByteViewModel.Factory(activity.application)).get(DevByteViewModel::class.java)
        ViewModelProvider(this, MainViewModelFactory(activity.application)).get(MainViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        setHasOptionsMenu(true)
        setBinding(inflater)
        initRecyclerView()
        observeAsteroid()
        setImageOfDay()

        return binding.root
    }

    private fun setBinding(inflater: LayoutInflater){
        binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel
    }

    private fun setImageOfDay() {
        viewModel.imageOfDay.observe(viewLifecycleOwner, { image ->

            Picasso.with(this.activity)
                .load(image.url)
                .into(activity_main_image_of_the_day)
        })
    }

    private fun initRecyclerView() {
        asteroidAdapter = AsteroidAdapter(AsteroidListener { asteroidId ->
            Toast.makeText(this.context, "$asteroidId", Toast.LENGTH_SHORT).show()
        })

        binding.asteroidRecycler.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = asteroidAdapter
        }
    }

    private fun observeAsteroid(){
        viewModel.test.observe(viewLifecycleOwner,  {
            it?.let {
                asteroidAdapter.submitList(it)
            }
        })
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }
}
