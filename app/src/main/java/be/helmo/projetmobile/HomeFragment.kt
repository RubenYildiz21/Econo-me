package be.helmo.projetmobile

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import be.helmo.projetmobile.databinding.FragmentHomeBinding
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate


class HomeFragment : HeaderFragment(R.layout.fragment_home) {
    private var binding: FragmentHomeBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)

        setupPieCharts(binding)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


    private fun setupPieCharts(binding: FragmentHomeBinding) {
        setupPieChartRevenus(binding.pieChartRevenus)
        setupPieChartDepenses(binding.pieChartDepenses)
    }

    private fun setupPieChartRevenus(pieChart: PieChart) {
        val entries: MutableList<PieEntry> = ArrayList()
        entries.add(PieEntry(50f))
        entries.add(PieEntry(30f))
        val dataSet = PieDataSet(entries, "Revenus")
        dataSet.setColors(*ColorTemplate.MATERIAL_COLORS)
        val pieData = PieData(dataSet)
        pieChart.setData(pieData)
        pieChart.description = null
        pieChart.isDrawHoleEnabled = true
        pieChart.setHoleColor(Color.WHITE)
        pieChart.transparentCircleRadius = 61f

        // Ajouter une animation
        pieChart.animateY(1400, Easing.EaseInOutQuad)
        pieChart.invalidate() // refresh le graphique
    }

    private fun setupPieChartDepenses(pieChart: PieChart) {
        val entries: MutableList<PieEntry> = ArrayList()
        entries.add(PieEntry(70f))
        entries.add(PieEntry(20f))
        val dataSet = PieDataSet(entries, "Dépenses")
        dataSet.setColors(*ColorTemplate.JOYFUL_COLORS)
        val pieData = PieData(dataSet)
        pieChart.setData(pieData)
        pieChart.description = null
        pieChart.isDrawHoleEnabled = true
        pieChart.setHoleColor(Color.WHITE)
        pieChart.transparentCircleRadius = 61f

        // Ajouter une animation
        pieChart.animateY(1400, Easing.EaseInOutQuad)
        pieChart.invalidate() // refresh le graphique
    }

}
