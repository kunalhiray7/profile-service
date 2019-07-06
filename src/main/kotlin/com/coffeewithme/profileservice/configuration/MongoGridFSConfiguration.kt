package com.coffeewithme.profileservice.configuration

import com.mongodb.MongoClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.AbstractMongoConfiguration
import org.springframework.data.mongodb.gridfs.GridFsTemplate


@Configuration
class MongoGridFSConfiguration : AbstractMongoConfiguration() {

    @Value("\${mongo.database.name}")
    private lateinit var databaseName: String

    @Value("\${mongo.database.host}")
    private lateinit var hostName: String

    override fun mongoClient(): MongoClient = MongoClient(hostName)

    override fun getDatabaseName(): String = databaseName

    @Bean
    fun gridFsTemplate(): GridFsTemplate {
        return GridFsTemplate(mongoDbFactory(), mappingMongoConverter())
    }
}