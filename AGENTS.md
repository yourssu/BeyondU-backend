📄 AGENTS.md (Beyond U - Final Instruction)
이 파일은 Beyond U 프로젝트를 수행하는 LLM 에이전트를 위한 통합 가이드라인입니다. 모든 코드 생성 및 수정 시 아래의 아키텍처, 기술 스택, 공통 규약을 엄격히 준수하십시오.

🚀 1. Project Overview
프로젝트명: Beyond U (교환학생 파견 준비 관리 플랫폼)

핵심 목표: 국가/대학별 지원 자격 필터링, 준비 로드맵 관리.

아키텍처: DDD 기반 4-Layered Architecture (Application - Business - Implement - Storage).

🏗 2. Architecture & Directory Structure
모든 도메인은 domain/{domain_name} 아래에 위치하며, 계층 간 의존성은 단방향(Application -> Business -> Implement -> Storage)으로만 흐릅니다.

src/main/kotlin/com/beyond_u/
├── common/                # [공통] 전역 유틸리티, 공통 예외, 표준 응답
└── domain/
└── {domain_name}/     # 예: university, member, program 등
├── application/   # [응용] API 진입점, 요청/응답 변환
│   ├── dto/       # Web Request/Response DTO
│   └── {Domain}Controller.kt
├── business/      # [서비스] 유즈케이스 조립, 트랜잭션 관리
│   ├── dto/       # 레이어 간 데이터 전송용 DTO
│   └── {Domain}Service.kt
├── implement/     # [구현] 도메인 핵심 로직 및 인터페이스
│   ├── dto/       # 구현 계층 전용 데이터 객체
│   ├── exception/ # 도메인 전용 BusinessException
│   ├── {Domain}Reader.kt / {Domain}Writer.kt (기능별 컴포넌트)
│   ├── {Domain}Repository.kt (Interface)
│   └── {Domain}.kt (Domain Model - 비즈니스 로직 포함)
└── storage/       # [영속성] 데이터베이스 상세 구현
├── {Domain}Entity.kt
└── {Domain}RepositoryImpl.kt (QueryDSL 및 JPA 구현체)
🛠 3. Technology Stack & Environment
Language: Kotlin 1.9.25 (JDK 21)

Framework: Spring Boot 3.4.1

Database: MySQL 8.0+ (모든 환경 공통)

Local: 로컬 MySQL (Workbench 사용)

Prod: AWS RDS (MySQL)

ORM/Query: Spring Data JPA + QueryDSL

Testing: 테스트 코드를 작성하지 않음. (모든 검증은 실제 구동으로 대체)

📋 4. Standardized Coding Rules (Common Package)
에이전트는 모든 구현 시 common 패키지의 다음 요소들을 필수적으로 사용해야 합니다.

4.1. 응답 처리 (ApiResponse)
모든 API 응답은 ResponseEntity<ApiResponse<T>> 형식을 사용합니다.

성공 시 ApiResponse.success(result) 또는 SuccessCode를 지정하여 반환합니다.

4.2. 예외 처리 (Exception)
비즈니스 로직 오류 발생 시 BusinessException(ErrorCode.XXX)을 던집니다.

직접 ResponseEntity의 에러 본문을 생성하지 말고, ErrorCode에 상수를 등록한 뒤 예외를 던져 핸들러가 처리하게 합니다.

추가 규칙: ClientExceptionHandler에 BusinessException 핸들러가 포함되어야 함을 인지하십시오.

4.3. 엔티티 공통 필드 (BaseEntity)
모든 JPA Entity는 반드시 BaseEntity를 상속받습니다.

createdTime, updatedTime은 JPA Auditing에 의해 자동 관리되므로, 코드에서 직접 값을 할당하거나 세팅하지 마십시오.

⚠️ 5. Agent Task Checklist
[ ] 새로운 도메인 생성 시 4개의 패키지(application, business, implement, storage)기준으로 생성했는가?

[ ] Service에서 Repository 구현체를 직접 의존하지 않고 implement 레이어의 인터페이스를 거치는가?

[ ] 모든 응답이 ApiResponse로 감싸져 있는가?

[ ] 비즈니스 로직 실패 시 BusinessException과 ErrorCode를 적절히 사용했는가?
