package org.example.beyondubackend.domain.university.unit

import org.assertj.core.api.Assertions.assertThatThrownBy
import org.example.beyondubackend.common.code.ErrorCode
import org.example.beyondubackend.common.exception.BusinessException
import org.example.beyondubackend.domain.languagerequirement.storage.LanguageRequirementJpaRepository
import org.example.beyondubackend.domain.university.implement.UniversityReader
import org.example.beyondubackend.domain.university.implement.UniversityRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class UniversityReaderTest {
    @Mock
    private lateinit var universityRepository: UniversityRepository

    @Mock
    private lateinit var languageRequirementJpaRepository: LanguageRequirementJpaRepository

    @InjectMocks
    private lateinit var universityReader: UniversityReader

    @Test
    fun `존재하지 않는 ID로 조회 시 UNIVERSITY_NOT_FOUND BusinessException이 발생한다`() {
        given(universityRepository.findById(999L)).willReturn(null)

        assertThatThrownBy { universityReader.getUniversityById(999L) }
            .isInstanceOf(BusinessException::class.java)
            .hasMessageContaining(ErrorCode.UNIVERSITY_NOT_FOUND.message)
    }

    @Test
    fun `존재하는 ID로 조회 시 University 도메인 객체가 반환된다`() {
        val university =
            org.example.beyondubackend.domain.university.implement.University(
                id = 1L,
                semester = "2024-1",
                region = "미주",
                nation = "USA",
                nameKor = "하버드대학교",
                nameEng = "Harvard University",
                minGpa = 3.0,
                remark = "특이사항 없음",
                isExchange = true,
                isVisit = false,
            )
        given(universityRepository.findById(1L)).willReturn(university)

        val result = universityReader.getUniversityById(1L)

        org.assertj.core.api.Assertions
            .assertThat(result.id)
            .isEqualTo(1L)
        org.assertj.core.api.Assertions
            .assertThat(result.nameKor)
            .isEqualTo("하버드대학교")
    }
}
