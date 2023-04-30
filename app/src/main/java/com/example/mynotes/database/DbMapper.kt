package com.example.mynotes.database

import com.example.mynotes.domain.model.*

class DbMapper {
    // Create list of NoteModels by pairing each note with a color
    fun mapPhones(
        phoneDbModels: List<PhoneDbModel>,
        colorDbModels: Map<Long, ColorDbModel>,
        tagDbModels: Map<Long, TagDbModel>
    ): List<PhoneModel> = phoneDbModels.map {
        val colorDbModel = colorDbModels[it.colorId]
            ?: throw RuntimeException("Color for colorId: ${it.colorId} was not found. Make sure that all colors are passed to this method")
        val tagDbModel = tagDbModels[it.tagId]
            ?: throw RuntimeException("Tag for tagId: ${it.tagId} was not found. Make sure that all tags are passed to this method")
        mapPhone(it, colorDbModel, tagDbModel)
    }

    // convert NoteDbModel to NoteModel
    fun mapPhone(phoneDbModel: PhoneDbModel, colorDbModel: ColorDbModel, tagDbModel: TagDbModel): PhoneModel {
        val color = mapColor(colorDbModel)
        val tag = mapTag(tagDbModel)
        return with(phoneDbModel) { PhoneModel(id, name, phoneNum, color, tag) }
    }

    // convert list of ColorDdModels to list of ColorModels
    fun mapColors(colorDbModels: List<ColorDbModel>): List<ColorModel> =
        colorDbModels.map { mapColor(it) }

    // convert ColorDbModel to ColorModel
    fun mapColor(colorDbModel: ColorDbModel): ColorModel =
        with(colorDbModel) { ColorModel(id, name, hex) }

    // convert list of ColorDdModels to list of ColorModels
    fun mapTags(tagDbModels: List<TagDbModel>): List<TagModel> =
        tagDbModels.map { mapTag(it) }

    // convert ColorDbModel to ColorModel
    fun mapTag(tagDbModel: TagDbModel): TagModel =
        with(tagDbModel) { TagModel(id, tagName) }

    // convert NoteModel back to NoteDbModel
    fun mapDbPhone(phone: PhoneModel): PhoneDbModel =
        with(phone) {
            if (id == NEW_PHONE_ID)
                PhoneDbModel(
                    name = name,
                    phoneNum = phoneNum,
                    colorId = color.id,
                    tagId = tag.id,
                    isInTrash = false
                )
            else
                PhoneDbModel(id, name, phoneNum, color.id, tag.id, false)
        }
}