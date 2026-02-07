# Architecture Guide

## Project Overview

**프로젝트명**: Beyond U (교환학생 파견 준비 관리 플랫폼)

**핵심 목표**: 국가/대학별 지원 자격 필터링, 준비 로드맵 관리

**아키텍처**: DDD 기반 4-Layered Architecture (Application - Business - Implement - Storage)

## 4-Layered Architecture

모든 도메인은 `domain/{domain_name}` 아래에 위치하며, 계층 간 의존성은 단방향(Application → Business → Implement → Storage)으로만 흐릅니다.
계층간 의존은 바로 인접한 계층만 가능합니다.
Application -> Implement ❌
Business -> Storage ❌
```
src/main/kotlin/com/beyond_u/
├── common/                # [공통] 전역 유틸리티, 공통 예외, 표준 응답
└── domain/
    └── {domain_name}/     # 예: university, member, program 등
        ├── application/   # [응용] API 진입점, 요청/응답 변환
        │   ├── dto/       # Web Request/Response DTO
        │   └── {Domain}Controller.kt
        ├── business/      # [Service] 유즈케이스 조립, 트랜잭션
        │   ├── query/     # 필터링/조회용 파라미터 객체 (Query DTO)
        │   ├── dto/       # 서비스 결과 반환용 DTO (Response DTO)
        │   └── {Domain}Service.kt
        ├── implement/     # [Core] 비즈니스 행위의 단위 구현
        │   ├── {Domain}Reader.kt # Read 전용 컴포넌트
        │   ├── {Domain}Writer.kt # CUD 전용 컴포넌트
        │   ├── {Domain}**.kt     # 그 외 서비스 계층에 필요한 컴포넌트
        │   ├── {Domain}.kt       # Domain Model (Entity와 분리된 순수 모델)
        │   └── {Domain}Repository.kt (Interface)
        └── storage/       # [DB] JPA, QueryDSL 구현체
```

## Layer 책임

### Application Layer (응용 계층)
- API 엔드포인트 정의 (Controller)
- HTTP 요청/응답 처리
- Web DTO 변환 (Request → Business DTO, Business DTO → Response)

### Business Layer (서비스 계층)
- 유즈케이스 조립 및 트랜잭션 관리
- 여러 도메인 로직 조합
- 레이어 간 데이터 전송용 DTO 사용

### Implement Layer (구현 계층)
- 도메인 핵심 비즈니스 로직
- Repository 인터페이스 정의
- Reader/Writer 컴포넌트 (기능별 분리)
- 도메인 모델 (Domain Model)
- 도메인 전용 예외 처리

### Storage Layer (영속성 계층)
- JPA Entity 정의
- Repository 구현체 (QueryDSL + JPA)
- 데이터베이스 접근 상세 구현

## Technology Stack

- **Language**: Kotlin 1.9.25 (JDK 21)
- **Framework**: Spring Boot 3.4.1
- **Database**: MySQL 8.0+
  - Local: 로컬 MySQL (Workbench 사용)
  - Prod: AWS RDS (MySQL)
- **ORM/Query**: Spring Data JPA + QueryDSL
- **Testing**: 테스트 코드를 작성하지 않음 (모든 검증은 실제 구동으로 대체)

## 계층 간 의존성 규칙

✅ **허용**:
- Application → Business
- Business → Implement
- Implement → Storage

❌ **금지**:
- Storage → Implement (역방향)
- Application → Implement (계층 건너뛰기)
- Service에서 Repository 구현체 직접 의존 (인터페이스만 의존)
- 
* 혹시나 위와같은 역방향 의존성이 표준이거나, 훨씬 간단한 구현이 가능한 경우 사용자(claude 이용자)에게 공지 후, 사용자가 승인할 시 가능
