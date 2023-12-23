package com.jeizard.domain.repository

interface BaseRepository<Entity> {
    fun insert(item: Entity)
    fun update(item: Entity)
    fun delete(item: Entity)
    fun deleteAll()
    fun getAll(): List<Entity>

    interface OnDataChangedListener<Entity> {
        fun onChanged(items: List<Entity>)
    }

    fun addListener(listener: OnDataChangedListener<Entity>)
    fun removeListener(listener: OnDataChangedListener<Entity>)
}
