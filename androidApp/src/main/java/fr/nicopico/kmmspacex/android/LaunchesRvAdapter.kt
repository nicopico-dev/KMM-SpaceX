package fr.nicopico.kmmspacex.android

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import fr.nicopico.kmmspacex.android.databinding.ItemLaunchBinding
import fr.nicopico.kmmspacex.shared.entity.RocketLaunch

class LaunchesRvAdapter(
    var launches: List<RocketLaunch>
) : RecyclerView.Adapter<LaunchesRvAdapter.LaunchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LaunchViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLaunchBinding.inflate(inflater, parent, false)
        return LaunchViewHolder(binding)
    }

    override fun getItemCount(): Int = launches.count()

    override fun onBindViewHolder(holder: LaunchViewHolder, position: Int) {
        holder.bindData(launches[position])
    }

    inner class LaunchViewHolder(
        private val binding: ItemLaunchBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindData(launch: RocketLaunch) {
            val ctx = itemView.context

            with(binding) {
                missionName.text = ctx.getString(R.string.mission_name_field, launch.missionName)
                launchYear.text = ctx.getString(R.string.launch_year_field, launch.launchYear.toString())
                details.text = ctx.getString(R.string.details_field, launch.details ?: "")
                when (launch.launchSuccess) {
                    true -> launchSuccess.also {
                        it.text = ctx.getString(R.string.successful)
                        it.setTextColor(ContextCompat.getColor(ctx, R.color.colorSuccessful))
                    }
                    false -> launchSuccess.also {
                        it.text = ctx.getString(R.string.unsuccessful)
                        it.setTextColor(ContextCompat.getColor(ctx, R.color.colorUnsuccessful))
                    }
                    null -> launchSuccess.also {
                        it.text = ctx.getString(R.string.no_data)
                        it.setTextColor(ContextCompat.getColor(ctx, R.color.colorNoData))
                    }
                }
            }
        }
    }
}