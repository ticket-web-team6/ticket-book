package com.team6.ticketbook.domain.show.service

import com.team6.ticketbook.domain.show.dto.ShowResponse
import com.team6.ticketbook.domain.show.dto.TopSearchKeyword
import com.team6.ticketbook.domain.show.repository.ShowRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.redis.core.HashOperations
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ZSetOperations
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class SearchService(
    private val redisTemplate: RedisTemplate<String, String>,
    private val redisTemplateForShowResponse: RedisTemplate<String, Page<ShowResponse>>,
    private val showRepository: ShowRepository
) {
    private val topSearchZSet: ZSetOperations<String, String> = redisTemplate.opsForZSet()
    private val searchV2Hash : HashOperations<String,String,Page<ShowResponse>> = redisTemplateForShowResponse.opsForHash()
    private val pageRequest = PageRequest.of(0, 10)

    fun topSearch10(limit: Long): Set<TopSearchKeyword> {
        val rangeWithScores = topSearchZSet.reverseRangeWithScores("topSearch", 0, limit - 1)
        if (rangeWithScores != null) {
            return rangeWithScores.map { TopSearchKeyword(it.value, it.score) }.toSet()
        }
        return emptySet()
    }

    fun searchByDB(term:String):Page<ShowResponse>{
        addTermTopSearch(term)
        val showInfos = showRepository.findByTitlePaginated(term, pageRequest)
        if(showInfos.hasContent()){
            return showInfos.map{ShowResponse.from(it) }
        }
        return Page.empty(pageRequest)
    }

    fun searchByRedis(term: String): Page<ShowResponse> {
        addTermTopSearch(term)
        val cachedData = searchV2Hash.get("searchList",term)
        if (cachedData != null) {
            return cachedData
        } else {
            val showInfos = showRepository.findByTitlePaginated(term, pageRequest)
            if (showInfos.hasContent()) {
                addTermSearchV2(term, showInfos.map{ShowResponse.from(it) })
                return showInfos.map{ShowResponse.from(it) }
            }
        }
        return Page.empty(pageRequest)
    }

    fun addTermTopSearch(term: String) {
        topSearchZSet.incrementScore("topSearch", term, 1.0)
        redisTemplate.expire(term, 10, TimeUnit.MINUTES)
    }

    fun addTermSearchV2(term: String, showInfos: Page<ShowResponse>) {
        searchV2Hash.put("searchList", term, showInfos)
        redisTemplateForShowResponse.expire(term, 10, TimeUnit.MINUTES)
    }
}