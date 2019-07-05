package com.coffeewithme.profileservice.utils

import com.coffeewithme.profileservice.exceptions.UnprocessableEntityException
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.fge.jsonpatch.JsonPatch
import org.springframework.stereotype.Component
import javax.validation.Valid
import javax.validation.constraints.NotNull

@Component
class JsonPatcher(private val mapper: ObjectMapper) {

    fun <T : Any> patch(json: String, @NotNull @Valid target: T): T {
        val patchedNode: JsonNode?
        try {
            val patch = mapper.readValue(json, JsonPatch::class.java)
            patchedNode = patch.apply(mapper.convertValue(target, JsonNode::class.java))
        } catch (e: Throwable) {
            throw UnprocessableEntityException("Request body is invalid and cannot be processed: ${e.message}")
        }

        return mapper.convertValue(patchedNode, target.javaClass)
    }
}