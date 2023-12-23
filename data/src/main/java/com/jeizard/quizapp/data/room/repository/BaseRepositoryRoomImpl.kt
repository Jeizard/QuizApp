package com.jeizard.quizapp.data.room.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.jeizard.domain.repository.BaseRepository
import com.jeizard.quizapp.data.mapper.Mapper
import com.jeizard.quizapp.data.room.dao.BaseDao

abstract class BaseRepositoryRoomImpl<DBEntity, DAO : BaseDao<DBEntity>, Entity>(
    val dao: DAO,
    val mapper: Mapper<DBEntity, Entity>
) : BaseRepository<Entity> {

    private var allItems: List<DBEntity> = emptyList()
    private val listeners: MutableSet<BaseRepository.OnDataChangedListener<Entity>> = HashSet()

    init {
        GlobalScope.launch(Dispatchers.IO) {
            allItems = withContext(Dispatchers.IO) { dao.getAll() }
            notifyChanges()
        }
    }

    override fun insert(item: Entity) {
        GlobalScope.launch(Dispatchers.IO) {
            dao.insert(mapper.mapToDBEntity(item))
            allItems = withContext(Dispatchers.IO) { dao.getAll() }
            notifyChanges()
        }
    }

    override fun update(item: Entity) {
        GlobalScope.launch(Dispatchers.IO) {
            dao.update(mapper.mapToDBEntity(item))
            allItems = withContext(Dispatchers.IO) { dao.getAll() }
            notifyChanges()
        }
    }

    override fun delete(item: Entity) {
        GlobalScope.launch(Dispatchers.IO) {
            dao.delete(mapper.mapToDBEntity(item))
            allItems = withContext(Dispatchers.IO) { dao.getAll() }
            notifyChanges()
        }
    }

    override fun deleteAll() {
        GlobalScope.launch(Dispatchers.IO) {
            dao.deleteAll()
            allItems = withContext(Dispatchers.IO) { dao.getAll() }
            notifyChanges()
        }
    }

    override fun getAll(): List<Entity> {
        return mapper.mapFromDBEntity(allItems)
    }

    override fun addListener(listener: BaseRepository.OnDataChangedListener<Entity>) {
        listeners.add(listener)
        listener.onChanged(mapper.mapFromDBEntity(allItems))
    }

    override fun removeListener(listener: BaseRepository.OnDataChangedListener<Entity>) {
        listeners.remove(listener)
    }

    private fun notifyChanges() {
        for (listener in listeners) {
            listener.onChanged(mapper.mapFromDBEntity(allItems))
        }
    }
}
