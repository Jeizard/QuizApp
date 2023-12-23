package com.jeizard.quizapp.data.mapper

interface Mapper<D, E> {
    fun mapFromDBEntity(d: D): E

    fun mapToDBEntity(e: E): D

    fun mapFromDBEntity(dCollection: Collection<D>): List<E>

    fun mapToDBEntity(eCollection: Collection<E>): List<D>
}
