package com.test.fitnessstudios.core.testing.data.dao

import com.test.fitnessstudios.core.database.FitnessStudio
import com.test.fitnessstudios.core.database.FitnessStudioDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeFitnessStuioDao : FitnessStudioDao {
    private val fakeRoomDB = mutableMapOf<String, FitnessStudio>()

    override fun getFitnessStudios(): Flow<List<FitnessStudio>> = flow {
        emit(fakeRoomDB.values.toList())
    }

    override fun getFitnessStudio(uid: String): Flow<FitnessStudio> {
        TODO("Not yet implemented")
    }

    override suspend fun insertFitnessStudio(item: FitnessStudio) {
        fakeRoomDB.put(item.uid, item)
    }

    override suspend fun deleteFitnessStudio(item: FitnessStudio) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteById(id: String) {
        TODO("Not yet implemented")
    }

    override fun itemExistsByName(name: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun itemExistsById(id: String): Flow<Boolean> {
        TODO("Not yet implemented")
    }

    override fun getItemById(id: String): Flow<FitnessStudio> {
        TODO("Not yet implemented")
    }

    override suspend fun nuke() {
        TODO("Not yet implemented")
    }
}

/*
    override fun getMyModels(): Flow<List<MyModel>> = flow {
        emit(data)
    }

    override suspend fun insertMyModel(item: MyModel) {
        data.add(0, item)
    }
 */
