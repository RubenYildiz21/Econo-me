package be.helmo.projetmobile

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.viewModels
import be.helmo.projetmobile.database.CategoryRepository
import be.helmo.projetmobile.databinding.FragmentStatistiqueBinding
import be.helmo.projetmobile.model.Category
import be.helmo.projetmobile.viewmodel.StatistiqueViewModel
import be.helmo.projetmobile.viewmodel.StatistiqueViewModelFactory
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import java.time.LocalDate

class StatistiqueFragment : HeaderFragment(R.layout.fragment_statistique) {
    private var _binding: FragmentStatistiqueBinding? = null
    private val binding get() = _binding!!

    private val viewModel: StatistiqueViewModel by viewModels {
        StatistiqueViewModelFactory(CategoryRepository.get())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentStatistiqueBinding.inflate(inflater, container, false)
        viewModel.loadCategoriesForCurrentMonth()
        viewModel.loadCategoriesForCurrentYear()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        binding.thisMonth.text = LocalDate.now().month.toString()
        binding.thisYear.text = LocalDate.now().year.toString()
        val headerButton: ImageButton = view.findViewById(be.helmo.projetmobile.R.id.headerButton)
        headerButton.setOnClickListener{
            setupHeaderButton()
        }
    }

    private fun setupObservers() {
        viewModel.categoriesMonth.observe(viewLifecycleOwner) { categories ->
            val data = parseCategoryData(categories)
            setupBarChart(binding.barChartMonthly, data.first, data.second, "Spending by Category - Month")
        }

        viewModel.categoriesYear.observe(viewLifecycleOwner) { categories ->
            val data = parseCategoryData(categories)
            setupBarChart(binding.barChartYearly, data.first, data.second, "Spending by Category - Year")
        }
    }

    private fun parseCategoryData(categories: List<Category>): Pair<List<BarEntry>, List<String>> {
        val entries = mutableListOf<BarEntry>()
        val labels = mutableListOf<String>()
        categories.forEachIndexed { index, category ->
            entries.add(BarEntry(index.toFloat(), category.solde.toFloat()))
            labels.add(category.nom)
        }
        return Pair(entries, labels)
    }

    private fun setupBarChart(chart: BarChart, data: List<BarEntry>, labels: List<String>, label: String) {
        val dataSet = BarDataSet(data, label).apply {
            setColors(*ColorTemplate.COLORFUL_COLORS)
            valueTextColor = Color.WHITE
            valueTextSize = 12f
        }

        val barData = BarData(dataSet)
        chart.apply {
            this.data = barData
            description.text = ""
            xAxis.granularity = 1f
            xAxis.valueFormatter = IndexAxisValueFormatter(labels)
            legend.verticalAlignment = com.github.mikephil.charting.components.Legend.LegendVerticalAlignment.BOTTOM
            legend.horizontalAlignment = com.github.mikephil.charting.components.Legend.LegendHorizontalAlignment.CENTER
            legend.orientation = com.github.mikephil.charting.components.Legend.LegendOrientation.HORIZONTAL
            legend.setDrawInside(false)
            legend.isEnabled = false
            animateY(1000)
            invalidate()
        }
    }

    private fun setupHeaderButton() {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayoutContainer, UserFragment())
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
