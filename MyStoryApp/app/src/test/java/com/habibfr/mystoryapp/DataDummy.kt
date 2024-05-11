package com.habibfr.mystoryapp

import com.habibfr.mystoryapp.data.remote.response.ListStoryItem

object DataDummy {
    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                i.toString(),
                "author + $i",
                "quote $i",
                "quote $i",
                "quote $i",
            )
            items.add(story)
        }
        return items
    }
}