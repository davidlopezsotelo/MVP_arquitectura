package com.davidlopez.mvp.mainModule.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.davidlopez.mvp.EventBus
import com.davidlopez.mvp.SportEvent
import com.davidlopez.mvp.databinding.ActivityMainBinding
import com.davidlopez.mvp.getAdEventsInRealtime
import com.davidlopez.mvp.getResultEventsInRealtime
import com.davidlopez.mvp.someTime
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), OnClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ResultAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setupAdapter()
        setupRecyclerView()
        setupSwipeRefresh()
        setupClicks()
    }



    private fun setupAdapter() {
        adapter = ResultAdapter(this)
    }
    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
        }
    }

    private fun setupSwipeRefresh() {
        binding.srlResults.setOnRefreshListener {
            //adapter.clear()
            getEvents()
            binding.btnAd.visibility = View.VISIBLE
        }
    }
    private fun setupClicks() {
        binding.btnAd.run {
            setOnClickListener {
                lifecycleScope.launch {
                   // binding.srlResults.isRefreshing = true
                    val events = getAdEventsInRealtime()
                    EventBus.instance().publish(events.first())
                }
            }
            setOnLongClickListener { view ->
                lifecycleScope.launch {
                   // binding.srlResults.isRefreshing = true
                    EventBus.instance().publish(SportEvent.ClosedAdEvent)
                }
                true
            }
        }
    }

    private fun getEvents() {
        lifecycleScope.launch {
            val events = getResultEventsInRealtime()
            events.forEach{ event ->
                delay(someTime())
                EventBus.instance().publish(event)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        getEvents()
    }



    // OnClickListener-------------------------------------------------------------------------------
    override fun onClick(result: SportEvent.ResultSuccess) {

        binding.srlResults.isRefreshing = true
        lifecycleScope.launch {
            //SportService.instance().saveResult(result)
        }
    }

    /*
    * View Layer---------CAPA DE VISTA----------------------------------------------------------------
    * */


    fun add(event: SportEvent.ResultSuccess){ adapter.add(event) }

    fun clearAdapter(){ adapter.clear() }

    fun showAdUI(isVisible: Boolean){
        binding.btnAd.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    fun showProgress(isVisible: Boolean){
        binding.srlResults.isRefreshing = isVisible
    }

    fun showToast(msg:String){
        Toast.makeText(this@MainActivity, msg,Toast.LENGTH_SHORT).show()
    }
    fun showSnackBar(msg:String){
        Snackbar.make(binding.root, msg,Snackbar.LENGTH_LONG).show()
    }

}