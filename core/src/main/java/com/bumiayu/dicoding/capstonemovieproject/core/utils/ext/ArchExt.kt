package com.bumiayu.dicoding.capstonemovieproject.core.utils.ext

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData

fun <T> LifecycleOwner.observe(liveData: LiveData<T>?, action: (t: T) -> Unit, owner: LifecycleOwner = this) {
    liveData?.observe(owner,
        {it?.let { t -> action(t) }}
    )
}