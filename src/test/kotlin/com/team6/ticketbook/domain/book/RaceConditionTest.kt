package com.team6.ticketbook.domain.book

import com.team6.ticketbook.domain.book.dto.CreateBookRequest
import com.team6.ticketbook.domain.book.repository.BookRepository
import com.team6.ticketbook.domain.book.service.BookService
import com.team6.ticketbook.domain.member.model.Member
import com.team6.ticketbook.domain.member.repository.MemberRepository
import com.team6.ticketbook.domain.place.model.Place
import com.team6.ticketbook.domain.place.repository.PlaceRepository
import com.team6.ticketbook.domain.seat.model.Seat
import com.team6.ticketbook.domain.seat.repository.SeatRepository
import com.team6.ticketbook.domain.show.model.Show
import com.team6.ticketbook.domain.show.model.ShowStartTime
import com.team6.ticketbook.domain.show.repository.ShowRepository
import com.team6.ticketbook.infra.queryDSL.QueryDslConfig
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDate
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger
import kotlin.test.Test

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(value = [QueryDslConfig::class])
@ActiveProfiles("test")
class RaceConditionTest @Autowired constructor(
    private val bookRepository: BookRepository,
    private val seatRepository: SeatRepository,
    private val showRepository: ShowRepository,
    private val placeRepository: PlaceRepository,
    private val memberRepository: MemberRepository

) {
    private val bookService = BookService(
        bookRepository = bookRepository,
        seatRepository = seatRepository,
        showRepository = showRepository
    )

    @Test
    fun testRaceCondition() {
        // given
        val executeNumber = 20
        val executorService = Executors.newFixedThreadPool(10)
        val countDownLatch = CountDownLatch(executeNumber)

        val successCount = AtomicInteger(0)
        val failCount = AtomicInteger(0)

        val place = Place(
            name = "떡잎유치원",
            address = "떡잎마을"
        ).let { executorService.submit<Place> { placeRepository.save(it) }.get() }

        val seat = Seat(
            placeId = place.id!!,
            seatCode = "A1",
            grade = "VIP"
        ).let { executorService.submit<Seat> { seatRepository.save(it) }.get() }

        val show = Show(
            place = place,
            title = "액션가면",
            vipPrice = 100000,
            rPrice = 80000,
            sPrice = 60000,
            aPrice = 40000,
            startDate = LocalDate.of(2024, 7, 10),
            endDate = LocalDate.of(2024, 7, 11),
            mainImageUrl = "test",
            info = "info",
            category = "애니메이션",
            startTime = ShowStartTime.DAY
        ).let { executorService.submit<Show> { showRepository.save(it) }.get() }


        val member = Member(
            email = "짱구",
            password = "짱아",
            name = "봉미선",
            address = "신형만",
            phoneNumber = "훈이철수유리맹구"
        ).let { executorService.submit<Member> { memberRepository.save(it) }.get() }

        val req = CreateBookRequest(
            showId = show.id!!,
            seatId = seat.id!!,
            date = LocalDate.of(2024, 7, 10),
        )

        // when
        for (i in 0 until executeNumber) {
            executorService.execute {
                try {
                    bookService.createBook(member.id!!, req)
                    successCount.incrementAndGet()
                } catch (e: Exception) {
                    failCount.incrementAndGet()
                    println(e.message)
                }
                countDownLatch.countDown()
            }
        }
        countDownLatch.await()


        // then
        successCount.get() + failCount.get() shouldBe 20

        bookRepository.findAllByShowIdAndDateAndSeatId(
            req.showId,
            req.date,
            req.seatId
        ).size shouldBe successCount.get()

        successCount.get() shouldNotBe 1

        println("예약 성공 횟수 : ${successCount.get()}")
        println("실패 횟수: ${failCount.get()}")

    }
}