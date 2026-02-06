# Beyond U Project Guide

## Quick Overview

**Beyond U**는 교환학생 파견 준비를 관리하는 플랫폼입니다.

- **아키텍처**: DDD 기반 4-Layered Architecture
- **기술 스택**: Kotlin + Spring Boot + MySQL + QueryDSL
- **핵심 원칙**: 단방향 의존성, 공통 응답 규격, 계층별 책임 분리

## 필수 규칙

### 1. 계층 구조
모든 도메인은 4개 계층으로 구성:
- **Application** (Controller, Web DTO)
- **Business** (Service, Business DTO)
- **Implement** (Domain Logic, Repository Interface, Reader/Writer)
- **Storage** (Entity, Repository Implementation)

### 2. 공통 규칙
- 모든 API 응답: `ApiResponse<T>` 사용
- 예외 처리: `BusinessException(ErrorCode.XXX)` 던지기
- 엔티티: `BaseEntity` 상속 필수
- Service는 Repository 구현체가 아닌 인터페이스에만 의존

### 3. 이슈/브랜치/커밋
- 이슈: `<tag>: <description>` (예: `feat: University 조회 API 구현`)
- 브랜치: `<tag>/<번호>` (예: `feat/10`)
- 커밋: `[#번호]; <tag>: <description>` (예: `[#10]; feat: Repository 구현`)

## 상세 가이드

프로젝트 작업 시 다음 파일들을 참고하세요:

- `.claude/rules/architecture.md` - 아키텍처 상세, 계층별 책임, 기술 스택
- `.claude/rules/coding-standards.md` - 코딩 규칙, 공통 패키지 사용법, 체크리스트
- `.claude/rules/issue-creation.md` - 이슈 생성 규칙, 브랜치/커밋 컨벤션

**AGENTS.md**에도 동일한 내용이 정리되어 있습니다.
