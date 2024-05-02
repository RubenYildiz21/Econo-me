package be.helmo.projetmobile

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.constraintlayout.motion.utils.Easing
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import be.helmo.projetmobile.database.CategoryRepository
import be.helmo.projetmobile.database.TransactionRepository
import be.helmo.projetmobile.databinding.FragmentHomeBinding
import be.helmo.projetmobile.model.Category
import be.helmo.projetmobile.viewmodel.CurrencyViewModel
import be.helmo.projetmobile.viewmodel.HomeViewModel
import be.helmo.projetmobile.viewmodel.HomeViewModelFactory
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.animation.*
import com.github.mikephil.charting.components.Legend


class HomeFragment : HeaderFragment(R.layout.fragment_home) {
    private var binding: FragmentHomeBinding? = null
    private val viewModel: HomeViewModel by viewModels {
        HomeViewModelFactory(TransactionRepository.get(), CategoryRepository.get(), CurrencyViewModel())
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        setupObserver()
        Log.d("HomeFragment", "Transaction loaded")
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
        val headerButton: ImageButton = view.findViewById(R.id.headerButton)
        headerButton.setOnClickListener{
            setupHeaderButton()
        }
    }

    private fun setupHeaderButton() {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayoutContainer, UserFragment())
            .addToBackStack(null)
            .commit()
    }

    private fun setupObserver() {
        viewModel.revenueData.observe(viewLifecycleOwner) { data ->
            if (data.isNotEmpty()) {
                binding?.let { setupPieChart(it.pieChartRevenus, data, "Revenus") }
            } else {
                Log.d("HomeFragment", "No revenue data available")
            }
        }

        viewModel.expenseData.observe(viewLifecycleOwner) { data ->
            if (data.isNotEmpty()) {
                binding?.let { setupPieChart(it.pieChartDepenses, data, "Dépenses") }
            } else {
                Log.d("HomeFragment", "No expense data available")
            }
        }
    }

    private fun setupPieChart(pieChart: PieChart, data: List<Category>, label: String) {
        val entries = data.map { PieEntry(it.solde.toFloat(), it.nom) }
        val dataSet = PieDataSet(entries, "").apply {
            setColors(*ColorTemplate.MATERIAL_COLORS)
            valueTextColor = Color.WHITE
            valueTextSize = 12f
        }
        Log.d("HomeFragment", "Entries: $entries")
        Log.d("HomeFragment", "DataSet: $dataSet")

        pieChart.data = PieData(dataSet)

        pieChart.apply {
            description.isEnabled = false
            isDrawHoleEnabled = true
            setHoleColor(Color.WHITE)
            holeRadius = 58f
            transparentCircleRadius = 61f
            setEntryLabelTextSize(12f)
            setUsePercentValues(true)
            setDrawEntryLabels(false)

            legend.apply {
                isEnabled = true
                verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
                horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
                orientation = Legend.LegendOrientation.VERTICAL
                setDrawInside(false)
                xEntrySpace = 7f
                yEntrySpace = 0f
                yOffset = 5f
            }

            animateY(1400, com.github.mikephil.charting.animation.Easing.EaseInOutQuad)
            invalidate()
        }
    }

}
