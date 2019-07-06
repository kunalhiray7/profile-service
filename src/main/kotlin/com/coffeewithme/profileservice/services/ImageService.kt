package com.coffeewithme.profileservice.services

import com.coffeewithme.profileservice.exceptions.ProfileNotFoundException
import com.mongodb.client.gridfs.GridFSBucket
import com.mongodb.client.gridfs.GridFSBuckets
import org.bson.types.ObjectId
import org.springframework.data.mongodb.MongoDbFactory
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.gridfs.GridFsOperations
import org.springframework.data.mongodb.gridfs.GridFsResource
import org.springframework.stereotype.Service
import java.io.InputStream

@Service
class ImageService(private val gridOperations: GridFsOperations, private val mongoDbFactory: MongoDbFactory) {

    fun saveFile(file: InputStream, name: String, contentType: String): String {
        val savedFileId = gridOperations.store(file, name, contentType)
        return savedFileId.toString()
    }

    @Throws(ProfileNotFoundException::class)
    fun get(imageId: String): GridFsResource {
        val image = gridOperations.findOne(Query(Criteria.where("_id").`is`(ObjectId(imageId))))
                ?: throw ProfileNotFoundException("No image found for id $imageId")

        return GridFsResource(image, getGridFs().openDownloadStream(image.id))
    }

    private fun getGridFs(): GridFSBucket {

        val db = mongoDbFactory.db
        return GridFSBuckets.create(db)
    }
}