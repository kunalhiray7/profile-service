package com.coffeewithme.profileservice.services

import com.coffeewithme.profileservice.exceptions.ProfileNotFoundException
import com.mongodb.client.MongoDatabase
import com.mongodb.client.gridfs.GridFSBucket
import com.mongodb.client.gridfs.GridFSBuckets
import com.mongodb.client.gridfs.model.GridFSFile
import com.nhaarman.mockito_kotlin.*
import org.bson.BsonObjectId
import org.bson.types.ObjectId
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.data.mongodb.MongoDbFactory
import org.springframework.data.mongodb.gridfs.GridFsOperations
import java.io.InputStream
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class ImageServiceTest {

    @Mock
    private lateinit var gridOperations: GridFsOperations

    @Mock
    private lateinit var mongoDbFactory: MongoDbFactory

    @InjectMocks
    private lateinit var imageService: ImageService

    @Test
    fun `saveFile() should save file into gridFS`() {
        // given
        val imageId = ObjectId("5d1e4a25ba52df864cc09028")
        val fileName = "dummy_image.jpg"
        val contentType = "image/png"
        val file: InputStream = this.javaClass.classLoader.getResourceAsStream(fileName)
        doReturn(imageId).`when`(gridOperations).store(file, fileName, contentType)

        // when
        val result = imageService.saveFile(file, fileName, contentType)

        // then
        assertEquals(imageId.toString(), result)
        verify(gridOperations, times(1)).store(file, fileName, contentType)
        verifyNoMoreInteractions(gridOperations)
    }

    fun `get() should return the gridFS file for given id`() {
        // given
        val imageId = "5d1e4a25ba52df864cc09028"
        val db = Mockito.mock(MongoDatabase::class.java)
        val gfs = Mockito.mock(GridFSBuckets::class.java)
        val bucket = Mockito.mock(GridFSBucket::class.java)
        val gridFsFile = GridFSFile(BsonObjectId(), "dummy_image.jpg", 345L, 3, Date(), null, null, null)
//        val gridFsResource = GridFsResource(gridFsFile, GridFSBuckets.create(db).openDownloadStream(gridFsFile.id))

        doReturn(db).`when`(mongoDbFactory).db
//        doReturn(bucket).`when`(gfs).create(db)
        doReturn(gridFsFile).`when`(gridOperations).findOne(any())

        // when
        imageService.get(imageId)

        // then
        verify(gridOperations).findOne(any())
    }

    @Test
    fun `get() should throw ProfileNotFoundException when no file found for given id`() {
        // given
        val imageId = "5d1e4a25ba52df864cc09028"
        doReturn(null).`when`(gridOperations).findOne(any())

        // when
        try {
            imageService.get(imageId)
            fail()
        } catch (e: ProfileNotFoundException) {
            assertEquals("No image found for id $imageId", e.message)
        }

        // then
        verify(gridOperations).findOne(any())
    }
}