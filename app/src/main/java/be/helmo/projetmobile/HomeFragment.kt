package be.helmo.projetmobile

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import be.helmo.projetmobile.databinding.FragmentHomeBinding
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
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
        val entries = mutableListOf(
            PieEntry(50f, "Catégorie 1"),
            PieEntry(30f, "Catégorie 2")
        )

        val dataSet = PieDataSet(entries, "Revenus").apply { // Laissez le label vide si vous ne voulez pas de titre sur la légende
            setColors(*ColorTemplate.MATERIAL_COLORS)
            valueFormatter = PercentFormatter(pieChart)
            setDrawValues(false)
        }

        val pieData = PieData(dataSet).apply {
            setValueTextSize(12f)
            setValueTextColor(Color.WHITE)
        }

        pieChart.apply {
            data = pieData
            description.text = ""
            isDrawHoleEnabled = true
            setHoleColor(Color.WHITE)
            setUsePercentValues(true)
            setDrawEntryLabels(false) // Ne pas dessiner les labels des tranches
            legend.isEnabled = true
            legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
            legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
            legend.orientation = Legend.LegendOrientation.VERTICAL
            legend.setDrawInside(false)
            animateY(1400, Easing.EaseInOutQuad)
            invalidate()
        }
    }



    private fun setupPieChartDepenses(pieChart: PieChart) {
        val entries = mutableListOf(
            PieEntry(70f, "Catégorie A"),
            PieEntry(20f, "Catégorie B")
        )

        val dataSet = PieDataSet(entries, "").apply { // Laissez le label vide si vous ne voulez pas de titre sur la légende
            setColors(*ColorTemplate.JOYFUL_COLORS)
            valueFormatter = PercentFormatter(pieChart) // Assurez-vous que les pourcentages sont formatés correctement
            setDrawValues(false) // Ne pas dessiner les valeurs à l'intérieur des tranches
        }

        val pieData = PieData(dataSet).apply {
            setValueTextSize(12f)
            setValueTextColor(Color.WHITE)
        }

        pieChart.apply {
            data = pieData
            description.text = ""
            isDrawHoleEnabled = true
            setHoleColor(Color.WHITE)
            setUsePercentValues(true)
            setDrawEntryLabels(false) // Ne pas dessiner les labels des tranches
            legend.isEnabled = true
            legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
            legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
            legend.orientation = Legend.LegendOrientation.VERTICAL
            legend.setDrawInside(false)
            animateY(1400, Easing.EaseInOutQuad)
            invalidate()
        }
    }



}
