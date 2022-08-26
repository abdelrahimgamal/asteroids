package com.udacity.asteroidradar.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.ItemAsteroidBinding

class AsteroidsAdapter(val callback: AsteroidClick) : RecyclerView.Adapter<AsteroidsHolder>() {

    /**
     * The asteroids that our Adapter will show
     */
    var asteroids: List<Asteroid> = emptyList()
        set(value) {
            field = value
            // Notify any registered observers that the data set has changed. This will cause every
            // element in our RecyclerView to be invalidated.
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidsHolder {
        val binding: ItemAsteroidBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            AsteroidsHolder.LAYOUT,
            parent,
            false
        )
        return AsteroidsHolder(binding)
    }

    override fun onBindViewHolder(holder: AsteroidsHolder, position: Int) {
        holder.itemAsteroidBinding.also {
            it.asteroid = asteroids[position]
            it.asteroidCallback = callback
        }
    }

    override fun getItemCount(): Int = asteroids.size

}
class AsteroidsHolder(val itemAsteroidBinding: ItemAsteroidBinding) :
    RecyclerView.ViewHolder(itemAsteroidBinding.root) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_asteroid
    }
}

class AsteroidClick(val asteroid: (Asteroid) -> Unit) {

    fun onClick(asteroid: Asteroid) = asteroid(asteroid)
}