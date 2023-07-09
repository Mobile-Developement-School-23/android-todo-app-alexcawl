package org.alexcawl.todoapp.domain.repository

interface IRevisionRepository {
    fun getRevision(): Int

    fun setRevision(revision: Int)
}