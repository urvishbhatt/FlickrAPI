package com.example.flickrapi.View.Activities


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.flickrapi.Model.FlickrPhotoProperties
import com.example.flickrapi.R
import com.example.flickrapi.Utils.FlickrImageAdapter
import com.example.flickrapi.ViewModel.MainActivityViewModel
import com.example.flickrapi.databinding.ActivityMainBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import com.example.flickrapi.Utils.Constants.DATE_POSTED_ASC
import com.example.flickrapi.Utils.Constants.DATE_POSTED_DASC
import com.example.flickrapi.Utils.isConnected


class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    lateinit var binding: ActivityMainBinding
    var searchedHistory : ArrayList<String> = ArrayList<String>()
    var viewModel = MainActivityViewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        if (isConnected(baseContext)) {

            viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]

            val adapter = FlickrImageAdapter()
            viewModel.curruntImageLive.observe(this, { list ->

                binding.progressCircular.visibility = View.INVISIBLE

                if(binding.flickrRecyclerView.adapter == null){
                    Log.d(TAG, "RecyclerView initialized first-time")

                    binding.flickrRecyclerView.adapter = adapter
                    val gridLayoutManager = GridLayoutManager(this,2)
                    gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    binding.flickrRecyclerView.layoutManager = gridLayoutManager

                    adapter.data = list as List<FlickrPhotoProperties>
                } else {
                    Log.d(TAG, "RecyclerView initialized")

                    adapter.data = list as List<FlickrPhotoProperties>

                    runOnUiThread {
                        adapter.notifyDataSetChanged()
                    }
                }
            })

            searchedHistory.add("Bamberg")
            var arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, searchedHistory)
            binding.searchBox.setAdapter(arrayAdapter)

            binding.searchButton.setOnClickListener { view ->
                binding.progressCircular.visibility = View.VISIBLE
                viewModel.search_query = binding.searchBox.text.toString()
                viewModel.fetchData(viewModel.search_query,true)
                searchedHistory.add(viewModel.search_query)
                arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, searchedHistory)
                binding.searchBox.setAdapter(arrayAdapter)
            }


            binding.flickrRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    if (!recyclerView.canScrollVertically(1) && dy > 0){
                        if (!viewModel.isdataFetching){
                            viewModel.addImages()
                            Log.d(TAG, "onScrolled: calling addImages()")
                        }
                    }
                }
            })


        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        binding.progressCircular.visibility = View.VISIBLE

        when(item.itemId){

            R.id.action_newest -> {
                viewModel.order = DATE_POSTED_ASC
                viewModel.fetchData(viewModel.search_query,true)
                Log.d(TAG, "onOptionsItemSelected: Newest photos selected")
            }
            R.id.action_oldest ->  {
                viewModel.order = DATE_POSTED_DASC
                viewModel.fetchData(viewModel.search_query,true)
                Log.d(TAG, "onOptionsItemSelected: Oldest photos selected")
            }

        }
        return super.onOptionsItemSelected(item)
    }
}
