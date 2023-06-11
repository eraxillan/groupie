package com.xwray.groupie.databinding

import androidx.viewbinding.ViewBinding
import com.xwray.groupie.GroupieViewHolder1

public class GroupieViewHolder1<T: ViewBinding>(binding: T) : GroupieViewHolder1(binding.root) {
    val binding: T

    init {
        this.binding = binding
    }
}