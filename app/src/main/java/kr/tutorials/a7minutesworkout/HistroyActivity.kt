package kr.tutorials.a7minutesworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kr.tutorials.a7minutesworkout.databinding.ActivityHistroyBinding

class HistroyActivity : AppCompatActivity() {
    private var binding : ActivityHistroyBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistroyBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarHistoryActivity)
        if(supportActionBar!=null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "HISTORY"
        }
        binding?.toolbarHistoryActivity?.setNavigationOnClickListener{
            onBackPressed()
        }
        val dao = (application as WorkOutApp).db.historyDao()
        getAllCompleteDates(dao)
    }
    private fun getAllCompleteDates(historyDao: HistoryDao){
        lifecycleScope.launch {
            historyDao.fetchALlDates().collect { allCompletedDateList ->
               if(allCompletedDateList.isNotEmpty()){
                    binding?.tvHistory?.visibility = View.VISIBLE
                    binding?.rvHistory?.visibility = View.VISIBLE
                    binding?.tvNoDataAvailable?.visibility = View.INVISIBLE

                   binding?.rvHistory?.layoutManager = LinearLayoutManager(this@HistroyActivity)

                   val dates = ArrayList<String>()
                   for(date in allCompletedDateList){
                       dates.add(date.date)
                   }
                   val historyAdapter = HistoryAdapter(dates)
                   binding?.rvHistory?.adapter = historyAdapter

               }else{
                   binding?.tvHistory?.visibility = View.GONE
                   binding?.rvHistory?.visibility = View.GONE
                   binding?.tvNoDataAvailable?.visibility = View.VISIBLE
               }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}