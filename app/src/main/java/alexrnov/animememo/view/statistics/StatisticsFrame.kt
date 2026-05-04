package alexrnov.animememo.view.statistics

import alexrnov.animememo.R
import alexrnov.animememo.database.statistics.StatisticsEntity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class StatisticsFragment : Fragment() {
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		val statisticsViewModel = ViewModelProvider(requireActivity()).get(StatisticsViewModel::class.java)

		val root = inflater.inflate(R.layout.fragment_statistics, container, false)
		val recyclerView = root.findViewById<RecyclerView>(R.id.recyclerView)

		recyclerView.layoutManager = LinearLayoutManager(context)
		val adapter = StatisticsAdapter()
		recyclerView.adapter = adapter

		statisticsViewModel.texts.observe(viewLifecycleOwner) {
			adapter.submitList(it)
		}
		return root
	}
}

class StatisticsAdapter :
	ListAdapter<StatisticsEntity, StatisticsViewHolder>(object : DiffUtil.ItemCallback<StatisticsEntity>() {
		override fun areItemsTheSame(oldItem: StatisticsEntity, newItem: StatisticsEntity): Boolean =
			oldItem == newItem

		override fun areContentsTheSame(oldItem: StatisticsEntity, newItem: StatisticsEntity): Boolean =
			oldItem == newItem
	}) {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatisticsViewHolder {
		val view = LayoutInflater.from(parent.context)
			.inflate(R.layout.item_statistics, parent, false)
		return StatisticsViewHolder(view)
	}

	override fun onBindViewHolder(holder: StatisticsViewHolder, position: Int) {
		holder.statisticsDate.text = getItem(position).date
		holder.statisticsCards.text = getItem(position).cardsQuantity.toString()
		holder.statisticsErrors.text = getItem(position).errors.toString()
	}
}

class StatisticsViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
	val statisticsDate: TextView = itemView.findViewById(R.id.statisticsDate)
	val statisticsCards: TextView = itemView.findViewById(R.id.statisticsCards)
	val statisticsErrors: TextView = itemView.findViewById(R.id.statisticsErrors)
}