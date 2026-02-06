# Coding Standards

## Common Package 필수 사용 규칙

모든 구현 시 `common` 패키지의 다음 요소들을 **필수적으로** 사용해야 합니다.

## 1. 응답 처리 (ApiResponse)

### 규칙
- 모든 API 응답은 `ResponseEntity<ApiResponse<T>>` 형식을 사용합니다.
- 성공 시 `ApiResponse.success(result)` 또는 `SuccessCode`를 지정하여 반환합니다.

### 예시
```kotlin
// Controller에서 응답 반환
@GetMapping("/universities")
fun getUniversities(): ResponseEntity<ApiResponse<List<UniversityResponse>>> {
    val universities = universityService.getUniversities()
    return ApiResponse.success(universities)
}

// SuccessCode를 지정하는 경우
return ApiResponse.success(SuccessCode.CREATED, result)
```

### 응답 구조
```json
{
  "code": "200",
  "message": "Success",
  "result": { ... }
}
```

## 2. 예외 처리 (Exception)

### 규칙
- 비즈니스 로직 오류 발생 시 `BusinessException(ErrorCode.XXX)`을 던집니다.
- **직접 ResponseEntity의 에러 본문을 생성하지 마십시오.**
- `ErrorCode`에 상수를 등록한 뒤 예외를 던져 핸들러가 처리하게 합니다.
- `ClientExceptionHandler`에 `BusinessException` 핸들러가 포함되어야 함을 인지하십시오.

### 예시
```kotlin
// Service에서 예외 던지기
fun getUniversityById(id: Long): University {
    return universityReader.findById(id)
        ?: throw BusinessException(ErrorCode.UNIVERSITY_NOT_FOUND)
}

// ErrorCode 정의 예시
enum class ErrorCode(
    val code: String,
    val message: String,
    val httpStatus: HttpStatus
) {
    UNIVERSITY_NOT_FOUND("UNIVERSITY_NOT_FOUND", "대학교를 찾을 수 없습니다.", HttpStatus.NOT_FOUND)
}
```

## 3. 엔티티 공통 필드 (BaseEntity)

### 규칙
- 모든 JPA Entity는 반드시 `BaseEntity`를 상속받습니다.
- `createdTime`, `updatedTime`은 JPA Auditing에 의해 자동 관리되므로, **코드에서 직접 값을 할당하거나 세팅하지 마십시오.**

### 예시
```kotlin
@Entity
@Table(name = "university")
class UniversityEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    var name: String,

    // ... 기타 필드
) : BaseEntity()  // BaseEntity 상속 필수
```

## Agent Task Checklist

새로운 기능 구현 시 반드시 확인해야 할 체크리스트:

- [ ] 새로운 도메인 생성 시 4개의 패키지(application, business, implement, storage) 기준으로 생성했는가?
- [ ] Service에서 Repository 구현체를 직접 의존하지 않고 implement 레이어의 인터페이스를 거치는가?
- [ ] 모든 응답이 `ApiResponse`로 감싸져 있는가?
- [ ] 비즈니스 로직 실패 시 `BusinessException`과 `ErrorCode`를 적절히 사용했는가?
- [ ] 모든 Entity가 `BaseEntity`를 상속받는가?
- [ ] 계층 간 의존성이 단방향(Application → Business → Implement → Storage)으로만 흐르는가?

## Kotlin 코드 스타일

- **Nullable 처리**: Kotlin의 `?`와 `?.`, `?:` 연산자 적극 활용
- **Data Class**: DTO는 `data class` 사용
- **Named Arguments**: 가독성을 위해 named arguments 사용 권장
- **Companion Object**: 정적 팩토리 메서드는 companion object에 정의
