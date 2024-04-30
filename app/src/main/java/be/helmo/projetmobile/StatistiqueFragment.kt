package be.helmo.projetmobile

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import be.helmo.projetmobile.databinding.FragmentStatistiqueBinding
import be.helmo.projetmobile.model.Category
import be.helmo.projetmobile.view.CategoryListAdapter
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate

class StatistiqueFragment : HeaderFragment(R.layout.fragment_statistique) {
    private var _binding: FragmentStatistiqueBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentStatistiqueBinding.inflate(inflater, container, false)
        setupBarCharts()
        setupCategoryRecyclerView()
        return binding.root
    }

    private fun setupBarCharts() {
        val monthlyData = getMonthlyData()
        setupBarChart(binding.barChartMonthly, monthlyData.first, monthlyData.second, "Monthly Revenue and Expenses")

        val yearlyData = getYearlyData()
        setupBarChart(binding.barChartYearly, yearlyData.first, yearlyData.second, "Yearly Revenue and Expenses")
    }

    private fun setupBarChart(chart: BarChart, data: List<BarEntry>, labels: List<String>, label: String) {
        val dataSet = BarDataSet(data, label).apply {
            setColors(*ColorTemplate.COLORFUL_COLORS)
            valueTextColor = Color.WHITE
            valueTextSize = 12f
            // Set label for each entry for the legend
            setStackLabels(labels.toTypedArray())
        }

        val barData = BarData(dataSet)
        chart.apply {
            this.data = barData
            description.text = ""
            xAxis.granularity = 1f
            legend.verticalAlignment = com.github.mikephil.charting.components.Legend.LegendVerticalAlignment.BOTTOM
            legend.horizontalAlignment = com.github.mikephil.charting.components.Legend.LegendHorizontalAlignment.CENTER
            legend.orientation = com.github.mikephil.charting.components.Legend.LegendOrientation.VERTICAL
            legend.setDrawInside(false)
            legend.yEntrySpace = 4f
            animateY(1000)
            invalidate()
        }
    }

    private fun getMonthlyData(): Pair<List<BarEntry>, List<String>> {
        val entries = listOf(
            BarEntry(1f, 200f),
            BarEntry(2f, 150f),
            BarEntry(3f, 180f)
        )
        val labels = listOf("Food", "Travel", "Utilities")
        return Pair(entries, labels)
    }

    private fun getYearlyData(): Pair<List<BarEntry>, List<String>> {
        val entries = listOf(
            BarEntry(1f, 1200f),
            BarEntry(2f, 800f),
            BarEntry(3f, 950f)
        )
        val labels = listOf("Rent", "Entertainment", "Groceries")
        return Pair(entries, labels)
    }

    private fun setupCategoryRecyclerView() {
        binding.recyclerViewCategories.layoutManager = LinearLayoutManager(context)
        //binding.recyclerViewCategories.adapter = CategoryListAdapter(getCategories())
    }

    private fun getCategories(): List<Category> {
        // Simulated data for the category list
        return listOf(
            Category(0, "Food", 200.0),
            Category(0, "Travel", 150.0),
            Category(0, "Utilities", 120.0)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
